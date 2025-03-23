package com.itmo.mrdvd.shell;

import java.util.Optional;

import com.itmo.mrdvd.device.input.InputDevice;

public class BasicLinkedInput<U extends InputDevice> implements LinkedInput<U> {
   private Optional<BasicLinkedInput<U>> prev;
   private U input;

   public BasicLinkedInput(U input) {
      this(input, null);
   }
   
   public BasicLinkedInput(U input, BasicLinkedInput<U> prev) {
      this.input = input;
      this.prev = Optional.ofNullable(prev);
   }

   @Override
   public <K extends LinkedInput<U>> K update(U nextInput) {
      return (K) new BasicLinkedInput<>(nextInput, this);
   }

   @Override
   public Optional<BasicLinkedInput<U>> prev() {
      return this.prev;
   }

   @Override
   public U input() {
      return this.input;
   }
} 
