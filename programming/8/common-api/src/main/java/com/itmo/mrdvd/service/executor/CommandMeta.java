package com.itmo.mrdvd.service.executor;

/** DTO for keeping information about available commands. */
public interface CommandMeta {
  public String getCmd();

  public String getSignature();

  public String getDesc();

  public static CommandMeta of(String cmd, String signature, String desc) {
    return new CommandMeta() {
      @Override
      public String getCmd() {
        return cmd;
      }

      @Override
      public String getSignature() {
        return signature;
      }

      @Override
      public String getDesc() {
        return desc;
      }
    };
  }
}
