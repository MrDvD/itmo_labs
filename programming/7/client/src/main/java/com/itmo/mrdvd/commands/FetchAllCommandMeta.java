package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.executor.CommandMeta;

public class FetchAllCommandMeta implements CommandMeta {
  @Override
  public String getCmd() {
    return "fetch_all";
  }

  @Override
  public String getSignature() {
    return getCmd();
  }

  @Override
  public String getDesc() {
    return "получить доступные запросы от сервера";
  }
}
