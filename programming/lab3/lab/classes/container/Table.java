package lab.classes.container;

import java.util.ArrayList;
import java.util.List;

import lab.interfaces.ISeatable;

public class Table extends Container {
   private List<Chair> chairs = new ArrayList<>(); 
   public Table(int chairs) {
      super("Стол");
      for (int i = 0; i < chairs; i++) {
         chairs.add(new Chair());
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