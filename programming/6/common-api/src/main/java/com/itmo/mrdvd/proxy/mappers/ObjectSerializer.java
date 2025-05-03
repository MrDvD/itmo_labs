package com.itmo.mrdvd.proxy.mappers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
  private final Class<T> clz;

  public ObjectSerializer(ObjectMapper obj, Class<T> clz) {
    this.mapper = obj;
    this.clz = clz;
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    mapper.setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    // mapper.activateDefaultTyping(
    //   mapper.getPolymorphicTypeValidator(),
    //   ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT,
    //   JsonTypeInfo.As.PROPERTY
    // );
    // DIP violation, i guess
    // mapper.addMixIn(Response.class, ResponseMixin.class);
  }

  // @JsonDeserialize(as = List.class, contentAs = Object.class)
  // private abstract static class ResponseMixin {
  //   @JacksonXmlElementWrapper(useWrapping = false)
  //   @JacksonXmlProperty(localName = "body")
  //   private Object body;
  // }

  @Override
  public String wrap(T obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<T> unwrap(String obj) {
    try {
      return Optional.of(mapper.readValue(obj, this.clz));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }
}
