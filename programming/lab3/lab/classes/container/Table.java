package lab.classes.container;

import java.util.ArrayList;
import java.util.List;

import lab.interfaces.ISeatable;

public class Table extends Container {
   private List<Chair> chairs = new ArrayList<>(); 
   public Table(double size) {
      super("Стол", size);
   }
   public Table(double size, float cost) {
      super("Стол", size, cost);
   }
   public void initChairs(int num, double size) {
      if (chairs.size() == 0) {
         for (int i = 0; i < num; i++) {
            chairs.add(new Chair(size));
         }
      } else {
         // show exception (already initialized)
      }
   }
   public Chair getFreeChair() {
      for (int i = 0; i < chairs.size(); i++) {
         Chair curr = chairs.get(i);
         if (!curr.inUse()) {
            curr.setState(true);
            return curr;
         }
      }
      return null;
      // выкидывает ошибку здесь или выше??? когда мест свободных нет
   }
   public class Chair implements ISeatable {
      private double size;
      private boolean inUse = false;
      public Chair(double size) {
         this.size = size;
      }
      @Override
      public double getSize() {
         return size;
      }
      @Override
      public boolean inUse() {
         return inUse;
      }
      @Override
      public void setState(boolean state) {
         inUse = state;
      }
   }
}