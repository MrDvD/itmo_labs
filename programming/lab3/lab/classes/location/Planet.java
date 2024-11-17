package lab.classes.location;

public class Planet extends Location {
   public Planet() {
      this("Безымянная");
   }
   public Planet(String name) {
      super("Планета " + name);
   }
}
