package lab.classes.being;

import java.util.ArrayList;
import java.util.List;

import lab.classes.Log;
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
      socialStatusList.add(obj);
      Log.Console.printf("У %s появился новый социальный статус: %s.\n", this, obj);
   }
   @Override
   public void popSocialStatus() {
      Log.Console.printf("%s потерял социальный статус: %s.\n", this, socialStatusList.getLast());
      socialStatusList.removeLast();
   }
   @Override
   public List<SocialStatus> getSocialStatusList() {
      Log.Console.printf("Список социальных статусов сущности %s:\n", this);
      Log.Console.println(socialStatusList);
      return socialStatusList;
   }
   @Override
   public void setBalance(float value) {
      balance = value;
   }
   @Override
   public float getBalance() {
      return balance;
   }
   @Override
   public void sell(ICapitalisticPassive obj) {
      setBalance(getBalance() + obj.cost());
      Log.Console.printf("%s продал объект %s, получив %.2f у.е.\n", this, obj, obj.cost());
   }
   @Override
   public void buy(ICapitalisticPassive obj) {
      if (getBalance() >= obj.cost()) {
         setBalance(getBalance() - obj.cost());
         Log.Console.printf("%s купил объект %s, потратив %.2f у.е.\n", this, obj, obj.cost());
      } else {
         // throw an error?
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
   public void work() {
      workingDays++;
      Log.Console.printf("%s поработал %d-й день в качестве %s.\n", this, workingDays, getDuty().name());
      if (workingDays >= getDuty().days()) {
         workingDays = 0;
         setBalance(getBalance() + getDuty().wage());
         Log.Console.printf("%s получил свою зарплату в размере: %.2f у.е.\n", this, getDuty().wage());
      }
   }
   @Override
   public void say(String phrase) {
      // ??? зачем
   }
}