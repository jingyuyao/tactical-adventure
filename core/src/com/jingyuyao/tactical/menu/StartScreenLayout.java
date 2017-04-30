package com.jingyuyao.tactical.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.DataManager;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class StartScreenLayout extends VisTable {

  @Inject
  StartScreenLayout(final GameState gameState, final DataManager dataManager) {
    super(true);
    setFillParent(true);

    final VisLabel infoLabel = new VisLabel(dataManager.getInfo(), Align.center);
    VisTextButton play = new VisTextButton("Play", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        gameState.play();
      }
    });
    play.getLabelCell().pad(30);
    VisTextButton reset = new VisTextButton("Reset", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        gameState.reset();
        infoLabel.setText(dataManager.getInfo());
      }
    });
    reset.getLabelCell().pad(30);

    TableBuilder builder = new StandardTableBuilder(new Padding(20));
    builder.append(CellWidget.of(infoLabel).expandY().wrap());
    builder.row();

    builder.append(CellWidget.of(play).align(Alignment.LEFT).expandX().wrap());
    builder.append(reset);

    builder.build(this);
  }
}
