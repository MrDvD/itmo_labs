package com.itmo.mrdvd.proxy;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CollectionAwareSerializer extends StdSerializer<Object> {
  public CollectionAwareSerializer() {
    super(Object.class);
  }

  @Override
  public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    if (value instanceof List collect) {
      gen.writeStartArray();
      for (Object item : collect) {
        gen.writeObject(item);
      }
      gen.writeEndArray();
    } else {
      gen.writeObject(value);
    }
  }
}