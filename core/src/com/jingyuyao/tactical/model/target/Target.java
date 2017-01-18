package com.jingyuyao.tactical.model.target;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapObject;

// TODO: need a method that return a "target info" for this target to be displayed
public interface Target {

  /**
   * @return a current view of all the objects on the map that "selects" this target
   */
  ImmutableSet<MapObject> getSelectObjects();

  /**
   * @return a current view of all the characters being targeted
   */
  ImmutableSet<Character> getTargetCharacters();

  void showMarking();

  void hideMarking();
}
