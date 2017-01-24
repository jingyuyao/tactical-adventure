package com.jingyuyao.tactical.view.actor;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.InputLock;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.item.event.Attack;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AttackSubscriber implements EventSubscriber {

  private final Actors actors;
  private final InputLock inputLock;

  @Inject
  AttackSubscriber(Actors actors, InputLock inputLock) {
    this.actors = actors;
    this.inputLock = inputLock;
  }

  @Subscribe
  public void attacked(Attack attack) {
    attack.done();
  }
}
