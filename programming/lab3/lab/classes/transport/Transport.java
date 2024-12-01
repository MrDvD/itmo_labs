package lab.classes.transport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lab.classes.Log;
import lab.classes.being.Being;
import lab.classes.location.Location;
import lab.interfaces.ILocatable;
import lab.interfaces.IReservingSeat;
import lab.interfaces.ISeatable;

public abstract class Transport implements ILocatable, IReservingSeat {
   private String name;
   private Location location;
   private List<Seat> seatList = new ArrayList<>();
   private Set<Being> passengerSet = new HashSet<>();
   
   protected Transport(String name, Location location) {
      this.name = name;
      this.location = location;
   }
   public abstract void initSeats(int seatsCount, double size);
   public Set<Being> getPassengerSet() {
      return passengerSet;
   }
   public List<Seat> getSeatList() {
      return seatList;
   }
   @Override
   public void notifyOnExit(Being obj) {
      getPassengerSet().remove(obj);
   }
   @Override
   public Seat reserveFreeSeat(Being obj) {
      for (Seat seat : getSeatList()) {
         if (!seat.inUse() && obj.canFit(seat.getSize())) {
            getPassengerSet().add(obj);
            return seat;
         }
      }
      return null;
   }
   @Override
   public void setLocation(Location obj) {
      location = obj;
      Log.Console.printf("Транспорт %s прибыл в локацию %s.\n", this, obj);
      for (Being b : getPassengerSet()) {
         b.setLocation(obj);
      }
   }
   @Override
   public Location getLocation() {
      return location;
   }
   @Override
   public String toString() {
      return name;
   }
   public abstract class Seat implements ISeatable {
      private double size;
      private boolean inUse = false;
      private String name;
      protected Seat(String name, double size) {
         this.name = name;
         this.size = size;
      }
      @Override
      public double getSize() {
         return size;
      }
      @Override
      public boolean inUse() {
         return inUse;
      }
      @Override
      public void setState(boolean state) {
         inUse = state;
      }
      @Override
      public String toString() {
         return name;
      }
   }
}
