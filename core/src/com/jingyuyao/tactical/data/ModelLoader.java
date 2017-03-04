package com.jingyuyao.tactical.data;

import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.state.Waiting;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ModelLoader {

  private final Model model;
  private final Provider<Waiting> waitingProvider;
  private final CharactersLoader charactersLoader;
  private final TerrainsLoader terrainsLoader;

  @Inject
  ModelLoader(
      Model model,
      Provider<Waiting> waitingProvider,
      CharactersLoader charactersLoader,
      TerrainsLoader terrainsLoader) {
    this.model = model;
    this.waitingProvider = waitingProvider;
    this.charactersLoader = charactersLoader;
    this.terrainsLoader = terrainsLoader;
  }

  public void loadMap(String mapName) {
    FluentIterable<Character> characters = charactersLoader.loadCharacters(mapName);
    model.loadMap(
        terrainsLoader.loadTerrains(mapName),
        characters.filter(Player.class),
        characters.filter(Enemy.class),
        waitingProvider.get());
  }
}
