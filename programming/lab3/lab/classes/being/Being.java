package lab.classes.being;

import lab.classes.Log;
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
      
      if ((hunger & 0xFF) >= (obj.saturation() & 0xFF)) {
         hunger -= obj.saturation();
      } else {
         hunger = 0;
      }
      if ((eatingSpeed & 0xFF) > 175) {
         Log.Console.printf("%s быстро уписал %s.\n", this, obj.name());
      } else if ((eatingSpeed & 0xFF) < 75) {
         Log.Console.printf("%s медленно употребил %s.\n", this, obj.name());
      } else {
         Log.Console.printf("%s съел %s.\n", this, obj.name());
      }
   }
   public void eatIterative(Container obj) {
      eatIterative(obj, DEF_EATING_SPEED);
   }
   public void eatIterative(Container obj, byte eatingSpeed) {
      Log.Console.printf("%s рассматривает %s на наличие съестного.\n", this, obj);
      // check if it's correct (from SOLID pov)
      for (IMeasurable item : obj.getItemList()) {
         if (item instanceof Eatable) {
            eat((Eatable) item, eatingSpeed);
         } else if (item instanceof Container) {
            eatIterative((Container) item, eatingSpeed);
         } else {
            Log.Console.printf("%s чуть не начал есть %s.\n", this, obj);
         }
      }
   }
   public void seat(ISeatable obj) {
      if (canFit(obj.getSize())) {
         seat = obj;
         seat.setState(true);
         Log.Console.printf("%s присел за/на %s.\n", this, seat);
      } else {
         // error
      }
   }
   public void getUp() {
      if (seat != null) {
         seat.setState(false);
         Log.Console.printf("%s встал с/из-за %s.\n", this, seat);
         seat = null;
      } else {
         Log.Console.printf("%s осознал, что уже стоит.\n", this);
      }
   }
   @Override
   public void setLocation(Location location) {
      if (this.location != null) {
         this.location.delVisitor(this);
      }
      this.location = location;
      this.location.addVisitor(this);
      Log.Console.printf("%s переместился в локацию %s.\n", this, location);
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
   @Override
   public int hashCode() {
      return name.hashCode() + type.hashCode() + (int) size;
   }
   @Override
   public boolean equals(Object obj) {
      if (obj == null || !(obj instanceof Being)) {
         return false;
      }
      Being other = (Being) obj;
      return this.name == other.name && this.type == other.type && this.size == other.size;
   }
}