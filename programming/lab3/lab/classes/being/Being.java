package lab.classes.being;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lab.classes.container.Container;
import lab.classes.location.Location;
import lab.enums.Effect;
import lab.enums.Environment;
import lab.records.Eatable;
import lab.interfaces.IEnvironment;
import lab.interfaces.ILocatable;
import lab.interfaces.IMeasurable;

public abstract class Being implements ILocatable, IMeasurable {
   public final byte DEF_EATING_SPEED = 90;
   private final String name;
   private final String type;
   private byte hunger = 100;
   private Effect effect;
   private Location location;
   
   static class Log {
      static void println(String message) {
         System.out.println(message);
      }
      static void printf(String message, Object ... args) {
         System.out.printf(message, args);
      }
   }
   protected Being() {
      this("Безымянный", "Нечто");
   }
   protected Being(String name, String type) {
      this.name = name;
      this.type = type;
   }
   public void setEffect(Effect effect) {
      this.effect = effect;
   }
   public void eat(Eatable obj) {
      eat(obj, DEF_EATING_SPEED);
   }
   public void eat(Eatable obj, byte eatingSpeed) {
      if (hunger >= obj.saturation()) {
         hunger -= obj.saturation();
      } else {
         hunger = 0;
      }
      if (eatingSpeed > 175) {
         Log.printf("%s %s быстро упитал %s.\n", this.type, this.name, obj.name());
      } else if (eatingSpeed < 75) {
         Log.printf("%s %s медленно употребил %s.\n", this.type, this.name, obj.name());
      } else {
         Log.printf("%s %s съел %s.\n", this.type, this.name, obj.name());
      }
   }
   public void eatIterative(Eatable obj) {
      eat(obj);
   }
   public void eatIterative(Container obj) {
      eatIterative(obj, DEF_EATING_SPEED);
   }
   public void eatIterative(Eatable obj, byte eatingSpeed) {
      eat(obj, eatingSpeed);
   }
   public void eatIterative(IMeasurable obj, byte eatingSpeed) {
      Log.printf("%s %s чуть не начал есть %s.\n", this.type, this.name, obj);
   }
   public void eatIterative(Container obj, byte eatingSpeed) {
      Log.printf("%s %s рассматривает %s на наличие съестного.\n", this.type, this.name, obj);
      for (IMeasurable item : obj.getItemList()) {
         eatIterative(item, eatingSpeed);
      }
   }
   @Override
   public void setLocation(Location location) {
      // ...
   }
   @Override
   public Location getLocation() {
      return location;
   }
   @Override
   public boolean isReachable(Location obj) {
      // ...
   }
   public abstract Locomotion getLocomotion();
   public abstract class Locomotion implements IEnvironment {
      private List<Environment> currEnvironment, defaultEnvironment;
      public Locomotion(Environment ... arr) {
         currEnvironment = Arrays.asList(arr);
         defaultEnvironment = Arrays.asList(arr);
      }
      public void setDefaultEnvironment(Environment ... arr) {
         defaultEnvironment = Arrays.asList(arr);
      }
      public void resetEnvironment() {
         currEnvironment = new ArrayList<>(defaultEnvironment);
      }
      @Override
      public void setEnvironment(Environment ... arr) {
         currEnvironment = Arrays.asList(arr);
      }
      @Override
      public List<Environment> getEnvironment() {
         return currEnvironment;
      }
   } 
}