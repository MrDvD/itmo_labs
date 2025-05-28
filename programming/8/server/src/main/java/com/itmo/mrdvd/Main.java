package com.itmo.mrdvd;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.login.BCryptHash;
import com.itmo.mrdvd.collection.login.LoginCollection;
import com.itmo.mrdvd.collection.login.LoginJdbc;
import com.itmo.mrdvd.collection.meta.MetaCollection;
import com.itmo.mrdvd.collection.meta.MetaJdbc;
import com.itmo.mrdvd.collection.ticket.TicketCollection;
import com.itmo.mrdvd.collection.ticket.TicketJdbc;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.proxy.mappers.HashmapObjectMapper;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.publicScope.PublicServerExecutor;
import com.itmo.mrdvd.publicScope.PublicServerProxy;
import com.itmo.mrdvd.service.GrpcServer;
import com.itmo.mrdvd.validators.CoordinatesValidator;
import com.itmo.mrdvd.validators.EventValidator;
import com.itmo.mrdvd.validators.TicketValidator;
import io.grpc.netty.NettyServerBuilder;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO: 1. Plan out the api: - api/v1/register - api/v1/login - api/v1/info - api/v1/tickets -
 * api/v1/ticket/1 - api/v1/add - api/v1/update/2 - api/v1/remove_at/1 - api/v1/remove_last -
 * api/v1/remove_by_id/1 localhost: - api/v1/clear - api/v1/shutdown Перехватчики (Interceptors) -
 * для аутентификации? Metadata is information about a particular RPC call (such as authentication
 * details) in the form of a list of key-value pairs, where the keys are strings and the values are
 * typically strings, but can be binary data.
 */
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
    ObjectSerializer<Object> serialObject =
        new ObjectSerializer<>(XmlMapper.builder().defaultUseWrapper(true).build());
    ReentrantReadWriteLock loginCollectionLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock objectCollectionLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock metaCollectionLock = new ReentrantReadWriteLock();
    String jdbcUrl = String.format("jdbc:postgresql://%s:5432/%s", pgHost, pgDbname);
    TicketJdbc jdbc = new TicketJdbc(jdbcUrl, envUser, envPass);
    TicketCollection collect = new TicketCollection(jdbc, objectCollectionLock);
    TicketValidator validator =
        new TicketValidator(new CoordinatesValidator(), new EventValidator());

    BCryptHash hash = new BCryptHash();
    LoginCollection loginCollection =
        new LoginCollection(new LoginJdbc(jdbcUrl, envUser, envPass, hash), loginCollectionLock);
    MetaCollection metaCollection =
        new MetaCollection(new MetaJdbc(jdbcUrl, envUser, envPass), metaCollectionLock);
    PublicServerExecutor publicExec =
        new PublicServerExecutor(
            collect, validator, loginCollection, metaCollection, serialObject, hash);
    // PrivateServerExecutor privateExec = new PrivateServerExecutor(listener, jdbc, collect);
    HashmapObjectMapper<LoginPasswordPair> authMapper =
        new HashmapObjectMapper<>(new XmlMapper(), LoginPasswordPair.class);
    PublicServerProxy publicProxy =
        new PublicServerProxy(
            publicExec, authMapper, new HashmapObjectMapper<>(new XmlMapper(), Node.class));
    // PrivateServerProxy privateProxy = new PrivateServerProxy(privateExec, publicProxy,
    // authMapper);
    GrpcServer server =
        new GrpcServer(
            NettyServerBuilder.forAddress(new InetSocketAddress(publicHostname, publicPort)),
            publicExec);
    server.start();
    server.block();
  }
}
