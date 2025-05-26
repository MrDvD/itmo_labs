package com.itmo.mrdvd;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.login.BCryptHash;
import com.itmo.mrdvd.collection.login.LoginCollection;
import com.itmo.mrdvd.collection.login.LoginJdbc;
import com.itmo.mrdvd.collection.meta.MetaCollection;
import com.itmo.mrdvd.collection.meta.MetaJdbc;
import com.itmo.mrdvd.collection.ticket.TicketCollection;
import com.itmo.mrdvd.collection.ticket.TicketJdbc;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.privateScope.PrivateServerExecutor;
import com.itmo.mrdvd.privateScope.PrivateServerProxy;
import com.itmo.mrdvd.proxy.mappers.HashmapObjectMapper;
import com.itmo.mrdvd.proxy.mappers.ObjectDeserializer;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.mappers.PacketQueryMapper;
import com.itmo.mrdvd.proxy.mappers.QueryPacketMapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.publicScope.PublicServerExecutor;
import com.itmo.mrdvd.publicScope.PublicServerProxy;
import com.itmo.mrdvd.service.ServerClientHandler;
import com.itmo.mrdvd.service.ServerConnectionAcceptor;
import com.itmo.mrdvd.service.ServerListenerService;
import com.itmo.mrdvd.service.ServerResponseSender;
import com.itmo.mrdvd.validators.CoordinatesValidator;
import com.itmo.mrdvd.validators.EventValidator;
import com.itmo.mrdvd.validators.TicketValidator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
  public static void main(String[] args) {
    String envUser, envPass, publicHostname, pgHost, pgDbname;
    int publicPort, privatePort;
    if (args.length < 5) {
      publicHostname = "localhost";
      publicPort = 8080;
      privatePort = 8090;
      pgHost = "localhost";
      pgDbname = "";
      System.err.println(
          "Программа запущена с неполным набором аргументов: активируется режим DEBUG.");
    } else {
      publicHostname = args[0];
      pgHost = args[3];
      pgDbname = args[4];
      try {
        publicPort = Integer.parseInt(args[1]);
        privatePort = Integer.parseInt(args[2]);
      } catch (NumberFormatException e) {
        System.err.println("Не удалось распарсить порты.");
        return;
      }
    }
    System.out.print("Введите имя пользователя PostgreSQL: ");
    envUser = System.console().readLine();
    System.out.print("Введите пароль пользователя PostgreSQL: ");
    envPass = System.console().readLine();
    Selector selector;
    try {
      selector = Selector.open();
    } catch (IOException e) {
      System.err.println("Не удалось создать селектор.");
      return;
    }
    QueryPacketMapper queryPacket = new QueryPacketMapper(new ObjectSerializer<>(new XmlMapper()));
    PacketQueryMapper packetQuery =
        new PacketQueryMapper(new ObjectDeserializer<>(new XmlMapper(), List.class));
    ObjectDeserializer<? extends EmptyPacket> deserialPacket =
        new ObjectDeserializer<>(new XmlMapper(), EmptyPacket.class);
    ObjectSerializer<Object> serialObject =
        new ObjectSerializer<>(XmlMapper.builder().defaultUseWrapper(true).build());
    ReentrantReadWriteLock selectorLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock socketsLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock loginCollectionLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock objectCollectionLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock metaCollectionLock = new ReentrantReadWriteLock();
    String jdbcUrl = String.format("jdbc:postgresql://%s:5432/%s", pgHost, pgDbname);
    TicketJdbc jdbc = new TicketJdbc(jdbcUrl, envUser, envPass);
    TicketCollection collect = new TicketCollection(jdbc, objectCollectionLock);
    TicketValidator validator =
        new TicketValidator(new CoordinatesValidator(), new EventValidator());
    ServerListenerService<Packet> listener =
        new ServerListenerService<>(
            selector,
            new ServerConnectionAcceptor(selector, selectorLock, socketsLock),
            new ServerClientHandler<>(
                StandardCharsets.UTF_8,
                serialObject,
                deserialPacket,
                new ServerResponseSender(StandardCharsets.UTF_8),
                selectorLock),
            8192,
            selectorLock,
            socketsLock,
            new ForkJoinPool(), 
            Executors.newCachedThreadPool());
    BCryptHash hash = new BCryptHash();
    LoginCollection loginCollection =
        new LoginCollection(new LoginJdbc(jdbcUrl, envUser, envPass, hash), loginCollectionLock);
    MetaCollection metaCollection =
        new MetaCollection(new MetaJdbc(jdbcUrl, envUser, envPass), metaCollectionLock);
    PublicServerExecutor publicExec =
        new PublicServerExecutor(
            collect, validator, loginCollection, metaCollection, serialObject, hash);
    PrivateServerExecutor privateExec = new PrivateServerExecutor(listener, jdbc, collect);
    HashmapObjectMapper<LoginPasswordPair> authMapper =
        new HashmapObjectMapper<>(new XmlMapper(), LoginPasswordPair.class);
    PublicServerProxy publicProxy =
        new PublicServerProxy(
            publicExec,
            authMapper,
            new HashmapObjectMapper<>(new XmlMapper(), AuthoredTicket.class));
    PrivateServerProxy privateProxy = new PrivateServerProxy(privateExec, publicProxy, authMapper);
    try {
      ServerSocketChannel publicSock = ServerSocketChannel.open();
      ServerSocketChannel privateSock = ServerSocketChannel.open();
      publicSock.bind(new InetSocketAddress(publicHostname, publicPort));
      privateSock.bind(new InetSocketAddress("localhost", privatePort));
      listener.addListener(
          publicSock,
          (Packet p) -> {
            return publicProxy.processPacket(p, queryPacket, packetQuery);
          });
      listener.addListener(
          privateSock,
          (Packet p) -> {
            return privateProxy.processPacket(p, queryPacket, packetQuery);
          });
    } catch (IOException e) {
      System.err.println("Не удалось привязать сокеты к серверу.");
      return;
    }
    listener.start();
  }
}
