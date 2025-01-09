package lab;

import ru.ifmo.se.pokemon.*;

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
