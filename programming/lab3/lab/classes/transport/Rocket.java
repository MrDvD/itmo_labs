package lab.classes.transport;

import lab.classes.container.Container;
import lab.classes.location.Location;

public class Rocket extends Transport {
   private LuggageSection luggage;
   public Rocket(Location location, int seatsCount, double size) {
      super("Ракета", location);
      luggage = new LuggageSection();
      initSeats(seatsCount, size);
   }
   @Override
   public void initSeats(int seatsCount, double size) {
      if (getSeatList().size() > 0) {
         // error (already initialized)
      } else {
         for (int i = 0; i < seatsCount; i++) {
            getSeatList().add(new PassengerSeat(size));
         }
      }
   }
   public LuggageSection getLuggage() {
      return luggage;
   }
   public class LuggageSection extends Container {
      public LuggageSection() {
         super("БагажныйОтсек", 700);
      }
   }
   public class PassengerSeat extends Seat {
      public PassengerSeat(double size) {
         super("ПассажирскоеСиденье", size);
      }
   }
}
