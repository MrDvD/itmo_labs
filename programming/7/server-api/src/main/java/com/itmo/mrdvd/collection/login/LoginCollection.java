package com.itmo.mrdvd.collection.login;

import com.itmo.mrdvd.collection.CacheWorker;
import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.object.LoginPasswordPair;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class LoginCollection
    implements CacheWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> {
  private Set<LoginPasswordPair> cache;
  private final CrudWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> dbworker;

  public LoginCollection(CrudWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> dbworker) {
    this(dbworker, new HashSet<>());
  }

  public LoginCollection(
      CrudWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> dbworker,
      Set<LoginPasswordPair> cache) {
    this.dbworker = dbworker;
    this.cache = cache;
  }

  @Override
  public void setCache(Set<LoginPasswordPair> cache) {
    this.cache = cache;
  }

  @Override
  public void clearCache() {
    this.cache.clear();
  }

  @Override
  public Optional<LoginPasswordPair> add(LoginPasswordPair obj, Predicate<LoginPasswordPair> cond) {
    Optional<LoginPasswordPair> pair = dbworker.add(obj, cond);
    if (pair.isPresent()) {
      this.cache.add(pair.get());
    }
    return pair;
  }

  @Override
  public Optional<LoginPasswordPair> update(
      String key, LoginPasswordPair obj, Predicate<LoginPasswordPair> cond) {
    Optional<LoginPasswordPair> pair = dbworker.update(key, obj, cond);
    if (pair.isPresent()) {
      this.cache.removeIf(t -> t.getLogin().equals(key));
      this.cache.add(pair.get());
    }
    return pair;
  }

  @Override
  public Optional<? extends LoginPasswordPair> get(String key) {
    return this.cache.stream().filter(pair -> pair.getLogin().equals(key)).findAny();
  }

  @Override
  public Set<LoginPasswordPair> getAll() {
    return this.cache;
  }

  @Override
  public void remove(String key) {
    this.dbworker.remove(key);
    this.cache.removeIf(login -> login.getLogin().equals(key));
  }

  @Override
  public void clear() {
    this.dbworker.clear();
    this.cache.clear();
  }
}
