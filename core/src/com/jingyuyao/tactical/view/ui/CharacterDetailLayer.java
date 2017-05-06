package com.jingyuyao.tactical.view.ui;

import static com.jingyuyao.tactical.view.ui.GameUIModule.BUNDLE;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterDetailLayer extends VisTable {

  private static final Message CLOSE_BTN = BUNDLE.get("closeBtn");

  private final LayerManager layerManager;
  private final CharacterStatsPanel characterStatsPanel;
  private final CharacterItemsPanel characterItemsPanel;
  private final MessageLoader messageLoader;

  @Inject
  CharacterDetailLayer(
      LayerManager layerManager,
      CharacterStatsPanel characterStatsPanel,
      CharacterItemsPanel characterItemsPanel,
      MessageLoader messageLoader) {
    super(true);
    this.layerManager = layerManager;
    this.characterStatsPanel = characterStatsPanel;
    this.characterItemsPanel = characterItemsPanel;
    this.messageLoader = messageLoader;
    setFillParent(true);
    setBackground("window-bg");
    pad(20);

    VisTable scrollTable = new VisTable(true);
    scrollTable.defaults().top().left();
    scrollTable.add(characterStatsPanel);
    scrollTable.add(characterItemsPanel).expand().fill();

    VisScrollPane scrollPane = new VisScrollPane(scrollTable);
    scrollPane.setScrollingDisabled(true, false);
    add(scrollPane).expand().fill().center();
    row();
    add(this.new CloseButton()).bottom().right();
  }

  void display(Character character) {
    characterStatsPanel.display(character);
    characterItemsPanel.display(character);
  }

  private class CloseButton extends VisTextButton {

    private CloseButton() {
      super(messageLoader.get(CLOSE_BTN), new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          layerManager.close(CharacterDetailLayer.this);
        }
      });
      pad(20);
    }
  }
}
