package com.itmo.mrdvd.command;

import com.itmo.mrdvd.shell.Shell;

public class ExitCommand implements Command  {
   private Shell shell;
   public ExitCommand(Shell shell) {
      this.shell = shell;
   }
   @Override
   public void execute(String[] params) {
      shell.close();
   }
   @Override
   public String name() {
      return "exit";
   }
   @Override
   public String description() {
      return "завершить программу (без сохранения в файл)";
   }
}
