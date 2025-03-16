package com.itmo.mrdvd.command;

public interface Command {
  public void execute();

  public String name();

  public String signature();

  public String description();
}
