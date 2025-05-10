package com.itmo.mrdvd.object;

public class Coordinates {
  private Float x, y;

  public void setX(Float x) {
    this.x = x;
  }

  public void setY(Float y) {
    this.y = y;
  }

  public Float getX() {
    return x;
  }

  public Float getY() {
    return y;
  }

  @Override
  public String toString() {
    return String.format("(%.2f, %.2f)", getX(), getY());
  }
}
