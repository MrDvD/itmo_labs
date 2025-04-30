package com.itmo.mrdvd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.builder.builders.SuppliedCoordinatesBuilder;
import com.itmo.mrdvd.builder.builders.SuppliedEventBuilder;
import com.itmo.mrdvd.builder.builders.SuppliedTicketBuilder;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.proxy.EmptyQuery;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.response.Response;

public class Main {
  public static void main(String[] args) throws IOException {
    ObjectSerializer<EmptyQuery> serialQuery = new ObjectSerializer<>(new XmlMapper(), EmptyQuery.class);
    ObjectSerializer<Response> serialResponse = new ObjectSerializer<>(XmlMapper.builder().defaultUseWrapper(true).build(), Response.class);
    TicketCollection collect = new TicketCollection();
    ServerExecutor exec = new ServerExecutor(collect, new SuppliedTicketBuilder(new SuppliedEventBuilder(), new SuppliedCoordinatesBuilder()));
    ServerProxy proxy = new ServerProxy(exec);
    ServerListener listener = new ServerListener(Selector.open(), proxy::processQuery, serialQuery, serialResponse);
    ServerSocketChannel sock = ServerSocketChannel.open();
    sock.bind(new InetSocketAddress("localhost", 8080));
    listener.addListener(sock);
    listener.start();
  }
}
