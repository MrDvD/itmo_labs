package lab.classes.being;

import lab.classes.container.Container;
import lab.enums.Effect;
import lab.records.Eatable;
import lab.interfaces.IMeasurable;

public abstract class Being implements IMeasurable {
   public final byte DEF_EATING_SPEED = 90;
   private final String name;
   private final String type;
   private byte hunger = 100;
   private Effect effect;
   
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
         Log.printf("&s %s быстро упитал %s.\n", this.type, this.name, obj.name());
      } else if (eatingSpeed < 75) {
         Log.printf("&s %s медленно употребил %s.\n", this.type, this.name, obj.name());
      } else {
         Log.printf("&s %s съел %s.\n", this.type, this.name, obj.name());
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
   public void eatIterative(Container obj, byte eatingSpeed) {
      for (IMeasurable item : obj.getItemList()) {
         try {
            // реализовать паттерн Visitor
            eatIterative(item, eatingSpeed);
         } catch (Exception e) {
            continue;
         }
      }
   }
}