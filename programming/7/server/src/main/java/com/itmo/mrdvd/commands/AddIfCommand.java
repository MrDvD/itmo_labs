package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.validators.Validator;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class AddIfCommand<T extends HavingId> implements Command<Void> {
  protected final CollectionWorker<T, List<T>> collect;
  protected final Validator<T> validator;
  protected final Comparator<T> comparator;
  protected final Set<Integer> values;
  protected final Class<T> clz;

  public AddIfCommand(
      CollectionWorker<T, List<T>> collection,
      Validator<T> validator,
      Comparator<T> comparator,
      Class<T> clz,
      Set<Integer> values) {
    this.collect = collection;
    this.validator = validator;
    this.clz = clz;
    this.comparator = comparator;
    this.values = values;
  }

  @Override
  public Void execute(List<Object> params) throws IllegalArgumentException, RuntimeException {
    if (this.collect == null) {
      throw new IllegalStateException("Не задана коллекция для работы.");
    }
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Не предоставлен объект для добавления в коллекцию.");
    }
    try {
      T obj = this.clz.cast(params.get(0));
      if (collect.add(obj, this.validator, this.comparator, this.values).isEmpty()) {
        throw new RuntimeException("Не удалось добавить элемент в коллекцию.");
      }
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать переданный объект.");
    }
    return null;
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
