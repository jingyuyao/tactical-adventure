package com.jingyuyao.tactical.model;

import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

  @Mock
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private ModelBus modelBus;
  @Mock
  private Cell cell;
  @Mock
  private Turn turn;
  @Mock
  private Script script;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Model model;

  @Before
  public void setUp() {
    model = new Model(world, worldState, modelBus);
  }

  @Test
  public void initialize() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Character> characterMap = new HashMap<>();

    model.initialize(terrainMap, characterMap, turn, script);

    verify(world).initialize(terrainMap, characterMap);
    verify(worldState).initialize(turn, script);
  }

  @Test
  public void prep_for_save() {
    model.prepForSave();

    verify(worldState).prepForSave();
  }

  @Test
  public void reset() {
    model.reset();

    verify(world).reset();
    verify(worldState).reset();
  }

  @Test
  public void select() {
    model.select(cell);

    verify(worldState).select(cell);
    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SelectCell.class);
  }
}