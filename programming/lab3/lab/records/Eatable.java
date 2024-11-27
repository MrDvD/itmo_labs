package lab.records;

import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.IMeasurable;

public record Eatable(String name, byte saturation, float cost) implements ICapitalisticPassive, IMeasurable {
   public Eatable(String name, byte saturation) {
      this(name, saturation, ICapitalisticPassive.DEFAULT_COST);
   }
}