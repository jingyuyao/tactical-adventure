package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.World;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.CellFactory;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.state.Waiting;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class WorldLoader {

  private final World world;
  private final CellFactory cellFactory;
  private final Provider<Waiting> waitingProvider;
  private final CharactersLoader charactersLoader;
  private final TerrainsLoader terrainsLoader;

  @Inject
  WorldLoader(
      World world,
      CellFactory cellFactory,
      Provider<Waiting> waitingProvider,
      CharactersLoader charactersLoader,
      TerrainsLoader terrainsLoader) {
    this.world = world;
    this.cellFactory = cellFactory;
    this.waitingProvider = waitingProvider;
    this.charactersLoader = charactersLoader;
    this.terrainsLoader = terrainsLoader;
  }

  public void loadMap(String mapName) {
    Map<Coordinate, Cell> cellMap = new HashMap<>();
    for (Entry<Coordinate, Terrain> entry : terrainsLoader.loadTerrains(mapName).entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Duplicated terrain detected");
      }
      Cell cell = cellFactory.create(coordinate, entry.getValue());
      cellMap.put(coordinate, cell);
    }
    for (Entry<Coordinate, Character> entry : charactersLoader.loadCharacters(mapName).entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (!cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Character not on a terrain");
      }
      Cell cell = cellMap.get(coordinate);
      if (cell.hasCharacter()) {
        throw new IllegalArgumentException("Character occupying same space as another");
      }
      cell.spawnCharacter(entry.getValue());
    }

    world.load(waitingProvider.get(), cellMap.values());
  }
}
