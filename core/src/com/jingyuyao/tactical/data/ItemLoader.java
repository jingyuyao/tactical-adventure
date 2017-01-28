package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.ItemData;
import com.jingyuyao.tactical.model.item.ItemFactory;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ItemLoader {

  private final ItemFactory itemFactory;

  @Inject
  ItemLoader(ItemFactory itemFactory) {
    this.itemFactory = itemFactory;
  }

  List<Item> createItems(Iterable<ItemData> itemDataIterable) {
    List<Item> items = new ArrayList<Item>();
    if (itemDataIterable != null) {
      for (ItemData itemData : itemDataIterable) {
        items.add(itemData.load(itemFactory));
      }
    }
    return items;
  }
}
