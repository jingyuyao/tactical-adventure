package com.jingyuyao.tactical.model.target;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapObject;

// TODO: need a method that return a "target info" for this target to be displayed
public interface Target {

  /**
   * @return a current view of all the objects on the map that "selects" this target
   */
  Iterable<MapObject> getSelectObjects();

  /**
   * @return a current view of all the characters being targeted
   */
  Iterable<Character> getTargetCharacters();

  void showMarking();

  void hideMarking();

  /**
   * Executes this target. Behavior varies depending on the implementation.
   */
  void execute();
}
