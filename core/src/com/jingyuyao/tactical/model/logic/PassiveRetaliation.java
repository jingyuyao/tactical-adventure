package com.jingyuyao.tactical.model.logic;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PassiveRetaliation implements Retaliation {

  private final AttackPlanFactory attackPlanFactory;

  @Inject
  public PassiveRetaliation(AttackPlanFactory attackPlanFactory) {
    this.attackPlanFactory = attackPlanFactory;
  }

  @Override
  public ListenableFuture<Void> run(Enemy enemy) {
    // TODO: stub
    return Futures.immediateFuture(null);
  }
}
