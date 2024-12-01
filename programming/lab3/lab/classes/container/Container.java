package lab.classes.container;

import lab.classes.Log;
import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.IMeasurable;

import java.util.Set;
import java.util.HashSet;

public abstract class Container implements ICapitalisticPassive, IMeasurable {
   private final String name;
   private final double size;
   private final float cost;
   private double spaceLeft;
   private Set<IMeasurable> content = new HashSet<>();

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
      if (getItemSet().contains(obj)) {
         Log.Console.printf(Log.warnDecorate("В %s уже есть объект %s.\n"), this, obj);
      } else {
         try {
            if (obj.canFit(spaceLeft)) {
               String objState = this.toString();
               getItemSet().add(obj);
               spaceLeft -= obj.getSize();
               Log.Console.printf("В %s помещён объект %s.\n", objState, obj);
            } else {
               Log.Console.printf(Log.errDecorate("Объект %s слишком большой для %s.\n"), obj, this);
            }
         } catch (NullPointerException e) {
            Log.Console.printf(Log.errDecorate("Невозможно добавить пустой объект в %s.\n"), this);
         }
      }
   }
   public void delItem(IMeasurable obj) {
      if (getItemSet().contains(obj)) {
         String objState = this.toString();
         getItemSet().remove(obj);
         Log.Console.printf("Из %s убран объект %s.\n", objState, obj);
      } else {
         Log.Console.printf(Log.warnDecorate("В %s отсутствует объект %s.\n"), this, obj);
      }
   }
   public Set<IMeasurable> getItemSet() {
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
   public float cost() {
      float currCost = 0;
      for (IMeasurable item : getItemSet()) {
         if (item instanceof ICapitalisticPassive) {
            currCost += ((ICapitalisticPassive) item).cost();
         }
      }
      return cost + currCost;
   }
   @Override
   public String toString() {
      return name + getItemSet().toString();
   }
}