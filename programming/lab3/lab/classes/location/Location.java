package lab.classes.location;

import lab.classes.being.Being;

import java.util.List;
import java.util.ArrayList;

public abstract class Location {
   private final String name;
   private List<Being> visitorList = new ArrayList<>();
   private Location parent;

   protected Location(String name) {
      this.name = name;
   }
   public void setParent(Location obj) {
      parent = obj;
   }
   public Location getParent() {
      return parent;
   }
   public String getAddress() {
      if (parent != null) {
         return getParent().toString() + ", " + toString();
      } else {
         return toString();
      }
   }
   public void addVisitor(Being obj) {
      visitorList.add(obj);
   }
   public List<Being> getVisitorList() {
      return visitorList;
   }
   @Override
   public String toString() {
      return name;
   }
}
