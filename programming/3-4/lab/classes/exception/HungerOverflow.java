package lab.classes.exception;

import lab.classes.being.Being;

public class HungerOverflow extends RuntimeException {
   public HungerOverflow(Being obj) {
      super(String.format("У сущности %s произошло переполнение шкалы голода.\n", obj));
   }
}
