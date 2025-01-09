package lab.interfaces;

import lab.classes.being.Being;

public interface IReservingSeat {
   public ISeatable reserveFreeSeat(Being obj);
   public void notifyOnExit(Being obj);
}
