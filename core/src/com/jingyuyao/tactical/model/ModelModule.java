package com.jingyuyao.tactical.model;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.CharacterModule;
import com.jingyuyao.tactical.model.item.ItemModule;
import com.jingyuyao.tactical.model.map.MapModule;
import com.jingyuyao.tactical.model.mark.MarkModule;
import com.jingyuyao.tactical.model.state.StateModule;
import java.util.LinkedList;
import java.util.Queue;
import javax.inject.Singleton;

public class ModelModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new MapModule());
    install(new CharacterModule());
    install(new ItemModule());
    install(new MarkModule());
    install(new StateModule());

    bind(Waiter.class);
    bind(AttackPlanFactory.class);
  }

  @Provides
  @Singleton
  @Waiter.BackingWaiterQueue
  Queue<Runnable> provideRunnableQueue() {
    return new LinkedList<Runnable>();
  }
}
