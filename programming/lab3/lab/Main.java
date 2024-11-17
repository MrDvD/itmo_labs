package lab;

import lab.classes.being.*;
import lab.classes.container.*;
import lab.classes.location.*;
import lab.classes.transport.Rocket;
import lab.enums.*;
import lab.records.*;

class Main {
   public static void main(String[] args) {
      LittleGuy piluylkin = new LittleGuy("Пилюлькин");
      Duty doctor = new Duty("Доктор", 7500, 30);
      piluylkin.setDuty(doctor);
      LittleGuy ponchik = new LittleGuy("Пончик");
      ponchik.setEffect(Effect.SHOCKED);

      Table table = new Table();
      // table.addUser(ponchik);

      Bowl bowl_borscht = new Bowl();
      Eatable borscht = new Eatable("Борщ", 75); 
      bowl_borscht.addItem(borscht);
      table.addItem(bowl_borscht);
      Bowl bowl_porridge = new Bowl();
      Eatable porridge = new Eatable("Каша", 60);
      bowl_porridge.addItem(porridge);
      table.addItem(bowl_porridge);

      ponchik.eatIterative(table, (byte) 120);

      LittleGuy neznayka = new LittleGuy("Незнайка");
      Earth earth = new Earth();
      Rocket rocket = new Rocket(earth);
      rocket.addPassenger(neznayka);
      rocket.addPassenger(ponchik);

      Moon moon = new Moon();
      rocket.setLocation(moon);
      // ...
      Eatable salt = new Eatable("Соль", 30, 150);
      ponchik.sell(salt);
      // ... разорился
      SocialOrganization freeSpinners = new SocialOrganization("Общество свободных крутильщиков");
      ponchik.addSocialStatus(new SocialStatus("Член", freeSpinners));
   }
}