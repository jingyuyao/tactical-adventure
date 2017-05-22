package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.List;
import java.util.Map;

/**
 * Save representation for {@link World} and {@link WorldState}.
 */
public class LevelSave {

  private Map<Coordinate, Ship> activeShips;
  private List<Ship> inactiveShips;
  private Turn turn;
  private Script script;

  private LevelSave() {
  }

  LevelSave(
      Map<Coordinate, Ship> activeShips,
      List<Ship> inactiveShips,
      Turn turn,
      Script script) {
    this.activeShips = activeShips;
    this.inactiveShips = inactiveShips;
    this.turn = turn;
    this.script = script;
  }

  public Map<Coordinate, Ship> getActiveShips() {
    return activeShips;
  }

  public List<Ship> getInactiveShips() {
    return inactiveShips;
  }

  public Turn getTurn() {
    return turn;
  }

  public Script getScript() {
    return script;
  }
}
