package lab.interfaces;

import lab.classes.exception.NegativeBalance;
import lab.records.Duty;

public interface ICapitalisticActive {
   public float getBalance();
   public void setBalance(float balance) throws NegativeBalance;
   public Duty getDuty();
   public void setDuty(Duty duty);
   public void sell(ICapitalisticPassive obj) throws NegativeBalance;
   public void buy(ICapitalisticPassive obj);
   public void work() throws NegativeBalance;
}
