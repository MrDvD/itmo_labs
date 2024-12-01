package lab.classes.being;

import java.util.HashSet;
import java.util.Set;

import lab.classes.Log;
import lab.classes.container.Container;
import lab.classes.exception.BusyWithSeatable;
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
   private ISeatHandler seat;

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
      // effects
      updateHungerEffect(hunger);
   }
   protected void addHunger(byte hunger) {
      if ((hunger & 0xFF) + getHunger() > 255) {
         setHunger((byte) 255);
      } else {
         setHunger((byte) (hunger + getHunger()));
      }
   }
   protected void subHunger(byte hunger) {
      if (getHunger() - (hunger & 0xFF) < 0) {
         setHunger((byte) 0);
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
   public void updateHungerEffect(byte hunger) {
      if ((hunger & 0xFF) < 30) {
         if (getEffect() != Effect.NORMAL) {
            setEffect(Effect.NORMAL);
         }
      } else if ((hunger & 0xFF) > 220) {
         if (getEffect() != Effect.UNCONSCIOUS) {
            setEffect(Effect.UNCONSCIOUS);
         }
      } else {
         if (getEffect() == Effect.UNCONSCIOUS) {
            setEffect(Effect.NORMAL);
         }
      }
   }
   public void eat(Eatable obj) {
      eat(obj, DEF_EATING_SPEED);
   }
   public void eat(Eatable obj, byte eatingSpeed) {
      if (getHunger() == 0) {
         // throw an error
      } else {
         subHunger(obj.saturation());
         // logs
         if ((eatingSpeed & 0xFF) > 175) {
            Log.Console.printf("%s быстро упитал %s.\n", this, obj.name());
         } else if ((eatingSpeed & 0xFF) < 75) {
            Log.Console.printf("%s медленно употребил %s.\n", this, obj.name());
         } else {
            Log.Console.printf("%s съел %s.\n", this, obj.name());
         }
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
            eat((Eatable) item, eatingSpeed);
            toRemove.add(item);
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
   public void setSeatHandler(ISeatHandler obj) {
      seat = obj;
   }
   public void seat(IHavingSeat obj) {
      setSeatHandler(new SeatableHandler());
      getSeatHandler().reserveSeat(this, obj);
      Log.Console.printf("%s присел на объект %s.\n", this, getSeatHandler().getSeat());
   }
   public void seat(IReservingSeat obj) {
      setSeatHandler(new SeatableHandler());
      getSeatHandler().reserveSeat(this, obj);
      Log.Console.printf("%s присел на объект %s.\n", this, getSeatHandler().getSeat());
   }
   public void getUp() {
      if (getSeatHandler() != null) {
         getSeatHandler().exitSeat(this);
         Log.Console.printf("%s встал с объекта %s.\n", this, getSeatHandler().getSeat());
         setSeatHandler(null);
      } else {
         Log.Console.printf(Log.warnDecorate("%s осознал, что уже стоит.\n"), this);
      }
   }
   public void sleep() {
      addHunger((byte) 35);
      Log.Console.printf("%s немного отдохнул.\n", this);
   }
   public void goTo(Location location) throws BusyWithSeatable {
      if (getSeatHandler() != null) {
         throw new BusyWithSeatable(this);
      }
      setLocation(location);
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