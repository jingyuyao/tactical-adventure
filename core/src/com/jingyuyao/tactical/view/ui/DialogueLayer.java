package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DialogueLayer extends VisTable {

  private final LayerManager layerManager;
  private final TextLoader textLoader;
  private final VisLabel name;
  private final VisLabel text;
  private ShowDialogues showDialogues;
  private int index;

  @Inject
  DialogueLayer(LayerManager layerManager, TextLoader textLoader) {
    super(true);
    this.textLoader = textLoader;
    this.layerManager = layerManager;
    this.name = new VisLabel();
    this.text = new VisLabel();
    this.text.setAlignment(Align.topLeft);
    this.text.setWrap(true);
    setFillParent(true);
    setBackground("window-bg");
    pad(20);

    add(name).top().left();
    row();
    VisScrollPane scrollPane = new VisScrollPane(text);
    scrollPane.setScrollingDisabled(true, false);
    add(scrollPane).grow().top().left();
    row();
    add(this.new NextButton()).bottom().right();
  }

  void display(ShowDialogues showDialogues) {
    this.showDialogues = showDialogues;
    this.index = 0;
    layerManager.open(this);
    next();
  }

  private void next() {
    if (index < showDialogues.getDialogues().size()) {
      Dialogue dialogue = showDialogues.getDialogues().get(index);
      name.setText(textLoader.get(dialogue.getName()));
      text.setText(textLoader.get(dialogue.getResourceKey()));
      index++;
    } else {
      layerManager.close(this);
      showDialogues.complete();
    }
  }

  private class NextButton extends VisTextButton {

    private NextButton() {
      super(textLoader.get(UIBundle.NEXT_BTN), new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          DialogueLayer.this.next();
        }
      });
      pad(20);
    }
  }
}
