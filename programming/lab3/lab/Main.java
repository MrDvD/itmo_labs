package lab;

import lab.classes.Log;
import lab.classes.being.*;
import lab.classes.container.*;
import lab.classes.location.*;
import lab.classes.transport.Rocket;
import lab.enums.*;
import lab.records.*;

class Main {
   public static void main(String[] args) {
      Planet earth = new Planet("Земля");
      LittleGuy piluylkin = new LittleGuy("Пилюлькин", 140.0);
      piluylkin.setLocation(earth);
      Duty doctor = new Duty("Доктор", 7500, 30);
      piluylkin.setDuty(doctor);
      LittleGuy ponchik = new LittleGuy("Пончик");
      ponchik.setLocation(earth);
      ponchik.setEffect(Effect.SHOCKED);
      piluylkin.work();

      Table table = new Table(100.0);
      // chairs make no sense without table in this model
      table.initChairs(4, 200.0);
      ponchik.seat(table);

      Bowl bowlBorscht = new Bowl(25.0);
      bowlBorscht.addItem(new Eatable("Борщ", (byte) 45, 23.3));
      table.addItem(bowlBorscht);
      Bowl bowlPorridge = new Bowl(26.0);
      bowlPorridge.addItem(new Eatable("Каша", (byte) 40, 24.7));
      table.addItem(bowlPorridge);

      ponchik.eatIterative(table, (byte) 180);
      ponchik.getUp();

      LittleGuy neznayka = new LittleGuy("Незнайка", 141.0);
      neznayka.setLocation(earth);
      Rocket rocket = new Rocket(earth, 6, 200.0);
      for (int i = 0; i < 4; i++) {
         rocket.getLuggage().addItem(new Eatable("Картошка", (byte) 40, 40.2));
      }
      neznayka.seat(rocket);
      ponchik.seat(rocket);

      Moon moon = new Moon();
      rocket.setLocation(moon);
      neznayka.getUp();
      ponchik.getUp();

      Cave cave = new Cave();
      cave.setParent(moon);
      neznayka.goTo(cave);
      ponchik.goTo(cave);

      Underground underground = new Underground();
      underground.setParent(cave);
      neznayka.goTo(underground);
      Log.Console.printf("Список живых сущностей в локации %s:\n", cave);
      Log.Console.println(cave.getVisitorSet());

      ponchik.goTo(moon);
      ponchik.seat(rocket);
      while (rocket.getLuggage().getItemSet().size() > 0) {
         ponchik.eatIterative(rocket.getLuggage());
         ponchik.sleep();
      }
      ponchik.getUp();
      ponchik.goTo(cave);
      ponchik.goTo(underground);
      Town lospaganos = new Town("Лос-Паганос");
      lospaganos.setParent(underground);
      ponchik.goTo(lospaganos);
      
      for (int i = 0; i < 10; i++) {
         ponchik.sell(new Eatable("Соль", (byte) 20, 12, 120));
      }
      ponchik.buy(new Eatable("Суши", (byte) 255, 10, 1200));
      Log.Console.printf("Текущий баланс %s: %.2f у.е.\n", ponchik, ponchik.getBalance());

      Duty worker = new Duty("Рабочий чёртового колеса", 120.5f, 5);
      ponchik.setDuty(worker);
      for (int i = 0; i < 7; i++) {
         ponchik.work();
         ponchik.eat(new Eatable("Съестное", (byte) 35));
      }
      SocialOrganization freeSpinners = new SocialOrganization("Общество свободных крутильщиков");
      ponchik.addSocialStatus(new SocialStatus("Член", freeSpinners));
   }
}