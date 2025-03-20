package com.itmo.mrdvd.builder.validators;

import com.itmo.mrdvd.object.EventType;

public class EventValidator {
   public static boolean validateName(String name) {
     return name != null && !name.isBlank();
   }

   public static boolean validateDescription(String description) {
     return description != null && !description.isBlank() && description.length() <= 1190;
   }

   public static boolean validateType(EventType type) {
     return type != null;
   }
 }