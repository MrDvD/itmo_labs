package lab.records;

public record SocialStatus(String name, SocialOrganization organization) {
   @Override
   public String toString() {
      return name + " " + organization.toString();
   }
}
