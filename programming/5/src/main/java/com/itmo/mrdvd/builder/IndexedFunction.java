package com.itmo.mrdvd.builder;

import java.util.function.Function;

@FunctionalInterface
public interface IndexedFunction<V> extends Function<Integer,V> {
   public static <V> IndexedFunction<V> of(Function<Integer,V> method, int index) {
      return new IndexedFunction<V>() {
         @Override
         public int index() {
            return index;
         }

         @Override
         public V apply(Integer i) {
            return method.apply(i);
         }
      };
   }

   public default int index() {
      return -1;
   }
}