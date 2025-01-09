package lab.classes.exception;

import lab.classes.being.Being;

public class BusyWithSeatable extends Exception {
   Being being;
   public BusyWithSeatable(Being obj) {
      this.being = obj;
   }
   @Override
   public String getMessage() {
      return String.format("%s не может переместиться с объектом %s.\n", being, being.getSeatHandler().getSeat());
   }
}
