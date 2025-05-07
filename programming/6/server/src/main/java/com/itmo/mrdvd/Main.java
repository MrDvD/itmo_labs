package com.itmo.mrdvd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.private_scope.PrivateServerExecutor;
import com.itmo.mrdvd.private_scope.PrivateServerProxy;
import com.itmo.mrdvd.proxy.EmptyQuery;
import com.itmo.mrdvd.proxy.mappers.ObjectDeserializer;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.public_scope.PublicServerExecutor;
import com.itmo.mrdvd.public_scope.PublicServerProxy;
import com.itmo.mrdvd.validators.CoordinatesValidator;
import com.itmo.mrdvd.validators.EventValidator;
import com.itmo.mrdvd.validators.TicketValidator;

public class Main {
  public static void main(String[] args) throws IOException {
    ObjectDeserializer<? extends EmptyQuery> serialQuery =
        new ObjectDeserializer<>(new XmlMapper(), EmptyQuery.class);
    ObjectSerializer<Response> serialResponse =
        new ObjectSerializer<>(XmlMapper.builder().defaultUseWrapper(true).build());
    ObjectSerializer<TicketCollection> serialCollection =
        new ObjectSerializer<>(new XmlMapper());
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
            System.getenv("COLLECT_PATH"),
            validator);
    PublicServerProxy publicProxy = new PublicServerProxy(publicExec);
    PrivateServerProxy privateProxy = new PrivateServerProxy(privateExec, publicProxy);
    ServerListener listener = new ServerListener(Selector.open(), serialQuery, serialResponse);
    ServerSocketChannel publicSock = ServerSocketChannel.open();
    ServerSocketChannel privateSock = ServerSocketChannel.open();
    publicSock.bind(new InetSocketAddress("localhost", 8080));
    privateSock.bind(new InetSocketAddress("localhost", 8090));
    listener.addListener(publicSock, publicProxy::processQuery);
    listener.addListener(privateSock, privateProxy::processQuery);
    listener.start();
  }
}
