package lab;

import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Sneasel("Снизл", 1);
        Pokemon p2 = new Mesprit("Месприт", 1);
        Pokemon p3 = new Weavile("Вивиль", 1);
        Pokemon p4 = new Poliwag("Поливэг", 1);
        Pokemon p5 = new Poliwhirl("Поливирл", 1);
        Pokemon p6 = new Politoed("Политод", 1);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}
