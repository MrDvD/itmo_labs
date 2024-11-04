package lab;

import lab.classes.being.*;
import lab.classes.solidsurface.*;
import lab.enums.*;

class Main {
   public static void main(String[] args) {
      LittleGuy piluylkin = new LittleGuy("Пилюлькин");
      piluylkin.setDuty("Доктор");
      LittleGuy ponchik = new LittleGuy("Пончик");
      ponchik.setEffect(Effect.SHOCKED);

      Table table = new Table();
      table.addItem();

      LittleGuy neznayka = new LittleGuy("Незнайка");
   }
}