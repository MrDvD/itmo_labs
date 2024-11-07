package lab.interfaces;

public interface ICapitalisticActive {
   float getBalance();
   void setBalance(float balance);
   String getDuty();
   void setDuty(String duty);
   void sell(ICapitalisticPassive obj);
}
