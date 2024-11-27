package lab.interfaces;

import lab.classes.location.Location;

public interface ILocatable {
   public Location getLocation();
   // public boolean isReachable(Location obj);
   public void setLocation(Location location);
}