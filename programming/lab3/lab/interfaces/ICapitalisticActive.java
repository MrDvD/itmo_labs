package lab.interfaces;

import lab.records.Duty;

public interface ICapitalisticActive {
   public float getBalance();
   public void setBalance(float balance);
   public String getDuty();
   public void setDuty(Duty duty);
   public void sell(ICapitalisticPassive obj);
   public void buy(ICapitalisticPassive obj);
   public void work();
}
