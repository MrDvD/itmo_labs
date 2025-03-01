package com.itmo.mrdvd.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.TicketCollection;

public class TicketXMLMapper implements Serializer<TicketCollection>, Deserializer<TicketCollection> {
  private final XmlMapper mapper;

  public TicketXMLMapper() {
    this.mapper = new XmlMapper();
  }

  @Override
  public String serialize(TicketCollection obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  @Override
  public TicketCollection deserialize(String str) {
      try {
          return mapper.readValue(str, TicketCollection.class);
      } catch (JsonProcessingException e) {
         return null;
      }
  }
}
