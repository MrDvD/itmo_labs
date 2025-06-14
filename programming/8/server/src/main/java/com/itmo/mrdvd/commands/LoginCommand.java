package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.AuthResponse;
import com.itmo.mrdvd.AuthStatus;
import com.itmo.mrdvd.Credentials;
import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.collection.SelfContainedHash;
import com.itmo.mrdvd.mappers.Mapper;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LoginCommand implements Command<AuthResponse> {
  private final CachedCrudWorker<Credentials, Set<Credentials>, String> loginWorker;
  private final SelfContainedHash hash;
  private final Mapper<Credentials, String> tokenMapper;

  public LoginCommand(
      CachedCrudWorker<Credentials, Set<Credentials>, String> loginWorker,
      SelfContainedHash hash,
      Mapper<Credentials, String> tokenMapper) {
    this.loginWorker = loginWorker;
    this.hash = hash;
    this.tokenMapper = tokenMapper;
  }

  @Override
  public AuthResponse execute(List<Object> params) throws IllegalStateException {
    if (this.loginWorker == null) {
      throw new IllegalStateException("Не предоставлен обработчик логина.");
    }
    if (this.hash == null) {
      throw new IllegalStateException("Не предоставлен обработчик хеширования.");
    }
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    Credentials pair = null;
    try {
      pair = (Credentials) params.get(0);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать аргументы команды.");
    }
    Optional<? extends Credentials> dbPair = this.loginWorker.get(pair.getLogin());
    if (dbPair.isPresent() && this.hash.compare(pair.getPassword(), dbPair.get().getPassword())) {
      Optional<String> token = this.tokenMapper.convert(pair);
      if (token.isPresent()) {
        return AuthResponse.newBuilder()
            .setStatus(AuthStatus.AUTHORIZED)
            .setId(AuthID.newBuilder().setToken(token.get()).build())
            .build();
      }
    }
    return AuthResponse.newBuilder().setStatus(AuthStatus.INVALID).build();
  }

  @Override
  public String name() {
    return "login";
  }

  @Override
  public String signature() {
    return name() + " {credentials}";
  }

  @Override
  public String description() {
    return "получить токен для аутентификации";
  }
}
