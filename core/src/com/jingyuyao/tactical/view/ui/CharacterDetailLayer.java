package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.character.Character;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterDetailLayer extends VisWindow {

  private final LayerManager layerManager;
  private final CharacterStatPanel characterStatPanel;
  private final CharacterItemPanel characterItemPanel;

  @Inject
  CharacterDetailLayer(
      LayerManager layerManager,
      CharacterStatPanel characterStatPanel,
      CharacterItemPanel characterItemPanel) {
    super("Character Detail", false);
    this.layerManager = layerManager;
    this.characterStatPanel = characterStatPanel;
    this.characterItemPanel = characterItemPanel;
    setFillParent(true);
    setMovable(false);
    addCloseButton();

    VisTable table = new VisTable(true);
    table.defaults().top().left();
    table.add(characterStatPanel);
    table.add(characterItemPanel).expand().fill();

    VisScrollPane scrollPane = new VisScrollPane(table);
    scrollPane.setScrollingDisabled(true, false);
    add(scrollPane).expand().fill().center();
  }

  void display(Character character) {
    characterStatPanel.display(character);
    characterItemPanel.display(character);
  }

  @Override
  protected void close() {
    layerManager.close(CharacterDetailLayer.this);
  }
}
