package lab.classes.container;

import java.util.ArrayList;
import java.util.List;

import lab.classes.being.Being;
import lab.classes.exception.AlreadyInitialized;
import lab.interfaces.IHavingSeat;
import lab.interfaces.ISeatable;

public class Table extends Container implements IHavingSeat {
   private List<Chair> chairs = new ArrayList<>(); 
   public Table(double size) {
      super("Стол", size);
   }
   public Table(double size, float cost) {
      super("Стол", size, cost);
   }
   public void initChairs(int num, double size) throws AlreadyInitialized {
      if (chairs.size() > 0) {
         throw new AlreadyInitialized("Массив стульев");
      } else {
         for (int i = 0; i < num; i++) {
            chairs.add(new Chair(size));
         }
      }
   }
   @Override
   public Chair getFreeSeat(Being obj) {
      for (int i = 0; i < chairs.size(); i++) {
         Chair curr = chairs.get(i);
         if (!curr.inUse() && obj.canFit(curr.getSize())) {
            curr.setState(true);
            return curr;
         }
      }
      return null;
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
      @Override
      public String toString() {
         return "Стул";
      }
   }
}