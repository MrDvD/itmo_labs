package lab.classes.being;

import lab.enums.Effect;

public abstract class Being {
   private final String name;
   private final String type;
   private byte hunger;
   private Effect effect;
   Being() {
      this("Безымянный", "Нечто");
   }
   Being(String name, String type) {
      this.name = name;
      this.type = type;
      this.hunger = 100;
   }
   public void setEffect(Effect effect) {
      this.effect = effect;
   }
}