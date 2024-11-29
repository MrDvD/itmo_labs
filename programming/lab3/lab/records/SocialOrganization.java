package lab.records;

public record SocialOrganization(String name) {
   @Override
   public String toString() {
      return name();
   }
}
