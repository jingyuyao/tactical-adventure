package com.jingyuyao.tactical.model.resource;

/**
 * Contains a list of {@link KeyBundle} for the model.
 */
public class ModelBundle {

  // Text
  public static final KeyBundle ACTION = KeyBundle.i18n("model/state/Action");
  public static final KeyBundle DEATH_DIALOGUE = KeyBundle.i18n("model/script/dialogue/Death");
  public static final KeyBundle ITEM_NAME = KeyBundle.i18n("model/item/ItemName");
  public static final KeyBundle ITEM_DESCRIPTION = KeyBundle.i18n("model/item/ItemDescription");
  public static final KeyBundle PERSON_NAME = KeyBundle.i18n("model/person/PersonName");
  public static final KeyBundle PERSON_ROLE = KeyBundle.i18n("model/person/PersonRole");
  public static final KeyBundle SHIP_NAME = KeyBundle.i18n("model/ship/ShipName");
  public static final KeyBundle TERRAIN_NAME = KeyBundle.i18n("model/terrain/TerrainName");

  // Drawables
  public static final KeyBundle SHIP_ANIMATIONS = KeyBundle.animation("model/ship");
  public static final KeyBundle WEAPON_ANIMATIONS = KeyBundle.animation("model/weapon");

  public static KeyBundle getLevelDialogue(int level) {
    return KeyBundle.i18n("model/script/dialogue/Level" + level);
  }
}
