package com.jingyuyao.tactical.data;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game save containing meta data and the player's ships.
 */
public class GameSave {

  private int currentLevel = 1;
  private List<Ship> playerShips = new ArrayList<>();

  private GameSave() {
  }

  GameSave(int currentLevel, List<Ship> playerShips) {
    this.currentLevel = currentLevel;
    this.playerShips = playerShips;
  }

  public int getCurrentLevel() {
    return currentLevel;
  }

  void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  ImmutableList<Ship> getPlayerShips() {
    return ImmutableList.copyOf(playerShips);
  }

  /**
   * Replace the all the player ships with the ones from the world, both active and inactive.
   */
  void replacePlayerShipsFrom(World world) {
    playerShips.clear();
    FluentIterable
        .concat(world.getInactiveShips(), world.getShipSnapshot().values())
        .filter(new Predicate<Ship>() {
          @Override
          public boolean apply(Ship ship) {
            return ship.inGroup(ShipGroup.PLAYER);
          }
        })
        .copyInto(playerShips);
  }
}
