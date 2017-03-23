package com.jingyuyao.tactical.model.battle;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.World;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Battle {

  private final EventBus eventBus;
  private final World world;

  @Inject
  Battle(@ModelEventBus EventBus eventBus, World world) {
    this.eventBus = eventBus;
    this.world = world;
  }

  public ListenableFuture<Void> begin(
      final Character attacker, final Weapon weapon, final Target target) {
    SettableFuture<Void> future = SettableFuture.create();
    eventBus.post(new Attack(target, weapon, future));
    Futures.addCallback(future, new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        attacker.useItem(weapon);
        weapon.damages(target);
        world.removeDeadCharacters();
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
    return future;
  }
}
