package com.jingyuyao.tactical.view.world;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.AddEnemy;
import com.jingyuyao.tactical.model.event.AddPlayer;
import com.jingyuyao.tactical.model.event.AddTerrain;
import com.jingyuyao.tactical.model.event.RemoveObject;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldSubscriberTest {

  @Mock
  private WorldView worldView;
  @Mock
  private AddTerrain addTerrain;
  @Mock
  private AddPlayer addPlayer;
  @Mock
  private AddEnemy addEnemy;
  @Mock
  private RemoveObject removeObject;
  @Mock
  private Terrain terrain;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private MapObject mapObject;

  private WorldSubscriber subscriber;

  @Before
  public void setUp() {
    subscriber = new WorldSubscriber(worldView);
  }

  @Test
  public void add_terrain() {
    when(addTerrain.getObject()).thenReturn(terrain);

    subscriber.addTerrain(addTerrain);

    verify(worldView).add(terrain);
  }

  @Test
  public void add_player() {
    when(addPlayer.getObject()).thenReturn(player);

    subscriber.addPlayer(addPlayer);

    verify(worldView).add(player);
  }

  @Test
  public void add_enemy() {
    when(addEnemy.getObject()).thenReturn(enemy);

    subscriber.addEnemy(addEnemy);

    verify(worldView).add(enemy);
  }

  @Test
  public void remove_object() {
    when(removeObject.getObject()).thenReturn(mapObject);

    subscriber.removeObject(removeObject);

    verify(worldView).remove(mapObject);
  }
}