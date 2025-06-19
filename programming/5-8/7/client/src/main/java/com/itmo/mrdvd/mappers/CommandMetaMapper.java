package com.itmo.mrdvd.mappers;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.executor.CommandMeta;
import java.util.Map;
import java.util.Optional;

public class CommandMetaMapper implements Mapper<Map<String, String>, CommandMeta> {
  @Override
  public Optional<CommandMeta> convert(Map<String, String> map) {
    if (!map.containsKey("cmd") || !map.containsKey("signature") || !map.containsKey("desc")) {
      return Optional.empty();
    }
    return Optional.of(CommandMeta.of(map.get("cmd"), map.get("signature"), map.get("desc")));
  }
}
