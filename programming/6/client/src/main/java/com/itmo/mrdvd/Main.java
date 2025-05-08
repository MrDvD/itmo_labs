package com.itmo.mrdvd;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.builders.InteractiveCoordinatesBuilder;
import com.itmo.mrdvd.builders.InteractiveEventBuilder;
import com.itmo.mrdvd.builders.InteractiveTicketBuilder;
import com.itmo.mrdvd.device.DataConsole;
import com.itmo.mrdvd.device.DefaultTTY;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.proxy.mappers.ObjectDeserializer;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.mappers.PacketQueryMapper;
import com.itmo.mrdvd.proxy.mappers.QueryPacketMapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.service_query.EmptyServiceQuery;
import com.itmo.mrdvd.updaters.InteractiveCoordinatesUpdater;
import com.itmo.mrdvd.updaters.InteractiveEventUpdater;
import com.itmo.mrdvd.updaters.InteractiveTicketUpdater;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

/*
 * 1. Split Update query into two parts:
 *    - check existance of object with the following id (and get it)
 *    - update retrieved object and send it to the server
 */
public class Main {
  public static void main(String[] args) {
    ObjectDeserializer<? extends EmptyPacket> deserialPacket =
        new ObjectDeserializer<>(new XmlMapper(), EmptyPacket.class);
    ObjectSerializer<Packet> serialPacket =
        new ObjectSerializer<>(XmlMapper.builder().defaultUseWrapper(true).build());
    ClientSender sender = new ClientSender(serialPacket, deserialPacket);
    ClientExecutor exec =
        new ClientExecutor(new FileIO(Path.of(""), FileSystems.getDefault()), sender);
    ClientProxy proxy =
        new ClientProxy(
            sender,
            exec,
            new QueryPacketMapper(new ObjectSerializer<>(new XmlMapper())),
            new PacketQueryMapper(new ObjectDeserializer<>(new XmlMapper(), List.class)));
    CollectionShell shell = new CollectionShell(proxy, EmptyServiceQuery::new);
    shell.setBuilders(
        new InteractiveTicketBuilder(
            new InteractiveCoordinatesBuilder(shell), new InteractiveEventBuilder(shell), shell),
        new InteractiveTicketUpdater(
            new InteractiveCoordinatesUpdater(shell), new InteractiveEventUpdater(shell), shell));
    DataConsole console = new DataConsole().init();
    shell.setTty(new DefaultTTY(null, console, console));
    shell.start();
  }
}
