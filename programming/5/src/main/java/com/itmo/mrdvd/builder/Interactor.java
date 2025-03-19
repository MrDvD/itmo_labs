package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface Interactor<T> {
   public String attributeName();

   public Supplier<Optional<T>> inMethod();

   public Optional<String> comment();

   public Optional<List<String>> options();

   public String error();
}
