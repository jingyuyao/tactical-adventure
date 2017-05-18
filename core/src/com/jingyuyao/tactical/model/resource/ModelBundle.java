package com.jingyuyao.tactical.model.resource;

/**
 * Contains a list of {@link ResourceKeyBundle} for the model.
 */
public class ModelBundle {

  // Text
  public static final ResourceKeyBundle ACTION = new ResourceKeyBundle("i18n/model/state/Action");
  public static final ResourceKeyBundle DEATH_DIALOGUE =
      new ResourceKeyBundle("i18n/model/script/dialogue/Death");
  public static final ResourceKeyBundle ITEM_NAME =
      new ResourceKeyBundle("i18n/model/item/ItemName");
  public static final ResourceKeyBundle ITEM_DESCRIPTION =
      new ResourceKeyBundle("i18n/model/item/ItemDescription");
  public static final ResourceKeyBundle PERSON_NAME =
      new ResourceKeyBundle("i18n/model/person/PersonName");
  public static final ResourceKeyBundle PERSON_ROLE =
      new ResourceKeyBundle("i18n/model/person/PersonRole");
  public static final ResourceKeyBundle SHIP_NAME =
      new ResourceKeyBundle("i18n/model/ship/ShipName");
  public static final ResourceKeyBundle TERRAIN_NAME =
      new ResourceKeyBundle("i18n/model/terrain/TerrainName");

  // Drawables
  public static final ResourceKeyBundle SHIP_ANIMATIONS =
      new ResourceKeyBundle("animation/model/ship");
  public static final ResourceKeyBundle WEAPON_ANIMATIONS =
      new ResourceKeyBundle("animation/model/weapon");

  public static ResourceKeyBundle getLevelDialogue(int level) {
    return new ResourceKeyBundle("i18n/model/script/dialogue/Level" + level);
  }
}
