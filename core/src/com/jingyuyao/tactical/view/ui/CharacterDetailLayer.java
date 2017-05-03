package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.model.character.Character;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterDetailLayer extends VisTable {

  private final LayerManager layerManager;
  private final CharacterStatPanel characterStatPanel;
  private final CharacterItemPanel characterItemPanel;

  @Inject
  CharacterDetailLayer(
      LayerManager layerManager,
      CharacterStatPanel characterStatPanel,
      CharacterItemPanel characterItemPanel) {
    super(true);
    this.layerManager = layerManager;
    this.characterStatPanel = characterStatPanel;
    this.characterItemPanel = characterItemPanel;
    setFillParent(true);
    setBackground("window-bg");
    pad(20);

    VisTable scrollTable = new VisTable(true);
    scrollTable.defaults().top().left();
    scrollTable.add(characterStatPanel);
    scrollTable.add(characterItemPanel).expand().fill();

    VisScrollPane scrollPane = new VisScrollPane(scrollTable);
    scrollPane.setScrollingDisabled(true, false);
    add(scrollPane).expand().fill().center();
    row();
    add(this.new CloseButton()).bottom().right();
  }

  void display(Character character) {
    characterStatPanel.display(character);
    characterItemPanel.display(character);
  }

  private class CloseButton extends VisTextButton {

    private CloseButton() {
      super("Close", new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          layerManager.close(CharacterDetailLayer.this);
        }
      });
      pad(20);
    }
  }
}
