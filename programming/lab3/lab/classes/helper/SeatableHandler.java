package lab.classes.helper;

import lab.classes.being.Being;
import lab.interfaces.IHavingSeat;
import lab.interfaces.IReservingSeat;
import lab.interfaces.ISeatHandler;
import lab.interfaces.ISeatable;

public class SeatableHandler implements ISeatHandler {
   private IReservingSeat parent;
   private ISeatable currSeat;
   @Override
   public void reserveSeat(Being being, IReservingSeat obj) {
      currSeat = obj.reserveFreeSeat(being);
      parent = obj;
   }
   @Override
   public void reserveSeat(Being being, IHavingSeat obj) {
      currSeat = obj.getFreeSeat(being);
   }
   @Override
   public ISeatable getSeat() {
      return currSeat;
   }
   @Override
   public void exitSeat(Being being) {
      if (currSeat != null) {
         if (parent != null) {
            parent.notifyOnExit(being);
         }
         getSeat().setState(false);
      } else {
         // thrown exception or log
      }
   }
}
