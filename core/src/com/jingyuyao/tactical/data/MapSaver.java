package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.jingyuyao.tactical.model.map.Characters;
import java.io.Writer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapSaver {

  private final Gson gson;
  private final Characters characters;

  @Inject
  MapSaver(Gson gson, Characters characters) {
    this.gson = gson;
    this.characters = characters;
  }

  public void saveMap(String name) {
    FileHandle handle = Gdx.files.local(name + ".save");
    Writer writer = handle.writer(false);
  }
}
