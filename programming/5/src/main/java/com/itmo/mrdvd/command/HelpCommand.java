package com.itmo.mrdvd.command;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.shell.Shell;

public class HelpCommand implements Command {
   private Shell shell;
   private OutputDevice out;
   public HelpCommand(Shell shell, OutputDevice out) {
      this.shell = shell;
      this.out = out;
   }
   @Override
   public void execute(String[] params) {
      for (Command cmd : shell.getCommands().values()) {
         out.write(String.format("%s\t%s\n", cmd.name(), cmd.description()));
      }
   }
   @Override
   public String name() {
      return "help";
   }
   @Override
   public String description() {
      return "вывести справку по доступным командам";
   }
}
