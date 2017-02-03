package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RootTable extends Table {

  @Inject
  RootTable(
      ActionGroup actionGroup,
      EnemyBurst enemyBurst,
      PlayerBurst playerBurst) {
    pad(10);
    setFillParent(true);
    setDebug(true);

    // row 1
    add(enemyBurst).top().left();
    add(playerBurst).top().right();

    // row 2
    row();
    add();
    add(actionGroup).bottom().right().expand();
  }
}
