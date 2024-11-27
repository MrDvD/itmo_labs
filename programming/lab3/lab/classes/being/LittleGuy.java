package lab.classes.being;

import java.util.ArrayList;
import java.util.List;

import lab.enums.Environment;
import lab.interfaces.ICapitalisticActive;
import lab.interfaces.ICapitalisticPassive;
import lab.interfaces.ISociable;
import lab.records.SocialStatus;

public class LittleGuy extends Being implements ICapitalisticActive, ISociable {
   private List<SocialStatus> socialStatusList = new ArrayList<>();
   private float balance = 0.0f;
   private Legs locomotion;

   public LittleGuy(String name) {
      super(name, "Коротышка");
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
   private class Legs extends Locomotion {
      Legs() {
         super(Environment.GROUND);
      }
   }
}