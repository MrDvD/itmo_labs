package lab.classes.transport;

import lab.classes.container.Container;
import lab.classes.location.Location;

public class Rocket extends Transport {
   private LuggageSection luggage;
   public Rocket(Location location) {
      super("Ракета", location, 4, 200);
      luggage = new LuggageSection();
   }
   public LuggageSection getLuggage() {
      return luggage;
   }
   public class LuggageSection extends Container {
      public LuggageSection() {
         super("Багажный отсек", 700);
      }
   }
}
