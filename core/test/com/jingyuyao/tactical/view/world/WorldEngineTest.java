package com.jingyuyao.tactical.view.world;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldEngineTest {

  @Mock
  private Engine engine;
  @Mock
  private EntitySystem system1;
  @Mock
  private EntitySystem system2;
  @Mock
  private EventBus eventBus;

  private WorldEngine worldEngine;

  @Before
  public void setUp() {
    worldEngine = new WorldEngine(engine, ImmutableList.of(system1, system2));
    verify(engine).addSystem(system1);
    verify(engine).addSystem(system2);
  }

  @Test
  public void register() {
    when(engine.getSystems())
        .thenReturn(new ImmutableArray<>(new Array<>(new EntitySystem[]{system1, system2})));

    worldEngine.register(eventBus);

    verify(eventBus).register(system1);
    verify(eventBus).register(system2);
  }

  @Test
  public void update() {
    worldEngine.update(0.7f);

    verify(engine).update(0.7f);
  }
}