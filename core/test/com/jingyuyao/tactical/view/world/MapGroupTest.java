package com.jingyuyao.tactical.view.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapGroupTest {

  @Mock
  private Group group;
  @Mock
  private Stage stage;
  @Mock
  private Actor actor;

  private Map<Object, Actor> map;
  private MapGroup<Object, Actor> mapGroup;

  @Before
  public void setUp() {
    map = new HashMap<>();
    mapGroup = new MapGroup<>(map, group);
  }

  @Test
  public void add_to_stage() {
    mapGroup.addToStage(stage);

    verify(stage).addActor(group);
  }

  @Test
  public void add() {
    Object object = new Object();

    mapGroup.add(object, actor);

    assertThat(map).containsExactly(object, actor);
    verify(group).addActor(actor);
  }

  @Test
  public void get() {
    Object object = new Object();
    map.put(object, actor);

    assertThat(mapGroup.get(object)).isSameAs(actor);
  }

  @Test
  public void remove() {
    Object object = new Object();
    map.put(object, actor);

    mapGroup.remove(object);

    assertThat(map).isEmpty();
    verify(group).removeActor(actor);
  }

  @Test
  public void clear() {
    Object object = new Object();
    map.put(object, actor);

    mapGroup.clear();

    assertThat(map).isEmpty();
    verify(group).clear();
  }
}