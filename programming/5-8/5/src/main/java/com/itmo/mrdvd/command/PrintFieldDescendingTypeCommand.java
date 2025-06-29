package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.object.HavingId;
import com.itmo.mrdvd.shell.Shell;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PrintFieldDescendingTypeCommand<T extends HavingId> implements Command {
  private final Collection<T, List<T>> collection;
  private final Shell<?, ?> shell;
  private final Comparator<T> comparator;

  public PrintFieldDescendingTypeCommand(Collection<T, List<T>> collect, Comparator<T> comparator) {
    this(collect, comparator, null);
  }

  public PrintFieldDescendingTypeCommand(
      Collection<T, List<T>> collect, Comparator<T> comparator, Shell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
    this.comparator = comparator;
  }

  @Override
  public PrintFieldDescendingTypeCommand<T> setShell(Shell<?, ?> shell) {
    return new PrintFieldDescendingTypeCommand<>(collection, comparator, shell);
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() {
    collection.getCollection().sort(comparator);
    if (collection.getCollection().isEmpty()) {
      getShell().get().getOut().writeln("[INFO] Коллекция пуста.");
    } else {
      for (T obj : collection) {
        getShell().get().getOut().writeln(obj.toString());
      }
    }
  }

  @Override
  public String name() {
    return "print_field_descending_type";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести значения поля type всех элементов в порядке убывания";
  }
}
