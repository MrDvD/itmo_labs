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
import com.itmo.mrdvd.proxy.EmptyQuery;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.response.Response;

public class Main {
  public static void main(String[] args) throws IOException {
    ObjectSerializer<EmptyQuery> serialQuery = new ObjectSerializer<>(new XmlMapper(), EmptyQuery.class);
    ObjectSerializer<Response> serialResponse = new ObjectSerializer<>(XmlMapper.builder().defaultUseWrapper(true).build(), Response.class);
    ObjectSerializer<TicketCollection> serialCollection = new ObjectSerializer<>(new XmlMapper(), TicketCollection.class);
    TicketCollection collect = new TicketCollection();
    PublicServerExecutor publicExec = new PublicServerExecutor(collect);
    PrivateServerExecutor privateExec = new PrivateServerExecutor(collect, serialCollection, new FileIO(Path.of(""), FileSystems.getDefault()));
    ServerProxy proxy = new ServerProxy(publicExec);
    ServerListener listener = new ServerListener(Selector.open(), proxy::processQuery, serialQuery, serialResponse);
    ServerSocketChannel sock = ServerSocketChannel.open();
    sock.bind(new InetSocketAddress("localhost", 8080));
    listener.addListener(sock);
    listener.start();
  }
}
