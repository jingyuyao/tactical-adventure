package com.jingyuyao.tactical.menu;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LevelProgress;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
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

  private static final String PLAY_BTN = "play button";
  private static final String RESET_BTN = "reset button";

  @Mock
  private Stage stage;
  @Mock
  private GameState gameState;
  @Mock
  private DataManager dataManager;
  @Mock
  private MessageLoader messageLoader;
  @Mock
  private GameSave gameSave;
  @Mock
  private LevelProgress levelProgress;
  @Mock
  private Player player;
  @Inject
  private GL20 gl20;
  @Inject
  private Input input;

  private StartMenu startMenu;

  @Before
  public void setUp() {
    Guice.createInjector(new MockGameModule()).injectMembers(this);
    when(messageLoader.get(MenuBundle.PLAY_BTN)).thenReturn(PLAY_BTN);
    when(messageLoader.get(MenuBundle.RESET_BTN)).thenReturn(RESET_BTN);

    startMenu = new StartMenu(gl20, input, stage, gameState, dataManager, messageLoader);

    verify(stage).addActor(startMenu.getRoot());
  }

  @Test
  public void on_show() {
    when(dataManager.loadCurrentSave()).thenReturn(gameSave);
    when(dataManager.loadCurrentProgress()).thenReturn(Optional.of(levelProgress));
    when(levelProgress.getActivePlayers())
        .thenReturn(ImmutableMap.of(new Coordinate(0, 0), player));
    when(levelProgress.getActiveEnemies()).thenReturn(ImmutableMap.<Coordinate, Enemy>of());
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(messageLoader.get(MenuBundle.HAS_PROGRESS.format(1, 0))).thenReturn("1 p 0 e");
    when(messageLoader.get(MenuBundle.LEVEL_INFO.format(2, "1 p 0 e"))).thenReturn("success");

    startMenu.show();

    verify(input).setInputProcessor(stage);
    VisLabel infoLabel = startMenu.getRoot().findActor("infoLabel");
    assertThat(infoLabel.getText().toString()).isEqualTo("success");
  }

  @Test
  public void play_button() {
    VisTextButton button = startMenu.getRoot().findActor("playButton");
    assertThat(button.getText().toString()).isEqualTo(PLAY_BTN);

    button.toggle();

    verify(gameState).play();
  }

  @Test
  public void reset_button() {
    when(dataManager.loadCurrentSave()).thenReturn(gameSave);
    when(dataManager.loadCurrentProgress()).thenReturn(Optional.of(levelProgress));
    when(levelProgress.getActivePlayers())
        .thenReturn(ImmutableMap.of(new Coordinate(0, 0), player));
    when(levelProgress.getActiveEnemies()).thenReturn(ImmutableMap.<Coordinate, Enemy>of());
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(messageLoader.get(MenuBundle.HAS_PROGRESS.format(1, 0))).thenReturn("1 p 0 e");
    when(messageLoader.get(MenuBundle.LEVEL_INFO.format(2, "1 p 0 e"))).thenReturn("success");
    VisTextButton button = startMenu.getRoot().findActor("resetButton");
    assertThat(button.getText().toString()).isEqualTo(RESET_BTN);

    button.toggle();

    verify(gameState).reset();
    VisLabel infoLabel = startMenu.getRoot().findActor("infoLabel");
    assertThat(infoLabel.getText().toString()).isEqualTo("success");
  }
}