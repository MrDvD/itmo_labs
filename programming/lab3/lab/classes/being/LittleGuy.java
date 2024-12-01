package lab.classes.being;

import java.util.ArrayList;
import java.util.List;

import lab.classes.Log;
import lab.classes.exception.NegativeBalance;
import lab.enums.Effect;
import lab.interfaces.ICapitalisticActive;
import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.ISociable;
import lab.records.Duty;
import lab.records.SocialStatus;

public class LittleGuy extends Being implements ICapitalisticActive, ISociable {
   private List<SocialStatus> socialStatusList = new ArrayList<>();
   private float balance = 0.0f;
   private Duty duty;
   private int workingDays = 0;

   public LittleGuy(String name, double size) {
      super(name, "Коротышка", size);
      duty = new Duty("Безработный", 0, 0);
   }
   @Override
   public void addSocialStatus(SocialStatus obj) {
      getSocialStatusList().add(obj);
      Log.Console.printf("У %s появился новый социальный статус: %s.\n", this, obj);
   }
   @Override
   public void popSocialStatus() {
      Log.Console.printf("%s потерял социальный статус: %s.\n", this, socialStatusList.getLast());
      getSocialStatusList().removeLast();
   }
   @Override
   public List<SocialStatus> getSocialStatusList() {
      return socialStatusList;
   }
   @Override
   public void setBalance(float value) throws NegativeBalance {
      if (value >= 0) {
         balance = value;
      } else {
         throw new NegativeBalance(this);
      }
   }
   @Override
   public float getBalance() {
      return balance;
   }
   @Override
   public void sell(ICapitalisticPassive obj) throws NegativeBalance {
      setBalance(getBalance() + obj.cost());
      Log.Console.printf("%s продал объект %s, получив %.2f у.е.\n", this, obj, obj.cost());
   }
   @Override
   public void buy(ICapitalisticPassive obj) {
      try {
         setBalance(getBalance() - obj.cost());
         Log.Console.printf("%s купил объект %s, потратив %.2f у.е.\n", this, obj, obj.cost());
      } catch (NegativeBalance e) {
         Log.Console.printf(Log.errDecorate("У %s недостаточно средств для покупки %s.\n"), this, obj);
      }
   }
   @Override
   public Duty getDuty() {
      return duty;
   }
   @Override
   public void setDuty(Duty duty) {
      this.duty = duty;
      Log.Console.printf("У %s появилась новая работа: %s.\n", this, duty.name());
   }
   @Override
   public void work() throws NegativeBalance {
      // effect
      if (getEffect() != Effect.UNCONSCIOUS) {
         workingDays++;
         Log.Console.printf("%s поработал %d-й день в качестве %s.\n", this, workingDays, getDuty().name());
         if (workingDays >= getDuty().days()) {
            workingDays = 0;
            setBalance(getBalance() + getDuty().wage());
            Log.Console.printf("%s получил свою зарплату в размере: %.2f у.е.\n", this, getDuty().wage());
         }
         // hunger
         setHunger((byte) (getHunger() + 50 < 256 ? getHunger() + 50: 255));
      } else {
         Log.Console.printf(Log.errDecorate("Состояние %s блокирует возможность работы для %s.\n"), this.getEffect(), this);
      }
   }
   @Override
   public void say(String phrase) {
      // ??? зачем
   }
}