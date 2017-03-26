package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.World;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldSaver {

  private final DataConfig dataConfig;
  private final Gson gson;
  private final Model model;
  private final World world;

  @Inject
  WorldSaver(DataConfig dataConfig, Gson gson, Model model, World world) {
    this.dataConfig = dataConfig;
    this.gson = gson;
    this.model = model;
    this.world = world;
  }

  public void saveMap(String mapName) {
    FileHandle handle = Gdx.files.local(dataConfig.getCharactersSaveFileName(mapName));

    model.prepForSave();
    Map<Coordinate, Character> characterMap = new HashMap<>();
    for (Cell cell : world.getCells()) {
      if (cell.hasCharacter()) {
        characterMap.put(cell.getCoordinate(), cell.getCharacter());
      }
    }
    CharactersSave save = new CharactersSave(characterMap);
    handle.writeString(gson.toJson(save), false);
  }

  public void removeSave(String mapName) {
    FileHandle handle = Gdx.files.local(dataConfig.getCharactersSaveFileName(mapName));
    handle.delete();
  }
}
