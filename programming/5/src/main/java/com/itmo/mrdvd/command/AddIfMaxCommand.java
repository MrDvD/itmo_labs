package com.itmo.mrdvd.command;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.builder.InteractiveBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.object.Ticket;

public class AddIfMaxCommand<T> implements Command {
   protected final CollectionWorker<T,List<T>> collect;
   protected final InteractiveInputDevice in;
   protected final InteractiveBuilder<T> builder;
   protected final OutputDevice out;
   private final Comparator<T> comparator;

  public AddIfMaxCommand(CollectionWorker<T,List<T>> collection, InteractiveBuilder<T> builder, Comparator<T> comparator, InteractiveInputDevice in, OutputDevice out) {
   this.collect = collection;
   this.builder = builder;
   this.in = in;
   this.out = out; 
   this.comparator = comparator;
  }

  @Override
  public void execute() {
    collect.getCollection().sort(comparator);
   //  comparator.compare(collect.getCollection().get(collect.getCollection().size() - 1), o2) > 0;
    if (collect.getCollection().isEmpty() || collect.getCollection().get(collect.getCollection().size() - 1).getId().compareTo(ticket.getId()) < 0) {
      Optional<Ticket> result = collect.add(ticket);
      if (result.isPresent()) {
        out.writeln("[INFO] Билет успешно добавлен в коллекцию.");
      } else {
        out.writeln("[ERROR] Не удалось добавить билет в коллекцию.");
      }
    } else {
      out.writeln("[INFO] Билет не был добавлен в коллекцию: он не максимальный.");
    }
  }

  @Override
  public String name() {
    return "add_if_max";
  }

  @Override
  public String signature() {
    return name() + " {element}";
  }

  @Override
  public String description() {
    return "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
  }
}
