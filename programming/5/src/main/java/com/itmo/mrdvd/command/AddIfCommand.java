package com.itmo.mrdvd.command;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.builder.InteractiveBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.device.OutputDevice;

public class AddIfCommand<T extends HavingId> implements Command {
   protected final CollectionWorker<T,List<T>> collect;
   protected final InteractiveBuilder<T> builder;
   protected final OutputDevice out;
   protected final Comparator<T> comparator;
   protected final Set<Integer> values;

  public AddIfCommand(CollectionWorker<T,List<T>> collection, InteractiveBuilder<T> builder, Comparator<T> comparator, Set<Integer> values, OutputDevice out) {
   this.collect = collection;
   this.builder = builder;
   this.out = out; 
   this.comparator = comparator;
   this.values = values;
  }

  @Override
  public void execute() {
   Optional<T> result = collect.add(builder, comparator, values);
   if (result.isPresent()) {
      out.writeln("[INFO] Билет успешно добавлен в коллекцию.");
   } else {
      out.writeln("[INFO] Билет не добавлен в коллекцию: он не удовлетворяет условию.");
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
