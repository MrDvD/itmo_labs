package lab.classes.being;

import lab.classes.container.Container;
import lab.enums.Effect;
import lab.records.Eatable;
import lab.interfaces.IMeasurable;
import lab.interfaces.IWeightable;

public abstract class Being implements IMeasurable, IWeightable {
   public final byte DEF_EATING_SPEED = 90;
   private final String name;
   private final String type;
   private byte hunger = 100;
   private Effect effect;
   Being() {
      this("Безымянный", "Нечто");
   }
   Being(String name, String type) {
      this.name = name;
      this.type = type;
   }
   public void setEffect(Effect effect) {
      this.effect = effect;
   }
   public void eat(Eatable obj) {
      eat(obj, DEF_EATING_SPEED);
   }
   public void eat(Eatable obj, byte eatingSpeed) {
      // ...
   }
   public void eatIterative(Container obj) {
      eatIterative(obj, DEF_EATING_SPEED);
   }
   public void eatIterative(Container obj, byte eatingSpeed) {
      // ...
   }
}