package com.jingyuyao.tactical.model.battle;

import com.jingyuyao.tactical.model.common.Coordinate;

// TODO: need a method that return a "target info" for this target to be displayed
public interface Target {

  Coordinate getSelectCoordinate();

  void showMarking();

  void hideMarking();

  void execute();
}
