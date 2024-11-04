package lab.classes.place;

import lab.classes.being.*;

import java.util.List;
import java.util.ArrayList;

public abstract class Place {
   private final String name;
   private List<Being> visitors;
   
   Place(String name) {
      this.name = name;
   }
}
