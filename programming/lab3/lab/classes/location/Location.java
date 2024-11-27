package lab.classes.location;

import lab.classes.being.Being;
import lab.enums.Environment;
import lab.interfaces.IEnvironment;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Location implements IEnvironment {
   private final String name;
   private List<Being> visitorList = new ArrayList<>();
   private List<Environment> environment = new ArrayList<>();
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
   public void setEnvironment(Environment ... arr) {
      environment = Arrays.asList(arr);
   }
   @Override
   public List<Environment> getEnvironment() {
      return environment;
   }
   @Override
   public String toString() {
      return name;
   }
}
