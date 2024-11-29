package lab.classes.container;

import lab.classes.Log;
import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.IMeasurable;

import java.util.List;
import java.util.ArrayList;

public abstract class Container implements ICapitalisticPassive, IMeasurable {
   private final String name;
   private final double size;
   private final float cost;
   private double spaceLeft;
   private List<IMeasurable> content = new ArrayList<>();

   protected Container(String name, double size) {
      this(name, size, ICapitalisticPassive.DEFAULT_COST);
   }
   protected Container(String name, double size, float cost) {
      this.name = name;
      this.size = size;
      this.spaceLeft = size;
      this.cost = cost;
   }
   public void addItem(IMeasurable obj) {
      if (obj.canFit(spaceLeft)) {
         content.add(obj);
         spaceLeft -= obj.getSize();
         Log.Console.printf("В %s помещён объект %s.\n", this, obj);
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
   public float cost() { // а как реализовать продажу контейнера с содержимым?
      return cost;
   }
   @Override
   public String toString() {
      return name;
   }
}