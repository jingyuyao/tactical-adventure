package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RootTable extends Table {

  @Inject
  RootTable(ActionGroup actionGroup, PlayerBurst playerBurst) {
    pad(10);
    setFillParent(true);
    setDebug(true);

    // row 1
    add(playerBurst).top().right();

    // row 2
    row();
    add(actionGroup).bottom().right().expand();
  }
}
