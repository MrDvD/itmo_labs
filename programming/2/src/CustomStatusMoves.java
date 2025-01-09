package lab;

import ru.ifmo.se.pokemon.*;

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
