package com.itmo.mrdvd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

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
import com.itmo.mrdvd.validators.CoordinatesValidator;
import com.itmo.mrdvd.validators.EventValidator;
import com.itmo.mrdvd.validators.TicketValidator;

public class Main {
  public static void main(String[] args) throws IOException {
    String envName, publicHostname;
    int publicPort, privatePort;
    if (args.length < 4) {
      envName = "COLLECT_PATH";
      publicHostname = "localhost";
      publicPort = 8080;
      privatePort = 8090;
      System.err.println("Программа запущена с неполным набором аргументов: активируется режим DEBUG.");
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
    ServerListener listener = new ServerListener(Selector.open(), deserialPacket, serialPacket);
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
    listener.start();
  }
}
