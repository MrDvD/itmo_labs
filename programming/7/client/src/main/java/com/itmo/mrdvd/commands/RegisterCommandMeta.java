package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.executor.CommandMeta;

public class RegisterCommandMeta implements CommandMeta {
  @Override
  public String getCmd() {
    return "register";
  }

  @Override
  public String getSignature() {
    return getCmd() + " {login} {password}";
  }

  @Override
  public String getDesc() {
    return "зарегистрировать нового пользователя";
  }
}
