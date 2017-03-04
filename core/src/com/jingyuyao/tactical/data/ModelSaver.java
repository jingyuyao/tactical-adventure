package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.map.Characters;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModelSaver {

  private final DataConfig dataConfig;
  private final Gson gson;
  private final Model model;
  private final Characters characters;

  @Inject
  ModelSaver(DataConfig dataConfig, Gson gson, Model model, Characters characters) {
    this.dataConfig = dataConfig;
    this.gson = gson;
    this.model = model;
    this.characters = characters;
  }

  public void saveMap(String mapName) {
    model.prepForSave();
    FileHandle handle = Gdx.files.local(dataConfig.getMapSaveFileName(mapName));
    CharactersSave save = new CharactersSave(characters.fluent().toList());
    handle.writeString(gson.toJson(save), false);
  }
}
