package lab.classes.being;

import java.util.HashSet;
import java.util.Set;

import lab.classes.Log;
import lab.classes.container.Container;
import lab.classes.exception.BusyWithSeatable;
import lab.classes.exception.HungerOverflow;
import lab.classes.helper.SeatableHandler;
import lab.classes.location.Location;
import lab.enums.Effect;
import lab.records.Eatable;
import lab.interfaces.IHavingSeat;
import lab.interfaces.ILocatable;
import lab.interfaces.IMeasurable;
import lab.interfaces.IReservingSeat;
import lab.interfaces.ISeatHandler;

public abstract class Being implements ILocatable, IMeasurable {
   public final byte DEF_EATING_SPEED = 90;
   private final String name;
   private final String type;
   private final double size;
   private byte hunger = 100;
   private Effect effect = Effect.NORMAL;
   private Location location;
   private ISeatHandler seat = new SeatableHandler();

   protected Being(String name, String type, double size) {
      this.name = name;
      this.type = type;
      this.size = size;
   }
   protected byte getHunger() {
      return hunger;
   }
   protected void setHunger(byte hunger) {
      this.hunger = hunger;
   }
   protected void addHunger(byte hunger) {
      if ((hunger & 0xFF) + (getHunger() & 0xFF) > 255) {
         throw new HungerOverflow(this);
      } else {
         setHunger((byte) (hunger + getHunger()));
      }
   }
   protected void subHunger(byte hunger) {
      if ((getHunger() & 0xFF) < (hunger & 0xFF)) {
         throw new HungerOverflow(this);
      } else {
         setHunger((byte) (getHunger() - hunger));
      }
   }
   public void setEffect(Effect effect) {
      this.effect = effect;
      Log.Console.printf("%s обновил своё состояние: %s.\n", this, effect);
   }
   public Effect getEffect() {
      return effect;
   }
   public void updateHungerEffect() {
      if ((getHunger() & 0xFF) < 30) {
         if (getEffect() != Effect.NORMAL) {
            setEffect(Effect.NORMAL);
         }
      } else if ((getHunger() & 0xFF) > 220) {
         if (getEffect() != Effect.UNCONSCIOUS) {
            setEffect(Effect.UNCONSCIOUS);
         }
      } else {
         if (getEffect() == Effect.UNCONSCIOUS) {
            setEffect(Effect.NORMAL);
         }
      }
   }
   public boolean eat(Eatable obj) {
      return eat(obj, DEF_EATING_SPEED);
   }
   public boolean eat(Eatable obj, byte eatingSpeed) {
      try {
         subHunger(obj.saturation());
         // logs
         if ((eatingSpeed & 0xFF) > 175) {
            Log.Console.printf("%s быстро упитал %s.\n", this, obj.name());
         } else if ((eatingSpeed & 0xFF) < 75) {
            Log.Console.printf("%s медленно употребил %s.\n", this, obj.name());
         } else {
            Log.Console.printf("%s съел %s.\n", this, obj.name());
         }
         updateHungerEffect();
         return true;
      } catch (HungerOverflow | NullPointerException e) {
         if (e instanceof HungerOverflow) {
            Log.Console.printf(Log.errDecorate("Шкала насыщения сущности %s переполнена, невозможно съесть %s.\n"), this, obj);
         } else if (e instanceof NullPointerException) {
            Log.Console.println(Log.errDecorate("Невозможно съесть пустую еду."));
         }
         return false;
      }
   }
   public void eatIterative(Container obj) {
      eatIterative(obj, DEF_EATING_SPEED);
   }
   public void eatIterative(Container obj, byte eatingSpeed) {
      Log.Console.printf("%s рассматривает %s на наличие съестного.\n", this, obj);
      Set<IMeasurable> toRemove = new HashSet<>();
      // check if it's correct (from SOLID pov)
      for (IMeasurable item : obj.getItemSet()) {
         if (item instanceof Eatable) {
            if (eat((Eatable) item, eatingSpeed)) {
               toRemove.add(item);
            } else {
               break;
            }
         } else if (item instanceof Container) {
            eatIterative((Container) item, eatingSpeed);
         } else {
            Log.Console.printf("%s чуть не начал есть %s.\n", this, obj);
         }
      }
      for (var i : toRemove) {
         obj.delItem(i);
      }
   }
   public ISeatHandler getSeatHandler() {
      return seat;
   }
   public void seat(IHavingSeat obj) {
      getSeatHandler().reserveSeat(this, obj);
   }
   public void seat(IReservingSeat obj) {
      getSeatHandler().reserveSeat(this, obj);
   }
   public void getUp() {
      getSeatHandler().exitSeat(this);
   }
   public void sleep() {
      try {
         addHunger((byte) 35);
      } catch (HungerOverflow e) {
         setHunger((byte) 255);
      }
      Log.Console.printf("%s немного отдохнул.\n", this);
   }
   public void goTo(Location location) {
      if (getEffect() != Effect.UNCONSCIOUS) {
         try {
            if (getSeatHandler().getSeat() != null) {
               throw new BusyWithSeatable(this);
            }
         } catch (BusyWithSeatable e) {
            Log.Console.println(e.getMessage());
         }
         try {
            addHunger((byte) 30);
            setLocation(location);
         } catch (HungerOverflow e) {
            Log.Console.printf(Log.errDecorate("Уровень голода блокирует возможность передвижения для %s.\n"), this);
         }
      } else {
         Log.Console.printf(Log.errDecorate("Состояние %s блокирует возможность передвижения для %s.\n"), this.getEffect(), this);
      }
   }
   @Override
   public void setLocation(Location location) {
      if (getLocation() != null) {
         this.location.delVisitor(this);
      }
      this.location = location;
      this.location.addVisitor(this);
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