package com.jingyuyao.tactical.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.menu.MenuModule.MenuStage;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StartMenu extends AbstractMenu {

  @Inject
  StartMenu(
      GL20 gl,
      Input input,
      @MenuStage Stage stage,
      final GameState gameState,
      final DataManager dataManager) {
    super(gl, input, stage);

    final VisLabel infoLabel = new VisLabel(dataManager.getInfo(), Align.center);
    VisTextButton play = new VisTextButton("Play", "blue", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        gameState.play();
      }
    });
    play.getLabelCell().pad(20);
    VisTextButton reset = new VisTextButton("Reset", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        gameState.reset();
        infoLabel.setText(dataManager.getInfo());
      }
    });
    reset.getLabelCell().pad(20);

    TableBuilder builder = new StandardTableBuilder(new Padding(20));
    builder.append(CellWidget.of(infoLabel).expandY().wrap());
    builder.row();

    builder.append(CellWidget.of(play).align(Alignment.LEFT).expandX().wrap());
    builder.append(reset);

    builder.build(getRoot());
  }

  @Override
  String getTitle() {
    return "Start";
  }
}
