package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Item;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisWindow;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterDetailLayer extends VisWindow {

  private final LayerManager layerManager;
  private final VisScrollPane content;

  @Inject
  CharacterDetailLayer(LayerManager layerManager) {
    super("Character Detail", false);
    this.layerManager = layerManager;
    this.content = new VisScrollPane(null);
    setFillParent(true);
    setMovable(false);
    addCloseButton();
    closeOnEscape();

    add(content).expand().fill().center();
  }

  void display(Character character) {
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("Name: %s\n", character.getName()));
    builder.append(String.format(Locale.US, "HP: %d\n", character.getHp()));
    builder.append(String.format(Locale.US, "Move: %d\n", character.getMoveDistance()));
    builder.append("Items:\n");
    FluentIterable<Item> items = character.fluentItems();
    if (items.isEmpty()) {
      builder.append("None");
    } else {
      for (Item item : character.fluentItems()) {
        builder.append(String.format(
            Locale.US, "%s (%d): %s\n",
            item.getName(), item.getUsageLeft(), item.getDescription()));
      }
    }
    content.setWidget(new VisLabel(builder.toString(), Align.topLeft));
  }

  @Override
  protected void close() {
    layerManager.close(CharacterDetailLayer.this);
  }
}
