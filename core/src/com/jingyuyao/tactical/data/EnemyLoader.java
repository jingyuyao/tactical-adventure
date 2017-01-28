package com.jingyuyao.tactical.data;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.item.Item;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class EnemyLoader {

  private final CharacterFactory characterFactory;
  private final ItemLoader itemLoader;

  @Inject
  EnemyLoader(CharacterFactory characterFactory, ItemLoader itemLoader) {
    this.characterFactory = characterFactory;
    this.itemLoader = itemLoader;
  }

  Iterable<Enemy> createEnemies(List<EnemySave> enemySaves) {
    ImmutableList.Builder<Enemy> builder = ImmutableList.builder();
    for (EnemySave enemySave : enemySaves) {
      builder.add(createEnemy(enemySave));
    }
    return builder.build();
  }

  private Enemy createEnemy(EnemySave enemySave) {
    String className = enemySave.getClassName();
    List<Item> items = itemLoader.createItems(enemySave.getItems());
    Enemy enemy;
    if (PassiveEnemy.class.getSimpleName().equals(className)) {
      enemy = characterFactory.createPassiveEnemy(enemySave.getData(), items);
    } else {
      throw new IllegalArgumentException("Unknown enemy class name: " + className);
    }
    return enemy;
  }
}
