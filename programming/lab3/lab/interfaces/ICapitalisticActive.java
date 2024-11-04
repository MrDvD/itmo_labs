package lab.interfaces;

public interface ICapitalisticActive {
   float getBalance();
   String getDuty();
   void setDuty(String duty);
   void sell(ICapitalisticPassive obj);
}
