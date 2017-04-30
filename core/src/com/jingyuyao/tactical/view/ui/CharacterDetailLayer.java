package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.model.character.Character;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterDetailLayer extends VisTable {

  private static final String FMT = "Detail:\n%s";

  private final VisLabel text;

  @Inject
  CharacterDetailLayer(final LayerManager layerManager) {
    this.text = new VisLabel();
    setFillParent(true);
    setBackground("window-noborder");

    VisTextButton back = new VisTextButton("Back");
    back.pad(20);
    back.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        layerManager.close(CharacterDetailLayer.this);
      }
    });
    TableBuilder builder = new StandardTableBuilder(new Padding(20));
    builder.append(CellWidget.of(text).expandX().expandY().wrap());
    builder.row();
    builder.append(CellWidget.of(back).align(Alignment.RIGHT).wrap());
    builder.build(this);
  }

  void display(Character character) {
    text.setText(String.format(Locale.US, FMT, character.getName()));
  }
}
