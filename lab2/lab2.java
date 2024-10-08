import ru.ifmo.se.pokemon.*;

public class lab2 {
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

// POKEMONS

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

// PHYSICAL MOVES

class Facade extends PhysicalMove {
    Facade() {
        this.power = 42.5;
        this.type = Type.NORMAL;
        this.accuracy = 1.0;
    }
    @Override
    public String describe() {
        return "uses Facade";
    }
    @Override
    public void applySelfEffects(Pokemon p) {
        Status curr = p.getCondition();
        if (curr == Status.BURN || curr == Status.PARALYZE || curr == Status.POISON) {
            this.power = 85;
        } else {
            this.power = 42.5;
        }
    }
}

class LowSweep extends PhysicalMove {
    LowSweep() {
        this.power = 36.7;
        this.type = Type.FIGHTING;
        this.accuracy = 1.0;
    }
    @Override
    public String describe() {
        return "uses Low Sweep";
    }
    @Override
    public void applyOppEffects(Pokemon p) {
        p.addEffect(new Effect().stat(Stat.SPEED, -1));
    }
}

class AerialAce extends PhysicalMove {
    AerialAce() {
        this.power = 29.4;
        this.type = Type.FLYING;
        this.accuracy = 1.0;
    }
    @Override
    public String describe() {
        return "uses Flying";
    }
    @Override
    public boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }
}

// SPECIAL MOVES

class ShadowBall extends SpecialMove {
    ShadowBall() {
        this.power = 55.2;
        this.type = Type.GHOST;
        this.accuracy = 1.0;
    }
    @Override
    public String describe() {
        return "uses Shadow Ball";
    }
    @Override
    public void applyOppEffects(Pokemon p) {
        p.addEffect(new Effect().chance(0.2).stat(Stat.SPECIAL_DEFENSE, -1));
    }
}

class Thunderbolt extends SpecialMove {
    Thunderbolt() {
        this.power = 67.3;
        this.type = Type.ELECTRIC;
        this.accuracy = 1.0;
    }
    @Override
    public String describe() {
        return "uses Thunderbolt";
    }
    @Override
    public void applyOppEffects(Pokemon p) {
        p.addEffect(new Effect().chance(0.1).condition(Status.PARALYZE));
    }
}

class DreamEater extends SpecialMove {
    DreamEater() {
        this.power = 76.4;
        this.type = Type.PSYCHIC;
        this.accuracy = 1.0;
    }
    @Override
    public String describe() {
        return "uses Dream Eater";
    }
    @Override
    public boolean checkAccuracy(Pokemon att, Pokemon def) {
        return (def.getCondition() == Status.SLEEP) && super.checkAccuracy(att, def);
    }
    @Override
    public void applySelfDamage(Pokemon att, double damage) {
        att.setMod(Stat.HP, (int) (-0.5 * damage));
    }
}

class Bubble extends SpecialMove {
    Bubble() {
        this.power = 13.8;
        this.type = Type.WATER;
        this.accuracy = 1.0;
    }
    @Override
    public String describe() {
        return "uses Bubble";
    }
    @Override
    public void applyOppEffects(Pokemon p) {
        p.addEffect(new Effect().chance(0.1).stat(Stat.SPEED, -1));
    }
}

class BubbleBeam extends Bubble {
    BubbleBeam() {
        super();
        this.power = 36.7;
    }
    @Override
    public String describe() {
        return "uses Bubble Beam";
    }
}

// STATUS MOVES

class SwordsDance extends StatusMove {
    SwordsDance() {
        this.type = Type.NORMAL;
    }
    @Override
    public String describe() {
        return "uses Swords Dance";
    }
    @Override
    public void applySelfEffects(Pokemon p) {
        p.setMod(Stat.ATTACK, 2);
    }
    @Override
    public boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }
}

class NastyPlot extends StatusMove {
    NastyPlot() {
        this.type = Type.DARK;
    }
    @Override
    public String describe() {
        return "uses Nasty Plot";
    }
    @Override
    public void applySelfEffects(Pokemon p) {
        p.setMod(Stat.SPECIAL_ATTACK, 2);
    }
    @Override
    public boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }
}

class Confide extends StatusMove {
    Confide() {
        this.type = Type.NORMAL;
    }
    @Override
    public String describe() {
        return "uses Confide";
    }
    @Override
    public void applyOppEffects(Pokemon p) {
        p.setMod(Stat.SPECIAL_ATTACK, -1);
    }
    @Override
    public boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }
}

class Swagger extends StatusMove {
    Swagger() {
        this.type = Type.NORMAL;
        this.accuracy = 0.85;
    }
    @Override
    public String describe() {
        return "uses Swagger";
    }
    @Override
    public void applyOppEffects(Pokemon p) {
        p.addEffect(new Effect().stat(Stat.ATTACK, 2));
        p.confuse();
    }
}
