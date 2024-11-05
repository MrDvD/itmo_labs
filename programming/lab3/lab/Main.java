package lab;

import lab.classes.being.*;
import lab.classes.container.*;
import lab.enums.*;
import lab.records.Eatable;

class Main {
   public static void main(String[] args) {
      LittleGuy piluylkin = new LittleGuy("Пилюлькин");
      piluylkin.setDuty("Доктор");
      LittleGuy ponchik = new LittleGuy("Пончик");
      ponchik.setEffect(Effect.SHOCKED);

      Table table = new Table();

      Bowl bowl_borscht = new Bowl();
      Eatable borscht = new Eatable("Борщ", 75); 
      bowl_borscht.addItem(borscht);
      table.addItem(bowl_borscht);
      Bowl bowl_porridge = new Bowl();
      Eatable porridge = new Eatable("Каша", 60);
      bowl_porridge.addItem(porridge);
      table.addItem(bowl_porridge);

      ponchik.eatIterative(table);


      LittleGuy neznayka = new LittleGuy("Незнайка");
   }
}