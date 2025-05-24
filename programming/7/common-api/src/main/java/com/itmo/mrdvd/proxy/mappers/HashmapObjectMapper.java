package com.itmo.mrdvd.proxy.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import java.util.Optional;

public class HashmapObjectMapper<T> implements Mapper<Map<String, Object>, T> {
  private final ObjectMapper mapper;
  private final Class<T> clz;

  public HashmapObjectMapper(ObjectMapper mapper, Class<T> clz) {
    this.mapper = mapper;
    this.mapper.registerModule(new JavaTimeModule());
    this.clz = clz;
  }

  @Override
  public Optional<T> convert(Map<String, Object> m) {
    try {
      return Optional.of(this.mapper.convertValue(m, this.clz));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
