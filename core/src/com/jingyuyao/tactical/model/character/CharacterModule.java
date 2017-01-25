package com.jingyuyao.tactical.model.character;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.jingyuyao.tactical.model.item.Item;
import java.util.ArrayList;
import java.util.List;

public class CharacterModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder()
        .implement(Enemy.class, Names.named("PassiveEnemy"), PassiveEnemy.class)
        .build(CharacterFactory.class));
  }

  @Provides
  List<Item> provideInitialItems() {
    return new ArrayList<Item>();
  }
}
