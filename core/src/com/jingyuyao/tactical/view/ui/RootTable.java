package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RootTable extends Table {

  @Inject
  RootTable(
      ActionGroup actionGroup,
      CharacterInfo characterInfo,
      TerrainInfo terrainInfo,
      ItemInfo itemInfo) {
    setDebug(true);
    setFillParent(true);
    pad(10);

    Table left = new Table().debug();
    left.defaults().top().left();
    left.add(itemInfo).expand();

    Table mid = new Table().debug();

    Table right = new Table().debug();
    right.defaults().top().right();
    right.add(characterInfo);
    right.row();
    right.add(terrainInfo);
    right.row();
    right.add(actionGroup).bottom().expand();

    // fill() enables the sub-tables to distribute its own vertical space.
    // grow() causes the middle column to take up all the horizontal space.
    add(left).fill();
    add(mid).grow();
    add(right).fill();
  }
}
