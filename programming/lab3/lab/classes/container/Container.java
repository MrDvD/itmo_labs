package lab.classes.container;

import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.IMeasurable;
import lab.interfaces.IWeightable;

import java.util.List;
import java.util.ArrayList;

public abstract class Container implements ICapitalisticPassive, IMeasurable {
   private final String name;
   private List<IMeasurable> content = new ArrayList<>();
   private List<IWeightable> users = new ArrayList<>();

   Container() {
      this("Безымянный контейнер");
   }
   Container(String name) {
      this.name = name;
   }
   public void addItem(IMeasurable obj) {
      content.add(obj);
   }
   public void addUser(IWeightable obj) {
      users.add(obj);
   }
}