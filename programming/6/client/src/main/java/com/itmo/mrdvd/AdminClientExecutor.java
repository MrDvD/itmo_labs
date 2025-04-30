package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.service.executor.Command;

/**
 * WIP
 */
public class AdminClientExecutor extends ClientExecutor {
  public AdminClientExecutor(DataFileDescriptor fd) {
    this(new HashMap<>());
  }

  public AdminClientExecutor(DataFileDescriptor fd, Map<String, Command<?>> commands) {
    super(commands);
    // add new commands there
  }
}
