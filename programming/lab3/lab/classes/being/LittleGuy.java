package lab.classes.being;

import java.util.ArrayList;
import java.util.List;

import lab.enums.Environment;
import lab.interfaces.ICapitalisticActive;
import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.ISociable;
import lab.records.Duty;
import lab.records.SocialStatus;

public class LittleGuy extends Being implements ICapitalisticActive, ISociable {
   private List<SocialStatus> socialStatusList = new ArrayList<>();
   private float balance = 0.0f;
   private Legs locomotion;
   private Duty duty;
   private int workingDays = 0;

   public LittleGuy(String name, double size) {
      super(name, "Коротышка", size);
      duty = new Duty("Безработный", 0, 0);
      locomotion = new Legs();
   }
   @Override
   public Locomotion getLocomotion() {
      return locomotion;
   }
   @Override
   public void addSocialStatus(SocialStatus obj) {
      socialStatusList.add(obj);
   }
   @Override
   public void popSocialStatus() {
      socialStatusList.removeLast();
   }
   @Override
   public List<SocialStatus> getSocialStatusList() {
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
   }
   @Override
   public void buy(ICapitalisticPassive obj) {
      if (getBalance() >= obj.cost()) {
         setBalance(getBalance() - obj.cost());
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
   }
   @Override
   public void work() {
      workingDays++;
      if (getDuty().days() >= workingDays) {
         workingDays = 0;
         setBalance(getBalance() + getDuty().wage());
      }
   }
   @Override
   public void say(String phrase) {
      // ??? зачем
   }
   private class Legs extends Locomotion {
      Legs() {
         super(Environment.GROUND);
      }
   }
}