package lab.interfaces;

import lab.classes.place.Place;

public interface ILocatable {
   Place getLocation();
   void setLocation(Place location);
}