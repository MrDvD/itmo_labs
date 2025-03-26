package com.itmo.mrdvd.builder.functionals;

@FunctionalInterface
public interface ExFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}