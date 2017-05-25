package com.jingyuyao.tactical.model.resource;

/**
 * Contains a list of {@link KeyBundle} for the model.
 */
public class ModelBundle {

  // Text
  public static final KeyBundle ACTION = new KeyBundle("i18n/model/state/Action");
  public static final KeyBundle DEATH_DIALOGUE =
      new KeyBundle("i18n/model/script/dialogue/Death");
  public static final KeyBundle ITEM_NAME =
      new KeyBundle("i18n/model/item/ItemName");
  public static final KeyBundle ITEM_DESCRIPTION =
      new KeyBundle("i18n/model/item/ItemDescription");
  public static final KeyBundle PERSON_NAME =
      new KeyBundle("i18n/model/person/PersonName");
  public static final KeyBundle PERSON_ROLE =
      new KeyBundle("i18n/model/person/PersonRole");
  public static final KeyBundle SHIP_NAME =
      new KeyBundle("i18n/model/ship/ShipName");
  public static final KeyBundle TERRAIN_NAME =
      new KeyBundle("i18n/model/terrain/TerrainName");

  // Drawables
  public static final KeyBundle SHIP_ANIMATIONS =
      new KeyBundle("animation/model/ship");
  public static final KeyBundle WEAPON_ANIMATIONS =
      new KeyBundle("animation/model/weapon");

  public static KeyBundle getLevelDialogue(int level) {
    return new KeyBundle("i18n/model/script/dialogue/Level" + level);
  }
}
