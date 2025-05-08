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
import com.itmo.mrdvd.validators.CoordinatesValidator;
import com.itmo.mrdvd.validators.EventValidator;
import com.itmo.mrdvd.validators.TicketValidator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    String path = System.getenv("COLLECT_PATH");
    if (path == null) {
      System.err.println("Не указана переменная окружения COLLECT_PATH.");
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
    PublicServerExecutor publicExec = new PublicServerExecutor(collect, validator);
    PrivateServerExecutor privateExec =
        new PrivateServerExecutor(
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
    ServerListener listener = new ServerListener(Selector.open(), deserialPacket, serialPacket);
    ServerSocketChannel publicSock = ServerSocketChannel.open();
    ServerSocketChannel privateSock = ServerSocketChannel.open();
    publicSock.bind(new InetSocketAddress("localhost", 8080));
    privateSock.bind(new InetSocketAddress("localhost", 8090));
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
