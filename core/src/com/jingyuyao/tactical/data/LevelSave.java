package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.Map;

/**
 * Save representation for {@link World} and {@link WorldState}.
 */
public class LevelSave {

  private Map<Coordinate, Ship> ships;
  private Turn turn;
  private Script script;

  private LevelSave() {
  }

  LevelSave(Map<Coordinate, Ship> ships, Turn turn, Script script) {
    this.ships = ships;
    this.turn = turn;
    this.script = script;
  }

  public Map<Coordinate, Ship> getShips() {
    return ships;
  }

  public Turn getTurn() {
    return turn;
  }

  public Script getScript() {
    return script;
  }
}
