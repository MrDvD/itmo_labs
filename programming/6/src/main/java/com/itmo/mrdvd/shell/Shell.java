package com.itmo.mrdvd.shell;

import java.util.Optional;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;

public interface Shell {
  public DataInputDevice getIn();

  public OutputDevice getOut();

  public ClientProxy getProxy();

  public Optional<Query> fetchQuery(String name);

  public void processLine();

  public Shell forkSubshell(DataInputDevice in, OutputDevice out);

  public void open();

  public void close();
}
