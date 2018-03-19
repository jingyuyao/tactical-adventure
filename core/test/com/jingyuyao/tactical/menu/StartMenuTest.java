package com.jingyuyao.tactical.menu;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.base.Optional;
import com.google.inject.Guice;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LevelSave;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Cell;
import com.kotcrab.vis.ui.util.dialog.Dialogs.OptionDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import java.util.Arrays;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StartMenuTest {

  private static final String PLAY_BTN = "play button";
  private static final String RESET_LEVEL_BTN = "reset button";
  private static final String CLEAR_SAVE_BTN = "clear save";

  @Mock
  private Stage stage;
  @Mock
  private GameState gameState;
  @Mock
  private DataManager dataManager;
  @Mock
  private TextLoader textLoader;
  @Mock
  private GameSave gameSave;
  @Mock
  private LevelSave levelSave;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Captor
  private ArgumentCaptor<InputProcessor> argumentCaptor;
  @Captor
  private ArgumentCaptor<Actor> actorCaptor;
  @Inject
  private GL20 gl20;
  @Inject
  private Input input;

  private StartMenu startMenu;

  @Before
  public void setUp() {
    Guice.createInjector(new MockGameModule()).injectMembers(this);
    when(textLoader.get(MenuBundle.PLAY_BTN)).thenReturn(PLAY_BTN);
    when(textLoader.get(MenuBundle.RESET_LEVEL_BTN)).thenReturn(RESET_LEVEL_BTN);
    when(textLoader.get(MenuBundle.CLEAR_SAVE_BTN)).thenReturn(CLEAR_SAVE_BTN);

    startMenu = new StartMenu(gl20, input, stage, gameState, dataManager, textLoader);

    verify(stage).addActor(startMenu.getRoot());
  }

  @Test
  public void on_show() {
    when(dataManager.loadGameSave()).thenReturn(gameSave);
    when(dataManager.loadLevelSave()).thenReturn(Optional.of(levelSave));
    when(levelSave.getWorldCells()).thenReturn(Arrays.asList(cell1, cell2));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    when(ship2.inGroup(ShipGroup.ENEMY)).thenReturn(true);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(textLoader.get(MenuBundle.HAS_PROGRESS.format(1, 1))).thenReturn("1 p 1 e");
    when(textLoader.get(MenuBundle.LEVEL_INFO.format(2, "1 p 1 e"))).thenReturn("success");

    startMenu.show();

    verify(input).setInputProcessor(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(InputMultiplexer.class);
    InputMultiplexer multiplexer = (InputMultiplexer) argumentCaptor.getValue();
    assertThat(multiplexer.getProcessors()).contains(stage);
    VisLabel infoLabel = startMenu.getRoot().findActor("infoLabel");
    assertThat(infoLabel.getText().toString()).isEqualTo("success");
  }

  @Test
  public void play_button() {
    VisTextButton button = startMenu.getRoot().findActor("playButton");
    assertThat(button.getText().toString()).isEqualTo(PLAY_BTN);

    button.toggle();

    verify(gameState).playCurrentLevel();
  }

  @Test
  public void reset_level_button() {
    when(dataManager.loadGameSave()).thenReturn(gameSave);
    when(dataManager.loadLevelSave()).thenReturn(Optional.of(levelSave));
    when(levelSave.getWorldCells()).thenReturn(Arrays.asList(cell1, cell2));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    when(ship2.inGroup(ShipGroup.ENEMY)).thenReturn(true);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(textLoader.get(MenuBundle.HAS_PROGRESS.format(1, 1))).thenReturn("1 p 1 e");
    when(textLoader.get(MenuBundle.LEVEL_INFO.format(2, "1 p 1 e"))).thenReturn("success");
    when(textLoader.get(MenuBundle.WARNING)).thenReturn("title");
    when(textLoader.get(MenuBundle.RESET_LEVEL_WARNING)).thenReturn("dont do it");
    VisTextButton resetButton = startMenu.getRoot().findActor("resetLevelButton");
    assertThat(resetButton.getText().toString()).isEqualTo(RESET_LEVEL_BTN);

    resetButton.toggle();

    // first time is table root, second time is dialog
    verify(stage, times(2)).addActor(actorCaptor.capture());
    OptionDialog dialog = (OptionDialog) actorCaptor.getValue();
    // ¯\_(ツ)_/¯
    Table buttons = (Table) dialog.getCells().get(1).getActor();
    Button yesButton = (Button) buttons.getCells().get(0).getActor();
    yesButton.toggle();

    verify(dataManager).removeLevelProgress();
    VisLabel infoLabel = startMenu.getRoot().findActor("infoLabel");
    assertThat(infoLabel.getText().toString()).isEqualTo("success");
  }

  @Test
  public void clear_save_button() {
    when(dataManager.loadGameSave()).thenReturn(gameSave);
    when(dataManager.loadLevelSave()).thenReturn(Optional.of(levelSave));
    when(levelSave.getWorldCells()).thenReturn(Arrays.asList(cell1, cell2));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    when(ship2.inGroup(ShipGroup.ENEMY)).thenReturn(true);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(textLoader.get(MenuBundle.HAS_PROGRESS.format(1, 1))).thenReturn("1 p 1 e");
    when(textLoader.get(MenuBundle.LEVEL_INFO.format(2, "1 p 1 e"))).thenReturn("success");
    when(textLoader.get(MenuBundle.WARNING)).thenReturn("title");
    when(textLoader.get(MenuBundle.CLEAR_SAVE_WARNING)).thenReturn("dont do it");
    VisTextButton clearSaveButton = startMenu.getRoot().findActor("clearSaveButton");
    assertThat(clearSaveButton.getText().toString()).isEqualTo(CLEAR_SAVE_BTN);

    clearSaveButton.toggle();

    // first time is table root, second time is dialog
    verify(stage, times(2)).addActor(actorCaptor.capture());
    OptionDialog dialog = (OptionDialog) actorCaptor.getValue();
    // ¯\_(ツ)_/¯
    Table buttons = (Table) dialog.getCells().get(1).getActor();
    Button yesButton = (Button) buttons.getCells().get(0).getActor();
    yesButton.toggle();

    verify(dataManager).freshStart();
    VisLabel infoLabel = startMenu.getRoot().findActor("infoLabel");
    assertThat(infoLabel.getText().toString()).isEqualTo("success");
  }
}