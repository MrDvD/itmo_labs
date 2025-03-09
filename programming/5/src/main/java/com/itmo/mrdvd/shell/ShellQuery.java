package com.itmo.mrdvd.shell;

public abstract class ShellQuery {
   private final String cmd;
   private final String[] params;

   public ShellQuery(String cmd, String[] params) {
      this.cmd = cmd;
      this.params = params;
   }

   public String cmd() {
      return this.cmd;
   }

   public String[] params() {
      return this.params;
   }
}
