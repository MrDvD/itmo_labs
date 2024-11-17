package lab.classes.being;

import java.util.ArrayList;
import java.util.List;

import lab.enums.Environment;
import lab.interfaces.ICapitalisticActive;
import lab.interfaces.ISociable;
import lab.records.SocialStatus;

public class LittleGuy extends Being implements ICapitalisticActive, ISociable {
   private List<SocialStatus> socialStatusList = new ArrayList<>();
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
   private class Legs extends Locomotion {
      Legs() {
         super(Environment.GROUND);
      }
   }
}