package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.FluentIterable;
import com.google.gson.Gson;
import com.jingyuyao.tactical.model.character.Character;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharactersLoader {

  private final DataConfig dataConfig;
  private final Gson gson;

  @Inject
  CharactersLoader(DataConfig dataConfig, Gson gson) {
    this.dataConfig = dataConfig;
    this.gson = gson;
  }

  FluentIterable<Character> loadCharacters(String mapName) {
    FileHandle mapData = Gdx.files.internal(dataConfig.getCharactersFileName(mapName));
    CharactersSave charactersSave = gson.fromJson(mapData.readString(), CharactersSave.class);
    return charactersSave.getCharacters();
  }
}
