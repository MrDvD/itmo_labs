package lab.interfaces;

import java.util.List;

import lab.records.SocialStatus;

public interface ISociable {
   public void say(String text);
   public void addSocialStatus(SocialStatus obj);
   public List<SocialStatus> getSocialStatusList();
   public void popSocialStatus();
}
