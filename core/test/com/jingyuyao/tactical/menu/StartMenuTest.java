package com.jingyuyao.tactical.menu;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.inject.Guice;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.data.DataManager;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StartMenuTest {

  @Mock
  private Stage stage;
  @Mock
  private GameState gameState;
  @Mock
  private DataManager dataManager;
  @Inject
  private GL20 gl20;
  @Inject
  private Input input;

  private StartMenu startMenu;

  @Before
  public void setUp() {
    Guice.createInjector(new MockGameModule()).injectMembers(this);
    startMenu = new StartMenu(gl20, input, stage, gameState, dataManager);

    verify(stage).addActor(startMenu.getRoot());
    assertThat(startMenu.getRoot().getTitleLabel().getText().toString()).isEqualTo("Start");
  }

  @Test
  public void show() {
    when(dataManager.getInfo()).thenReturn("bwa ha ha");

    startMenu.show();

    verify(input).setInputProcessor(stage);
    VisLabel infoLabel = startMenu.getRoot().findActor("infoLabel");
    assertThat(infoLabel.getText().toString()).isEqualTo("bwa ha ha");
  }

  @Test
  public void play_button() {
    VisTextButton button = startMenu.getRoot().findActor("playButton");

    button.toggle();

    verify(gameState).play();
  }

  @Test
  public void reset_button() {
    when(dataManager.getInfo()).thenReturn("mango berry");
    VisTextButton button = startMenu.getRoot().findActor("resetButton");

    button.toggle();

    verify(gameState).reset();
    VisLabel infoLabel = startMenu.getRoot().findActor("infoLabel");
    assertThat(infoLabel.getText().toString()).isEqualTo("mango berry");
  }
}