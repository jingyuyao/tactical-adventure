package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.DirectionalWeaponData;
import com.jingyuyao.tactical.model.item.Grenade;
import com.jingyuyao.tactical.model.item.GrenadeData;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.HealData;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.ItemFactory;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ItemLoader {

  private final Gson gson;
  private final ItemFactory itemFactory;

  @Inject
  ItemLoader(Gson gson, ItemFactory itemFactory) {
    this.gson = gson;
    this.itemFactory = itemFactory;
  }

  List<Item> createItems(Iterable<ItemSave> itemSaves) {
    List<Item> items = new ArrayList<Item>();
    if (itemSaves != null) {
      for (ItemSave itemSave : itemSaves) {
        items.add(createItem(itemSave));
      }
    }
    return items;
  }

  private Item createItem(ItemSave itemSave) {
    String className = itemSave.getClassName();
    if (DirectionalWeapon.class.getSimpleName().equals(className)) {
      return itemFactory.createDirectionalWeapon(
          itemSave.getData(gson, DirectionalWeaponData.class));
    } else if (Grenade.class.getSimpleName().equals(className)) {
      return itemFactory.createGrenade(itemSave.getData(gson, GrenadeData.class));
    } else if (Heal.class.getSimpleName().equals(className)) {
      return itemFactory.createHeal(itemSave.getData(gson, HealData.class));
    }
    throw new IllegalArgumentException("Unknown item class name: " + className);
  }
}
