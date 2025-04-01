package com.itmo.mrdvd.builder.validators;

import com.itmo.mrdvd.builder.ProcessStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectValidator<T> implements Validator<T> {
  protected List<Function<T, ?>> getters;
  protected List<Predicate> methods;
  protected List<Validator> validators;
  protected T object;

  public ObjectValidator() {
    this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public ObjectValidator(
      List<Function<T, ?>> getters, List<Predicate> methods, List<Validator> validators) {
    this.getters = getters;
    this.methods = methods;
    this.validators = validators;
  }

  @Override
  public <U> ObjectValidator<T> check(
      Function<T, U> getter, Class<U> valueCls, Predicate<U> validator)
      throws IllegalArgumentException {
    if (getter == null) {
      throw new IllegalArgumentException("Getter не может быть null.");
    }
    this.getters.add(getter);
    this.validators.add(null);
    this.methods.add(validator);
    return this;
  }

  @Override
  public <U> Validator<T> check(Function<T, U> getter, Validator<U> validator)
      throws IllegalArgumentException {
    if (getter == null) {
      throw new IllegalArgumentException("Getter не может быть null.");
    }
    if (validator == null) {
      throw new IllegalArgumentException("Validator не может быть null.");
    }
    this.getters.add(getter);
    this.methods.add(null);
    this.validators.add(validator);
    return this;
  }

  protected ProcessStatus processValidator(int index) {
    return validators.get(index).validate(getters.get(index).apply(object))
        ? ProcessStatus.SUCCESS
        : ProcessStatus.FAILURE;
  }

  protected ProcessStatus processCheck(int index) {
    return methods.get(index).test(getters.get(index).apply(object))
        ? ProcessStatus.SUCCESS
        : ProcessStatus.FAILURE;
  }

  protected boolean checkObject() {
    for (int i = 0; i < getters.size(); i++) {
      if (validators.get(i) != null) {
        if (processValidator(i).equals(ProcessStatus.FAILURE)) {
          return false;
        }
      } else if (processCheck(i).equals(ProcessStatus.FAILURE)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean validate(T obj) {
    if (obj == null) {
      throw new IllegalArgumentException("Объект не может быть null.");
    }
    this.object = obj;
    return checkObject();
  }
}
