package lab.classes.transport;

import java.util.ArrayList;
import java.util.List;

import lab.classes.Log;
import lab.classes.being.Being;
import lab.classes.location.Location;
import lab.interfaces.ILocatable;

public abstract class Transport implements ILocatable {
   private String name;
   private int maxPassengerCount, currPassengerCount = 0;
   private double maxPassengerSize;
   private List<Being> passengers = new ArrayList<>();
   private Location location;
   
   protected Transport(String name, Location location, int maxPassengerCount, double maxPassengerSize) {
      this.name = name;
      this.location = location;
      this.maxPassengerCount = maxPassengerCount;
      this.maxPassengerSize = maxPassengerSize;
   }
   public int getMaxPassengerCount() {
      return maxPassengerCount;
   }
   public void addPassenger(Being obj) {
      if (obj.getLocation() == getLocation()) {
         if (currPassengerCount + 1 <= maxPassengerCount) {
            if (obj.canFit(maxPassengerSize)) {
               passengers.add(obj);
               currPassengerCount += 1;
               Log.Console.printf("%s сел в транспорт %s.\n", obj, this);
            } else {
               // throw an exception
            }
         } else {
            // throw an exception
         }
      } else {
         // throw an exception
      }
   }
   public void popPassenger() {
      if (passengers.size() > 0) {
         Log.Console.printf("%s вышел из транспорта %s.\n", passengers.getLast(), this);
         passengers.removeLast();
      } else {
         // throw an exception
      }
   }
   public List<Being> getPassengerList() {
      return passengers;
   }
   @Override
   public void setLocation(Location obj) {
      location = obj;
      for (Being b : getPassengerList()) {
         b.setLocation(obj, true);
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
}
