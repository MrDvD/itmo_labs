package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.EmptyServiceQuery;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.CommandMeta;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CacheQueriesStrategy implements ProxyStrategy {
  private final AbstractSender<Packet> sender;
  private final AbstractExecutor exec;
  private final Mapper<? super ServiceQuery, Packet> serial;
  private final Mapper<Packet, ? extends ServiceQuery> deserial;
  private final Mapper<Map<String, String>, CommandMeta> metaMapper;
  private final ProxyStrategy prev;

  public CacheQueriesStrategy(
      AbstractSender<Packet> sender,
      AbstractExecutor exec,
      Mapper<? super ServiceQuery, Packet> serial,
      Mapper<Packet, ? extends ServiceQuery> deserial,
      Mapper<Map<String, String>, CommandMeta> metaMapper) {
    this(sender, exec, serial, deserial, metaMapper, null);
  }

  public CacheQueriesStrategy(
      AbstractSender<Packet> sender,
      AbstractExecutor exec,
      Mapper<? super ServiceQuery, Packet> serial,
      Mapper<Packet, ? extends ServiceQuery> deserial,
      Mapper<Map<String, String>, CommandMeta> metaMapper,
      ProxyStrategy prev) {
    this.sender = sender;
    this.serial = serial;
    this.deserial = deserial;
    this.exec = exec;
    this.metaMapper = metaMapper;
    this.prev = prev;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q)
      throws IllegalStateException, RuntimeException {
    if (this.exec == null) {
      throw new IllegalStateException("Не передан исполнитель для кеширования запросов.");
    }
    if (this.prev != null) {
      Optional<ServiceQuery> prevResult = this.prev.make(q);
      if (prevResult.isPresent()) {
        q = prevResult.get();
      }
    }
    try {
      Optional<? extends Packet> serialized = this.serial.convert(q);
      if (serialized.isEmpty()) {
        throw new RuntimeException("Не удалось сериализовать запрос.");
      }
      this.sender.connect();
      Optional<? extends Packet> r = this.sender.send(serialized.get());
      if (r.isPresent()) {
        Optional<? extends ServiceQuery> deserialized = this.deserial.convert(r.get());
        if (deserialized.isEmpty()) {
          throw new RuntimeException("Не удалось десериализовать ответ.");
        }
        if (q.getName().equals(deserialized.get().getName())) {
          this.exec.clearCache();
          for (Object qq : (List) deserialized.get().getArgs()) {
            Optional<CommandMeta> m = this.metaMapper.convert((Map) qq);
            if (m.isPresent()) {
              this.exec.setCache(m.get());
            }
          }
          return Optional.of(
              ServiceQuery.of(q.getName(), List.of("Получен набор запросов от сервера.")));
        }
        return Optional.of(deserialized.get());
      }
      return Optional.of(new EmptyServiceQuery());
    } catch (ClassCastException e) {
      throw new RuntimeException("Не удалось распознать полученные запросы.");
    } catch (IOException e) {
      throw new RuntimeException("Не удалось отправить запрос.");
    }
  }
}
