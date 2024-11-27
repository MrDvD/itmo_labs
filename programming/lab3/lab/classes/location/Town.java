package lab.classes.location;

public class Town extends Location {
   public Town() {
      this("Безымянный");
   }
   public Town(String name) {
      super("Город " + name);
   }
}
