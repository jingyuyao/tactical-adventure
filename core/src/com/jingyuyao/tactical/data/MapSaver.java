package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.map.Characters;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapSaver {

  private final Gson gson;
  private final Model model;
  private final Characters characters;

  @Inject
  MapSaver(Gson gson, Model model, Characters characters) {
    this.gson = gson;
    this.model = model;
    this.characters = characters;
  }

  public void saveMap(String name) {
    model.prepForSave();
    FileHandle handle = Gdx.files.local(name + ".save.json");
    MapSave save = new MapSave(ImmutableList.copyOf(characters.getAll()));
    handle.writeString(gson.toJson(save), false);
  }
}
