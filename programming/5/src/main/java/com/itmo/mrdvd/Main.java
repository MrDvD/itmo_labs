package com.itmo.mrdvd;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.collection.TicketIdGenerator;
import com.itmo.mrdvd.command.AddCommand;
import com.itmo.mrdvd.command.AddIfMaxCommand;
import com.itmo.mrdvd.command.ClearCommand;
import com.itmo.mrdvd.command.CountGreaterThanEventCommand;
import com.itmo.mrdvd.command.ExecuteScriptCommand;
import com.itmo.mrdvd.command.ExitCommand;
import com.itmo.mrdvd.command.HelpCommand;
import com.itmo.mrdvd.command.InfoCommand;
import com.itmo.mrdvd.command.LoadCommand;
import com.itmo.mrdvd.command.MinByPriceCommand;
import com.itmo.mrdvd.command.PrintFieldDescendingTypeCommand;
import com.itmo.mrdvd.command.ReadEnvironmentFilepathCommand;
import com.itmo.mrdvd.command.RemoveAtCommand;
import com.itmo.mrdvd.command.RemoveByIdCommand;
import com.itmo.mrdvd.command.RemoveLastCommand;
import com.itmo.mrdvd.command.SaveCommand;
import com.itmo.mrdvd.command.ShowCommand;
import com.itmo.mrdvd.command.UpdateCommand;
import com.itmo.mrdvd.device.Console;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.ObjectMapperDecorator;
import com.itmo.mrdvd.shell.TicketShell;

/*
 * TODO:
 * 1. In UpdateCommand set the max length of printed value of ticket fields;
 * 2. Print fixed length in command description
 * 3. Add BuilderObject in AddCommand as passed param
 * 
 * 4. Raise Exceptions instead of returning an int.
 * 5. Fix a bug for ctrl + d
 */

public class Main {
  public static void main(String[] args) throws JsonProcessingException {
    Console console = new Console().init();
    TicketCollection collection =
        new TicketCollection("My Collection", new TicketIdGenerator(), new TicketIdGenerator());
    ObjectMapperDecorator mapper = new ObjectMapperDecorator(new XmlMapper());
    TicketShell shell = new TicketShell(console, console, new TreeMap<>(), new ArrayList<>());
    FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    shell.addCommand(new AddCommand(collection, shell.getInput(), shell.getOutput()));
    shell.addCommand(new HelpCommand(shell.getOutput()));
    shell.addCommand(new ExitCommand());
    shell.addCommand(new UpdateCommand(collection, shell.getInput(), shell.getOutput()));
    shell.addCommand(new ClearCommand(collection, shell.getOutput()));
    shell.addCommand(new RemoveByIdCommand(collection, shell.getOutput()));
    shell.addCommand(new RemoveAtCommand(collection, shell.getOutput()));
    shell.addCommand(new RemoveLastCommand(collection, shell.getOutput()));
    shell.addCommand(new ShowCommand(collection, shell.getOutput()));
    shell.addCommand(new AddIfMaxCommand(collection, shell.getInput(), shell.getOutput()));
    shell.addCommand(new MinByPriceCommand(collection, shell.getOutput()));
    shell.addCommand(new PrintFieldDescendingTypeCommand(collection, shell.getOutput()));
    shell.addCommand(new CountGreaterThanEventCommand(collection, shell.getOutput()));
    shell.addCommand(
        new ReadEnvironmentFilepathCommand("TICKET_PATH", fd, shell.getOutput()), true);
    shell.addCommand(new LoadCommand(fd, collection, mapper, shell.getOutput()), true);
    shell.addCommand(new SaveCommand(collection, mapper, fd, shell.getOutput()));
    shell.addCommand(new ExecuteScriptCommand(shell.getOutput(), fd));
    shell.addCommand(new InfoCommand(collection, shell.getOutput()));
    shell.open();
  }
}
