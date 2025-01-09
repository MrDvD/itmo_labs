package lab;

import ru.ifmo.se.pokemon.*;

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
