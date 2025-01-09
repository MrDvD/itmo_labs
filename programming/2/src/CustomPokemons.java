package lab;

import ru.ifmo.se.pokemon.*;

class Mesprit extends Pokemon {
    Mesprit(String name, int level) {
        super(name, level);
        this.setType(Type.PSYCHIC);
        this.setStats(74.1, 79.9, 85.5, 83.7, 88.1, 63.4);
        this.setMove(new ShadowBall(), new Facade(), new Thunderbolt(), new DreamEater());
    }
    Mesprit() {
        this("Unnamed Mesprit", 1);
    }
}

class Sneasel extends Pokemon {
    Sneasel(String name, int level) {
        super(name, level);
        this.setType(Type.DARK, Type.ICE);
        this.setStats(29.8, 69.9, 29.3, 9.2, 57.5, 93.5);
        this.setMove(new LowSweep(), new AerialAce(), new SwordsDance());
    }
    Sneasel() {
        this("Unnamed Sneasel", 1);
    }
}

class Weavile extends Sneasel {
    Weavile(String name, int level) {
        super(name, level);
        this.setStats(56.6, 87.7, 43.0, 21.0, 69.9, 97.0);
        this.addMove(new NastyPlot());
    }
    Weavile() {
        this("Unnamed Weavile", 1);
    }
}

class Poliwag extends Pokemon {
    Poliwag(String name, int level) {
        super(name, level);
        this.setType(Type.WATER);
        this.setStats(9.5, 17.1, 10.6, 14.5, 10.1, 73.2);
        this.setMove(new BubbleBeam(), new Confide());
    }
    Poliwag() {
        this("Unnamed Poliwag", 1);
    }
}

class Poliwhirl extends Poliwag {
    Poliwhirl(String name, int level) {
        super(name, level);
        this.setStats(48.0, 36.4, 43.0, 27.2, 22.7, 73.2);
        this.addMove(new Bubble());
    }
    Poliwhirl() {
        this("Unnamed Poliwhirl", 1);
    }
}

class Politoed extends Poliwhirl {
    Politoed(String name, int level) {
        super(name, level);
        this.setStats(82.6, 48.9, 57.1, 70.6, 84.4, 53.9);
        this.addMove(new Swagger());
    }
    Politoed() {
        this("Unnamed Politoed", 1);
    }
}
