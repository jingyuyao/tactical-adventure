package com.jingyuyao.tactical.model.character;

public interface Player extends Character {

  boolean isActionable();

  void setActionable(boolean actionable);
}
