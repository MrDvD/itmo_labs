package com.itmo.mrdvd.object;

public class AuthoredTicket extends Ticket {
  private String author;

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getAuthor() {
    return this.author;
  }
}
