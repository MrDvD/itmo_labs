package com.itmo.mrdvd;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.mappers.ObjectDeserializer;
import com.itmo.mrdvd.proxy.mappers.VariableMapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.CacheQueriesStrategy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.SendServerStrategy;
import com.itmo.mrdvd.proxy.strategies.WrapStrategy;
import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.DefaultCommandMeta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientProxy extends AbstractProxy {
  public ClientProxy(
      AbstractSender<Packet> sender,
      AbstractExecutor exec,
      Mapper<? super ServiceQuery, Packet> serial,
      VariableMapper<Packet, ? extends ServiceQuery, String, List> deserial) {
    this(sender, exec, serial, deserial, new HashMap<>());
  }

  public ClientProxy(
      AbstractSender<Packet> sender,
      AbstractExecutor exec,
      Mapper<? super ServiceQuery, Packet> serial,
      VariableMapper<Packet, ? extends ServiceQuery, String, List> deserial,
      Map<String, ProxyStrategy> strats) {
    super(strats);
    deserial.setStrategy(
        "fetch_all",
        new ObjectDeserializer<>(
            new XmlMapper(),
            TypeFactory.defaultInstance()
                .constructCollectionType(List.class, DefaultCommandMeta.class)));
    setDefaultStrategy(new SendServerStrategy(sender, serial, deserial));
    setStrategy("help", new WrapStrategy(exec));
    setStrategy("exit", new InformStrategy(exec, "Производится выход..."));
    setStrategy("execute_script", new InformStrategy(exec, "Начинается выполнение скрипта..."));
    setStrategy("connect", new InformStrategy(exec, "Связка \"хост-порт\" сохранена."));
    setStrategy("fetch_all", new CacheQueriesStrategy(sender, exec, serial, deserial));
  }
}
