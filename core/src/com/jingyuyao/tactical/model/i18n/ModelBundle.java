package com.jingyuyao.tactical.model.i18n;

/**
 * Contains a list of {@link MessageBundle} for the model.
 */
public class ModelBundle {

  public static final MessageBundle ACTION = new MessageBundle("i18n/model/state/Action");
  public static final MessageBundle DEATH_DIALOGUE =
      new MessageBundle("i18n/model/script/dialogue/Death");
  public static final MessageBundle ITEM_NAME = new MessageBundle("i18n/model/item/ItemName");
  public static final MessageBundle ITEM_DESCRIPTION =
      new MessageBundle("i18n/model/item/ItemDescription");
  public static final MessageBundle PERSON_NAME = new MessageBundle("i18n/model/person/PersonName");
  public static final MessageBundle SHIP_NAME = new MessageBundle("i18n/model/ship/ShipName");
  public static final MessageBundle TERRAIN_NAME =
      new MessageBundle("i18n/model/terrain/TerrainName");

  public static MessageBundle getLevelDialogue(int level) {
    return new MessageBundle("i18n/model/script/dialogue/Level" + level);
  }
}
