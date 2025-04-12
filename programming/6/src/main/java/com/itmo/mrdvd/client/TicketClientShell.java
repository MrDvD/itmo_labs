package com.itmo.mrdvd.client;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.command.ExitCommand;
import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.shell.TicketShell;

public class TicketClientShell extends TicketShell {
  public TicketClientShell(
      DataInputDevice in,
      OutputDevice out,
      Collection<Ticket, List<Ticket>> collection,
      DataFileDescriptor fd,
      Serializer<Collection<Ticket, List<Ticket>>> serial,
      Deserializer<Collection<Ticket, List<Ticket>>> deserial) {
    this(
        in,
        out,
        collection,
        fd,
        serial,
        deserial,
        new HashSet<>(),
        new HashMap<>(),
        new ArrayList<>());
  }

  public TicketClientShell(
      DataInputDevice in,
      OutputDevice out,
      Collection<Ticket, List<Ticket>> collection,
      DataFileDescriptor fd,
      Serializer<Collection<Ticket, List<Ticket>>> serial,
      Deserializer<Collection<Ticket, List<Ticket>>> deserial,
      Set<Path> usedPaths,
      Map<String, Command> commands,
      List<Command> preExecute) {
    super(in, out, collection, fd, serial, deserial, usedPaths, commands, preExecute);
    addCommand(new ExitCommand());
  }
}
