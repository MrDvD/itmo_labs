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

public class Main {
  public static void main(String[] args) {
    String pgUserVar = "POSTGRES_USER",
        pgPassVar = "POSTGRES_PASSWORD",
        appSecretVar = "APP_SECRET";
    String envUser, envPass, hostname, pgHost, pgDbname;
    int ticketPort = 0, authPort = 0;
    String secret;
    if (args.length < 5) {
      hostname = "localhost";
      ticketPort = 8080;
      authPort = 8090;
      pgHost = "localhost";
      pgDbname = "";
      System.err.println(
          "Программа запущена с неполным набором аргументов: активируется режим DEBUG.");
    } else {
      hostname = args[0];
      pgHost = args[3];
      pgDbname = args[4];
      try {
        ticketPort = Integer.parseInt(args[1]);
        authPort = Integer.parseInt(args[2]);
      } catch (NumberFormatException e) {
        System.err.println("Не удалось распарсить порты.");
        System.exit(1);
      }
    }
    if (System.getenv(pgUserVar) == null) {
      System.err.println(String.format("Ошибка: переменная %s не задана.", pgUserVar));
      System.exit(1);
    }
    if (System.getenv(pgPassVar) == null) {
      System.err.println(String.format("Ошибка: переменная %s не задана.", pgPassVar));
      System.exit(1);
    }
    if (System.getenv(appSecretVar) == null) {
      System.err.println(String.format("Ошибка: переменная %s не задана.", appSecretVar));
      System.exit(1);
    }
    envUser = System.getenv(pgUserVar);
    envPass = System.getenv(pgPassVar);
    secret = System.getenv(appSecretVar);
    MetadataAuthIdMapper idMapper =
        new MetadataAuthIdMapper(
            Metadata.Key.of(
                ContextKeys.TOKEN.getKey().toString(), Metadata.ASCII_STRING_MARSHALLER));
    String jdbcUrl = String.format("jdbc:postgresql://%s:5432/%s", pgHost, pgDbname);
    TicketCollection collect =
        new TicketCollection(
            new TicketJdbc(new LocalDatetimeTimestampMapper(), jdbcUrl, envUser, envPass),
            new ReentrantReadWriteLock());
    BCryptHash hash = new BCryptHash();
    LoginCollection loginCollection =
        new LoginCollection(
            new LoginJdbc(jdbcUrl, envUser, envPass, hash), new ReentrantReadWriteLock());
    MetaCollection metaCollection =
        new MetaCollection(new MetaJdbc(jdbcUrl, envUser, envPass), new ReentrantReadWriteLock());
    ServerExecutor exec =
        new ServerExecutor(
            collect,
            new NodeValidator(
                new TicketValidator(new CoordinatesValidator(), new EventValidator())),
            loginCollection,
            metaCollection,
            new CredentialsJwtMapper(secret),
            new AuthIdValidator(secret),
            hash);
    AuthServiceImpl authService = new AuthServiceImpl(exec);
    AuthGrpcServer authServer =
        new AuthGrpcServer(
            NettyServerBuilder.forAddress(new InetSocketAddress(hostname, authPort)),
            authService,
            idMapper);
    authServer.start();
    TicketGrpcServer ticketServer =
        new TicketGrpcServer(
            NettyServerBuilder.forAddress(new InetSocketAddress(hostname, ticketPort)),
            new TicketServiceImpl(
                exec,
                new UserServiceImpl(new AuthIdUserInfoMapper(secret)),
                new ContextAuthIdMapper()),
            authService,
            idMapper,
            ContextKeys.TOKEN.getKey());
    ticketServer.start();
    ticketServer.block();
  }
}
