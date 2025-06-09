package com.itmo.mrdvd;

import com.itmo.mrdvd.collection.login.BCryptHash;
import com.itmo.mrdvd.collection.login.LoginCollection;
import com.itmo.mrdvd.collection.login.LoginJdbc;
import com.itmo.mrdvd.collection.meta.MetaCollection;
import com.itmo.mrdvd.collection.meta.MetaJdbc;
import com.itmo.mrdvd.collection.ticket.TicketCollection;
import com.itmo.mrdvd.collection.ticket.TicketJdbc;
import com.itmo.mrdvd.mappers.AuthIdUserInfoMapper;
import com.itmo.mrdvd.mappers.ContextAuthIdMapper;
import com.itmo.mrdvd.mappers.CredentialsJwtMapper;
import com.itmo.mrdvd.mappers.LocalDatetimeTimestampMapper;
import com.itmo.mrdvd.mappers.MetadataAuthIdMapper;
import com.itmo.mrdvd.publicScope.PublicServerExecutor;
import com.itmo.mrdvd.service.AuthGrpcServer;
import com.itmo.mrdvd.service.ContextKeys;
import com.itmo.mrdvd.service.TicketGrpcServer;
import com.itmo.mrdvd.service.implementations.AuthServiceImpl;
import com.itmo.mrdvd.service.implementations.TicketServiceImpl;
import com.itmo.mrdvd.service.implementations.UserServiceImpl;
import com.itmo.mrdvd.validators.AuthIdValidator;
import com.itmo.mrdvd.validators.CoordinatesValidator;
import com.itmo.mrdvd.validators.EventValidator;
import com.itmo.mrdvd.validators.NodeValidator;
import com.itmo.mrdvd.validators.TicketValidator;
import io.grpc.Metadata;
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
    String secret = "testytesttoremoveasap";
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
    MetadataAuthIdMapper idMapper =
        new MetadataAuthIdMapper(
            Metadata.Key.of(
                ContextKeys.TOKEN.getKey().toString(), Metadata.ASCII_STRING_MARSHALLER));
    CredentialsJwtMapper tokenMapper = new CredentialsJwtMapper(secret);
    AuthIdUserInfoMapper userMapper = new AuthIdUserInfoMapper(secret);
    LocalDatetimeTimestampMapper timeMapper = new LocalDatetimeTimestampMapper();
    ReentrantReadWriteLock loginCollectionLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock objectCollectionLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock metaCollectionLock = new ReentrantReadWriteLock();
    String jdbcUrl = String.format("jdbc:postgresql://%s:5432/%s", pgHost, pgDbname);
    TicketJdbc jdbc = new TicketJdbc(timeMapper, jdbcUrl, envUser, envPass);
    TicketCollection collect = new TicketCollection(jdbc, objectCollectionLock);
    NodeValidator validator =
        new NodeValidator(new TicketValidator(new CoordinatesValidator(), new EventValidator()));
    AuthIdValidator idValidator = new AuthIdValidator(secret);
    BCryptHash hash = new BCryptHash();
    LoginCollection loginCollection =
        new LoginCollection(new LoginJdbc(jdbcUrl, envUser, envPass, hash), loginCollectionLock);
    MetaCollection metaCollection =
        new MetaCollection(new MetaJdbc(jdbcUrl, envUser, envPass), metaCollectionLock);
    PublicServerExecutor publicExec =
        new PublicServerExecutor(
            collect, validator, loginCollection, metaCollection, tokenMapper, idValidator, hash);
    // PrivateServerExecutor privateExec = new PrivateServerExecutor(listener, jdbc, collect);
    AuthServiceImpl authService = new AuthServiceImpl(publicExec);
    AuthGrpcServer authServer =
        new AuthGrpcServer(
            NettyServerBuilder.forAddress(new InetSocketAddress("localhost", 5555)),
            authService,
            idMapper);
    authServer.start();
    TicketGrpcServer ticketServer =
        new TicketGrpcServer(
            NettyServerBuilder.forAddress(new InetSocketAddress(publicHostname, publicPort)),
            new TicketServiceImpl(
                publicExec,
                new UserServiceImpl(publicExec, userMapper),
                new ContextAuthIdMapper("collect-token")),
            authService,
            idMapper,
            ContextKeys.TOKEN.getKey());
    ticketServer.start();
    ticketServer.block();
  }
}
