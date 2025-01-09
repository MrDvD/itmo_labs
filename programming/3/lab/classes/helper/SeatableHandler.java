package lab.classes.helper;

import lab.classes.Log;
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
      Log.Console.printf("%s присел на объект %s.\n", being, getSeat());
   }
   @Override
   public void reserveSeat(Being being, IHavingSeat obj) {
      currSeat = obj.getFreeSeat(being);
      Log.Console.printf("%s присел на объект %s.\n", being, getSeat());
   }
   @Override
   public ISeatable getSeat() {
      return currSeat;
   }
   @Override
   public void exitSeat(Being being) {
      if (getSeat() != null) {
         if (parent != null) {
            parent.notifyOnExit(being);
         }
         getSeat().setState(false);
         Log.Console.printf("%s встал с объекта %s.\n", being, getSeat());
         currSeat = null;
         parent = null;
      } else {
         Log.Console.printf(Log.warnDecorate("%s уже не сидит на чём-либо.\n"), being);
      }
   }
}
