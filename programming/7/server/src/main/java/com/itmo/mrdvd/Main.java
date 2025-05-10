package com.itmo.mrdvd;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.private_scope.PrivateServerExecutor;
import com.itmo.mrdvd.private_scope.PrivateServerProxy;
import com.itmo.mrdvd.proxy.mappers.ObjectDeserializer;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.mappers.PacketQueryMapper;
import com.itmo.mrdvd.proxy.mappers.QueryPacketMapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.public_scope.PublicServerExecutor;
import com.itmo.mrdvd.public_scope.PublicServerProxy;
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
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    String envName, publicHostname;
    int publicPort, privatePort;
    if (args.length < 4) {
      envName = "COLLECT_PATH";
      publicHostname = "localhost";
      publicPort = 8080;
      privatePort = 8090;
      System.err.println(
          "Программа запущена с неполным набором аргументов: активируется режим DEBUG.");
    } else {
      envName = args[0];
      publicHostname = args[1];
      try {
        publicPort = Integer.parseInt(args[2]);
        privatePort = Integer.parseInt(args[3]);
      } catch (NumberFormatException e) {
        System.err.println("Не удалось распарсить порты.");
        return;
      }
    }
    String path = System.getenv(envName);
    if (path == null) {
      System.err.printf("Не указана переменная окружения \"%s\".\n", envName);
      return;
    }
    Selector selector;
    try {
      selector = Selector.open();
    } catch (IOException e) {
      System.err.println("Не удалось создать селектор.");
      return;
    }
    QueryPacketMapper queryPacket = new QueryPacketMapper(new ObjectSerializer<>(new XmlMapper()));
    ObjectDeserializer<? extends EmptyPacket> deserialPacket =
        new ObjectDeserializer<>(new XmlMapper(), EmptyPacket.class);
    ObjectSerializer<Packet> serialPacket =
        new ObjectSerializer<>(XmlMapper.builder().defaultUseWrapper(true).build());
    ObjectSerializer<TicketCollection> serialCollection = new ObjectSerializer<>(new XmlMapper());
    ObjectDeserializer<TicketCollection> deserialCollection =
        new ObjectDeserializer<>(new XmlMapper(), TicketCollection.class);
    TicketCollection collect = new TicketCollection();
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
    PublicServerExecutor publicExec = new PublicServerExecutor(collect, validator);
    PrivateServerExecutor privateExec =
        new PrivateServerExecutor(
            listener,
            collect,
            serialCollection,
            deserialCollection,
            new FileIO(Path.of(""), FileSystems.getDefault()),
            path,
            validator);
    PublicServerProxy publicProxy =
        new PublicServerProxy(
            publicExec,
            new PacketQueryMapper(new ObjectDeserializer<>(new XmlMapper(), List.class)));
    PrivateServerProxy privateProxy =
        new PrivateServerProxy(
            privateExec,
            publicProxy,
            new PacketQueryMapper(new ObjectDeserializer<>(new XmlMapper(), List.class)));
    try {
      ServerSocketChannel publicSock = ServerSocketChannel.open();
      ServerSocketChannel privateSock = ServerSocketChannel.open();
      publicSock.bind(new InetSocketAddress(publicHostname, publicPort));
      privateSock.bind(new InetSocketAddress("localhost", privatePort));
      listener.addListener(
          publicSock,
          (Packet p) -> {
            return publicProxy.processPacket(p, queryPacket);
          });
      listener.addListener(
          privateSock,
          (Packet p) -> {
            return privateProxy.processPacket(
                p,
                queryPacket,
                (Packet q) -> {
                  return publicProxy.processPacket(q, queryPacket);
                });
          });
    } catch (IOException e) {
      System.err.println("Не удалось привязать сокеты к серверу.");
      return;
    }
    listener.start();
  }
}
