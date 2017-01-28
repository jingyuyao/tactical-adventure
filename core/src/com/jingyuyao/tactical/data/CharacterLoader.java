package com.jingyuyao.tactical.data;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterLoader {

  private final CharacterFactory characterFactory;
  private final ItemLoader itemLoader;

  @Inject
  CharacterLoader(CharacterFactory characterFactory, ItemLoader itemLoader) {
    this.characterFactory = characterFactory;
    this.itemLoader = itemLoader;
  }

  Iterable<Character> createCharacter(final Iterable<CharacterSave> characterSaves) {
    return Iterables.transform(characterSaves, new Function<CharacterSave, Character>() {
      @Override
      public Character apply(CharacterSave save) {
        return save.getCharacterData()
            .load(characterFactory, itemLoader.createItems(save.getItemDataList()));
      }
    });
  }
}
