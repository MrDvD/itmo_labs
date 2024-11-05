package lab.classes.container;

import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.IMeasurable;

import java.util.List;
import java.util.ArrayList;

public abstract class Container implements ICapitalisticPassive, IMeasurable {
   private final String name;
   private List<IMeasurable> content = new ArrayList<>();

   Container() {
      this("Безымянный контейнер");
   }
   Container(String name) {
      this.name = name;
   }
   public void addItem(IMeasurable obj) {
      content.add(obj);
   }
}