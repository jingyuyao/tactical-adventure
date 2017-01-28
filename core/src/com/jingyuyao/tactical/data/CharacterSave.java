package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.CharacterData;
import com.jingyuyao.tactical.model.item.ItemData;
import java.util.List;

class CharacterSave {

  private CharacterData characterData;
  private List<ItemData> itemDataList;

  CharacterData getCharacterData() {
    return characterData;
  }

  List<ItemData> getItemDataList() {
    return itemDataList;
  }
}
