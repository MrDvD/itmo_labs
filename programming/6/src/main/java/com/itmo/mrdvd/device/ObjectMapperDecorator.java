package com.itmo.mrdvd.device;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperDecorator implements Serializer, Deserializer {
  private final ObjectMapper mapper;
  private final Class<?> cls;

  public ObjectMapperDecorator(ObjectMapper obj, Class<?> cls) {
    this.mapper = obj;
    this.cls = cls;
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    mapper.setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Override
  public <T> Optional<String> serialize(T obj) {
    try {
      return Optional.of(mapper.writeValueAsString(obj));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }

  @Override
  public <T> Optional<? super T> deserialize(String str) {
    try {
      return Optional.of(mapper.readValue(str, cls));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }
}
