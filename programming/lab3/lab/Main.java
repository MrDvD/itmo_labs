package lab;

import lab.classes.being.*;
import lab.classes.container.*;
import lab.classes.location.*;
import lab.classes.transport.Rocket;
import lab.enums.*;
import lab.records.*;

class Main {
   public static void main(String[] args) {
      LittleGuy piluylkin = new LittleGuy("Пилюлькин", 140.0);
      Duty doctor = new Duty("Доктор", 7500, 30);
      piluylkin.setDuty(doctor);
      LittleGuy ponchik = new LittleGuy("Пончик", 142.0);
      ponchik.setEffect(Effect.SHOCKED);

      Table table = new Table(100.0);
      table.initChairs(4, 200.0);
      ponchik.seat(table.getFreeChair());

      Bowl bowl_borscht = new Bowl(25.0);
      Eatable borscht = new Eatable("Борщ", (byte) 75, 23.3); 
      bowl_borscht.addItem(borscht);
      table.addItem(bowl_borscht);
      Bowl bowl_porridge = new Bowl(26.0);
      Eatable porridge = new Eatable("Каша", (byte) 60, 24.7);
      bowl_porridge.addItem(porridge);
      table.addItem(bowl_porridge);

      ponchik.eatIterative(table, (byte) 120);
      ponchik.getUp();

      LittleGuy neznayka = new LittleGuy("Незнайка", 141.0);
      Planet earth = new Planet("Земля");
      Rocket rocket = new Rocket(earth);
      rocket.addPassenger(neznayka);
      rocket.addPassenger(ponchik);

      Moon moon = new Moon();
      rocket.setLocation(moon);

      // clear all?
      rocket.popPassenger();
      rocket.popPassenger();

      Cave cave = new Cave();
      neznayka.setLocation(cave); // сделать так, чтобы тратился голод?
      ponchik.setLocation(cave);

      Underground underground = new Underground();
      neznayka.setLocation(underground);
      cave.getVisitorList();

      ponchik.setLocation(moon); // изменить текст в зависимости от родителя
      rocket.addPassenger(ponchik);
      ponchik.eatIterative(rocket.getLuggage());
      rocket.popPassenger();
      ponchik.setLocation(cave);
      ponchik.setLocation(underground);
      Town lospaganos = new Town("Лос-Паганос"); // сделать вместо отдельного класса общий
      lospaganos.setParent(underground);
      ponchik.setLocation(lospaganos);
      
      for (int i = 0; i < 25; i++) {
         ponchik.sell(new Eatable("Соль", (byte) 20, 120));
      }
      Eatable sushi = new Eatable("Суши", (byte) 255, 10000);
      ponchik.buy(sushi);

      Duty worker = new Duty("Рабочий чёртового колеса", 100, 7);
      ponchik.setDuty(worker);
      for (int i = 0; i < 7; i++) {
         ponchik.work();
      }
      SocialOrganization freeSpinners = new SocialOrganization("Общество свободных крутильщиков");
      ponchik.addSocialStatus(new SocialStatus("Член", freeSpinners));
   }
}