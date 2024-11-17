package lab.classes.transport;

import java.util.Arrays;
import java.util.List;

import lab.classes.being.Being;
import lab.classes.location.Location;
import lab.enums.Environment;
import lab.interfaces.IEnvironment;
import lab.interfaces.ILocatable;

public abstract class Transport implements ILocatable, IEnvironment {
   private String name;
   private int maxPassengerCount, currPassengerCount = 0;
   private double maxPassengerSize;
   private List<Being> passengers;
   private List<Environment> transportTypeList;
   private Location location;
   
   public Transport(String name, Environment type, Location location, int maxPassengerCount, double maxPassengerSize) {
      this.name = name;
      this.transportTypeList.add(type);
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
               obj.getLocomotion().setEnvironment();
               currPassengerCount += 1;
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
         passengers.getLast().getLocomotion().resetEnvironment();
         passengers.removeLast();
      } else {
         // throw an exception
      }
   }
   public List<Being> getPassengerList() {
      return passengers;
   }
   @Override
   public boolean isReachable(Location obj) {
      if (getLocation() == obj) {
         return true;
      }
      if (getLocation().getParent() == obj || obj.getParent() == getLocation() || getLocation().getParent() == obj.getParent()) {
         // считается, что из L1 в L2 можно попасть напрямую, если у них общий предок
         for (Environment t1 : getEnvironment()) {
            for (Environment t2 : getLocation().getEnvironment()) {
               if (t1 == t2) {
                  return true;
               }
            }
         }
      }
      return false;
   }
   @Override
   public List<Environment> getEnvironment() {
      return transportTypeList;
   }
   @Override
   public void setEnvironment(Environment ... list) {
      transportTypeList = Arrays.asList(list);
   }
   @Override
   public void setLocation(Location obj) {
      if (isReachable(obj)) {
         location = obj;
         for (Being b : getPassengerList()) {
            b.setLocation(obj);
         }
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
