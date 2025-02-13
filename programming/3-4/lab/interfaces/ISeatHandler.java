package lab.interfaces;

import lab.classes.being.Being;

public interface ISeatHandler {
   public void reserveSeat(Being being, IHavingSeat obj);
   public void reserveSeat(Being being, IReservingSeat obj);
   public void exitSeat(Being obj);
   public ISeatable getSeat();
}