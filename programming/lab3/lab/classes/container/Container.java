package lab.classes.container;

import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.IMeasurable;

import java.util.List;
import java.util.ArrayList;

public abstract class Container implements ICapitalisticPassive, IMeasurable {
   private final String name;
   private final double size;
   private double spaceLeft;
   private List<IMeasurable> content = new ArrayList<>();

   public Container() {
      this("Безымянный контейнер", 10.0);
   }
   public Container(String name, double size) {
      this.name = name;
      this.size = size;
      this.spaceLeft = size;
   }
   public void addItem(IMeasurable obj) {
      if (obj.canFit(spaceLeft)) {
         content.add(obj);
         spaceLeft -= obj.getSize();
      } else {
         // place custom error here
      }
   }
   public void delItem(IMeasurable obj) {
      // ...
   }
   public void empty() {
      // ...
   }
   public List<IMeasurable> getItemList() {
      return content;
   }
   @Override
   public boolean canFit(double size) {
      return size > this.size;
   }
   @Override
   public double getSize() {
      return this.size;
   }
   @Override
   public String toString() {
      return name;
   }
}