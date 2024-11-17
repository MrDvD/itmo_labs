package lab.interfaces;

import lab.records.Duty;

public interface ICapitalisticActive {
   float getBalance();
   void setBalance(float balance);
   String getDuty();
   void setDuty(Duty duty);
   void sell(ICapitalisticPassive obj);
}
