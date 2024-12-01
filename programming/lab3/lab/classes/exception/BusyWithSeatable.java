package lab.classes.exception;

import lab.classes.being.Being;

public class BusyWithSeatable extends Exception {
   public BusyWithSeatable(Being obj) {
      super(String.format("%s не может переместиться с объектом %s.\n", obj, obj.getSeatHandler().getSeat()));
   }
}
