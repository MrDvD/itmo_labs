package lab.classes.location;

import lab.classes.Log;
import lab.classes.being.Being;

import java.util.Set;
import java.util.HashSet;

public abstract class Location {
   private final String name;
   private Set<Being> visitorSet = new HashSet<>();
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
   public String getName() {
      return name;
   }
   public void addVisitor(Being obj) {
      if (getVisitorSet().contains(obj)) {
         Log.Console.printf(Log.warnDecorate("В локации %s уже есть посетитель %s.\n"), this, obj);
      } else {
         getVisitorSet().add(obj);
         Log.Console.printf("В локации %s новый посетитель: %s.\n", this, obj);
      }
   }
   public void delVisitor(Being obj) {
      if (getVisitorSet().contains(obj)) {
         getVisitorSet().remove(obj);
         Log.Console.printf("Из локации %s ушёл посетитель: %s.\n", this, obj);
      } else {
         Log.Console.printf(Log.warnDecorate("В локации %s нет посетителя %s.\n"), this, obj);
      }
   }
   public Set<Being> getVisitorSet() {
      return visitorSet;
   }
   @Override
   public String toString() {
      if (parent != null) {
         return getParent().toString() + ", " + getName();
      } else {
         return getName();
      }
   }
}
