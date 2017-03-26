package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.List;
import java.util.Map;

public class GameSave {

  private int currentLevel;
  private boolean inProgress;
  private List<Player> startingPlayers;
  // active + inactive players makes up the next list of starting players
  private List<Player> inactivePlayers;
  private Map<Coordinate, Player> activePlayers;
  private Map<Coordinate, Enemy> activeEnemies;
}
