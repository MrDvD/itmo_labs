package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.executor.AbstractCommandMeta;

public class FetchAllCommandMeta extends AbstractCommandMeta {
  public FetchAllCommandMeta() {
    super("fetch_all", "fetch_all", "получить доступные запросы от сервера");
  }
}
