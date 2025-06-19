package com.itmo.mrdvd.service;

import java.util.concurrent.ExecutorService;

public interface MultithreadModule {
  public void setExecutorService(ExecutorService service);
}
