package lab.classes.being;

import lab.classes.container.Container;
import lab.classes.location.Location;
import lab.enums.Effect;
import lab.records.Eatable;
import lab.interfaces.ILocatable;
import lab.interfaces.IMeasurable;
import lab.interfaces.ISeatable;

public abstract class Being implements ILocatable, IMeasurable {
   public final byte DEF_EATING_SPEED = 90;
   private final String name;
   private final String type;
   private final double size;
   private byte hunger = 100;
   private Effect effect;
   private Location location;
   private ISeatable seat;
   
   static class Log {
      static void println(String message) {
         System.out.println(message);
      }
      static void printf(String message, Object ... args) {
         System.out.printf(message, args);
      }
   }
   protected Being(String name, String type, double size) {
      this.name = name;
      this.type = type;
      this.size = size;
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
   public void seat(ISeatable obj) {
      if (canFit(obj.getSize())) {
         obj.setState(true);
         Log.printf("%s присел за/на %s", toString(), obj.toString());
      } else {
         // error
      }
   }
   public void getUp() {
      if (seat != null) {
         Log.printf("%s встал с/из-за %s", toString(), seat.toString());
      } else {
         Log.printf("%s осознал, что уже стоит", toString());
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
   public double getSize() {
      return size;
   }
   @Override
   public boolean canFit(double obj) {
      return obj > size;
   }
   @Override
   public String toString() {
      return type + ' ' + name;
   }
}