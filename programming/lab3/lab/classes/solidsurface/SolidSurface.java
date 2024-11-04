package lab.classes.solidsurface;

import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.IMeasurable;

public abstract class SolidSurface implements ICapitalisticPassive {
   private final String name;

   SolidSurface() {
      this("Безымянная поверхность");
   }
   SolidSurface(String name) {
      this.name = name;
   }
   void addItem(IMeasurable obj) {
      // ...
   }
}