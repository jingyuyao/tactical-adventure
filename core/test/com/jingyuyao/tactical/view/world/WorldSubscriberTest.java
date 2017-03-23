package com.jingyuyao.tactical.view.world;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.event.RemoveObject;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.map.Cell;
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
  private WorldLoad worldLoad;
  @Mock
  private WorldReset worldReset;
  @Mock
  private RemoveObject removeObject;
  @Mock
  private Object object;
  @Mock
  private Cell cell;

  private WorldSubscriber subscriber;

  @Before
  public void setUp() {
    subscriber = new WorldSubscriber(worldView);
  }

  @Test
  public void world_load() {
    when(worldLoad.getObject()).thenReturn(ImmutableList.of(cell));

    subscriber.worldLoad(worldLoad);

    verify(worldView).add(cell);
  }

  @Test
  public void world_reset() {
    subscriber.worldReset(worldReset);

    verify(worldView).reset();
  }

  @Test
  public void remove_object() {
    when(removeObject.getObject()).thenReturn(object);

    subscriber.removeObject(removeObject);

    verify(worldView).remove(object);
  }
}