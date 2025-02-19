package com.itmo.mrdvd.shell;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.command.AddCommand;
import com.itmo.mrdvd.command.AddIfMaxCommand;
import com.itmo.mrdvd.command.ClearCommand;
import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.command.ExitCommand;
import com.itmo.mrdvd.command.HelpCommand;
import com.itmo.mrdvd.command.RemoveAtCommand;
import com.itmo.mrdvd.command.RemoveByIdCommand;
import com.itmo.mrdvd.command.RemoveLastCommand;
import com.itmo.mrdvd.command.ShowCommand;
import com.itmo.mrdvd.command.UpdateCommand;
import com.itmo.mrdvd.device.InputDevice;
import com.itmo.mrdvd.device.OutputDevice;

public class TicketShell extends Shell {
   private Map<String, Command> commands;
   private boolean isOpen;
   public TicketShell(InputDevice in, OutputDevice out, TicketCollection collection) {
      super(in, out);
      this.commands = new TreeMap<String, Command>();
      this.isOpen = false;
      Command add = new AddCommand(collection, in, out);
      commands.put(add.name(), add);
      Command help = new HelpCommand(this, out);
      commands.put(help.name(), help);
      Command exit = new ExitCommand(this);
      commands.put(exit.name(), exit);
      Command update = new UpdateCommand(collection, in, out);
      commands.put(update.name(), update);
      Command clear = new ClearCommand(collection, out);
      commands.put(clear.name(), clear);
      Command removeById = new RemoveByIdCommand(collection, out);
      commands.put(removeById.name(), removeById);
      Command removeAt = new RemoveAtCommand(collection, out);
      commands.put(removeAt.name(), removeAt);
      Command removeLast = new RemoveLastCommand(collection, out);
      commands.put(removeLast.name(), removeLast);
      Command showCommand = new ShowCommand(collection, out);
      commands.put(showCommand.name(), showCommand);
      // Command addIfMaxCommand = new AddIfMaxCommand(collection, in, out);
      // commands.put(addIfMaxCommand.name(), addIfMaxCommand);
   }
   public static class RawCommand {
      String cmd;
      String[] params;
      public RawCommand(int paramsCount) {
         params = new String[paramsCount];
      }
      @Override
      public int hashCode() {
         return cmd.hashCode();
      }
      @Override
      public boolean equals(Object other) {
         if (other == null || !(other instanceof RawCommand)) {
            return false;
         }
         RawCommand otherCmd = (RawCommand) other;
         return this.cmd.equals(otherCmd.cmd);
      }
   }
   public static class TShellParser {
      public static RawCommand parseLine(String line) {
         if (line.isBlank()) {
            return null;
         }
         String[] keys = line.split(" ");
         RawCommand rawCmd = new RawCommand(keys.length - 1);
         rawCmd.cmd = keys[0];
         rawCmd.params = Arrays.copyOfRange(keys, 1, keys.length);
         return rawCmd;
      } 
   }
   @Override
   public void open() {
      this.isOpen = true;
      while (this.isOpen) {
         String strCmd = getInput().read("> ");
         RawCommand rawCmd = TShellParser.parseLine(strCmd);
         if (rawCmd == null) {
            continue;
         }
         if (getCommands().containsKey(rawCmd.cmd)) {
            getCommands().get(rawCmd.cmd).execute(rawCmd.params);
         } else {
            getOutput().writeln(String.format("[ERROR] Не существует команды \"%s\".", rawCmd.cmd));
         }
      }
   }
   @Override
   public void close() {
      this.isOpen = false;
   }
   @Override
   public Map<String, Command> getCommands() {
      return commands;
   }
}
