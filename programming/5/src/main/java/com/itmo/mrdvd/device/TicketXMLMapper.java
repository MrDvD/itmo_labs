package com.itmo.mrdvd.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.object.Ticket;

public class TicketXMLMapper implements Serializer<Ticket>, Deserializer<Ticket> {
  private final XmlMapper mapper;

  public TicketXMLMapper() {
    this.mapper = new XmlMapper();
  }

  @Override
  public String serialize(Ticket obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  @Override
  public Ticket deserialize(String str) {
      try {
          return mapper.readValue(str, Ticket.class);
      } catch (JsonProcessingException e) {
         return null;
      }
  }
}
