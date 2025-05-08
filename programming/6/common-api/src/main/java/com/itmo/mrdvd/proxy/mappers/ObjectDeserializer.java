package com.itmo.mrdvd.proxy.mappers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Optional;

/**
 * A wrapper of Jackson's ObjectMapper so it has needed configuration & implements necessary
 * interface.
 */
public class ObjectDeserializer<T> implements Mapper<String, T> {
  private final ObjectMapper mapper;
  private final Class<T> clz;

  public ObjectDeserializer(ObjectMapper obj, Class<T> clz) {
    this.mapper = obj;
    this.clz = clz;
    mapper.registerModule(new JavaTimeModule());
    mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    mapper.setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Override
  public Optional<T> convert(String obj) {
    try {
      return Optional.of(mapper.readValue(obj, this.clz));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }
}
