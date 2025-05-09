package com.itmo.mrdvd.proxy.mappers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Optional;

/**
 * A wrapper of Jackson's ObjectMapper so it has needed configuration & implements necessary
 * interface.
 */
public class ObjectSerializer<T> implements Mapper<T, String> {
  private final ObjectMapper mapper;

  public ObjectSerializer(ObjectMapper obj) {
    this.mapper = obj;
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
  }

  @Override
  public Optional<String> convert(T obj) {
    try {
      return Optional.of(mapper.writeValueAsString(obj));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }
}
