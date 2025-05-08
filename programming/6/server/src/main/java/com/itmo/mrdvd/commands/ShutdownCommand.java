package com.itmo.mrdvd.commands;

import java.util.List;

import com.itmo.mrdvd.service.Service;
import com.itmo.mrdvd.service.executor.Command;

public class ShutdownCommand implements Command<Void> {
  private final Service service;
  
  public ShutdownCommand(Service service) {
    this.service = service;
  }

  @Override
  public Void execute(List<Object> params) {
    this.service.stop();
    return null;
  }

  @Override
  public String name() {
    return "shutdown";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "выключить сервер";
  }
}
