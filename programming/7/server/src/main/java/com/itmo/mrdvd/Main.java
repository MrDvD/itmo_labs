package com.itmo.mrdvd;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.login.BCryptHash;
import com.itmo.mrdvd.collection.login.LoginCollection;
import com.itmo.mrdvd.collection.login.LoginJdbc;
import com.itmo.mrdvd.collection.ticket.TicketCollection;
import com.itmo.mrdvd.collection.ticket.TicketJdbc;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.privateScope.PrivateServerExecutor;
import com.itmo.mrdvd.privateScope.PrivateServerProxy;
import com.itmo.mrdvd.proxy.mappers.AuthMapper;
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

/** TODO:
 * 1. Hide LoginCommand as a service one (client has its own command)
 * 2. Add collection metadata to info command (+update TicketCollection constructor) */
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
    PacketQueryMapper packetQuery = new PacketQueryMapper(new ObjectDeserializer<>(new XmlMapper(), List.class));
    ObjectDeserializer<? extends EmptyPacket> deserialPacket =
        new ObjectDeserializer<>(new XmlMapper(), EmptyPacket.class);
    ObjectSerializer<Packet> serialPacket =
        new ObjectSerializer<>(XmlMapper.builder().defaultUseWrapper(true).build());
    TicketJdbc jdbc = new TicketJdbc(String.format("jdbc:postgresql://%s:5432/%s", pgHost, pgDbname), envUser, envPass);
    TicketCollection collect = new TicketCollection(jdbc);
    TicketValidator validator =
        new TicketValidator(new CoordinatesValidator(), new EventValidator());
    ServerListenerService<Packet> listener =
        new ServerListenerService<>(
            selector,
            new ServerConnectionAcceptor(selector),
            new ServerClientHandler<>(
                StandardCharsets.UTF_8,
                serialPacket,
                deserialPacket,
                new ServerResponseSender(StandardCharsets.UTF_8)),
            8192);
    BCryptHash hash = new BCryptHash();
    LoginCollection loginCollection =
        new LoginCollection(
            new LoginJdbc(String.format("jdbc:postgresql://%s:5432/%s", pgHost, pgDbname), envUser, envPass, hash));
    PublicServerExecutor publicExec =
        new PublicServerExecutor(collect, validator, loginCollection, hash);
    PrivateServerExecutor privateExec = new PrivateServerExecutor(listener, jdbc, collect);
    PublicServerProxy publicProxy = new PublicServerProxy(publicExec, new AuthMapper(), new HashmapObjectMapper<>(new XmlMapper(), AuthoredTicket.class));
    PrivateServerProxy privateProxy = new PrivateServerProxy(privateExec, publicProxy, new AuthMapper());
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