package com.itmo.mrdvd.device;

import java.util.Optional;

import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
import com.itmo.mrdvd.device.input.IntInputDevice;
import com.itmo.mrdvd.device.input.LongInputDevice;

public class TicketConsole extends Console implements IntInputDevice, LongInputDevice, FloatInputDevice, EnumInputDevice {
   @Override
   public TicketConsole init() {
      return (TicketConsole) super.init();
   }
   
   @Override
   public Optional<Integer> readInt() {
      Optional<String> token = readToken();
      if (token.isEmpty()) {
         return Optional.empty();
      }
      try {
         return Optional.of(Integer.valueOf(token.get()));   
      } catch (NumberFormatException e) {
         return Optional.empty();
      }
   }

   @Override
   public Optional<Long> readLong() {
      Optional<String> token = readToken();
      if (token.isEmpty()) {
         return Optional.empty();
      }
      try {
         return Optional.of(Long.valueOf(token.get()));   
      } catch (NumberFormatException e) {
         return Optional.empty();
      }
   }

   @Override
   public Optional<Float> readFloat() {
      Optional<String> token = readToken();
      if (token.isEmpty()) {
         return Optional.empty();
      }
      try {
          return Optional.of(Float.valueOf(token.get()));
      } catch (NumberFormatException e) {
         return Optional.empty();
      }
   }

   @Override
   public <T extends Enum<T>> Optional<Enum<T>> readEnum(Class<T> cls) {
      Optional<String> token = readToken();
      if (token.isEmpty()) {
         return Optional.empty();
      }
      try {
          return Optional.of(Enum.valueOf(cls, token.get()));
      } catch (IllegalArgumentException e) {
         return Optional.empty();
      }
   }
}
