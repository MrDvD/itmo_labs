package lab.records;

import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.IMeasurable;

public record Eatable(String name, byte saturation, double size, float cost) implements ICapitalisticPassive, IMeasurable {
   public Eatable(String name, byte saturation, double size) {
      this(name, saturation, size, ICapitalisticPassive.DEFAULT_COST);
   }
   @Override
   public double getSize() {
      return size;
   }
   @Override
   public boolean canFit(double obj) {
      return obj > size;
   }
   @Override
   public String toString() {
      return name();
   }
}