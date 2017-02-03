package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RootTable extends Table {

  @Inject
  RootTable(ActionGroup actionGroup) {
    pad(10);
    setFillParent(true);
    setDebug(true);

    add(actionGroup).bottom().right().expand();
  }
}
