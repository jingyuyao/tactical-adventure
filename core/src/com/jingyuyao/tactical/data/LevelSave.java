package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.io.Serializable;
import java.util.List;

/**
 * Save representation for {@link World} and {@link WorldState}.
 */
public class LevelSave implements Serializable {

  private int level;
  private List<Cell> worldCells;
  private List<Ship> inactiveShips;
  private Turn turn;
  private Script script;

  LevelSave() {
  }

  LevelSave(int level, List<Cell> worldCells, List<Ship> inactiveShips, Turn turn, Script script) {
    this.level = level;
    this.worldCells = worldCells;
    this.inactiveShips = inactiveShips;
    this.turn = turn;
    this.script = script;
  }

  public int getLevel() {
    return level;
  }

  public List<Cell> getWorldCells() {
    return worldCells;
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
