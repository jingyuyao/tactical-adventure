package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.view.world.WorldCamera;
import com.jingyuyao.tactical.view.world.WorldConfig;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.ShipComponent;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Markers;
import com.jingyuyao.tactical.view.world.resource.TileSets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemModuleTest {

  @Bind
  @Mock
  private Batch batch;
  @Bind
  @Mock
  private WorldConfig worldConfig;
  @Bind
  @Mock
  private WorldCamera worldCamera;
  @Bind
  @Mock
  private Animations animations;
  @Bind
  @Mock
  private Markers markers;
  @Bind
  @Mock
  private TileSets tileSets;
  @Bind
  private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
  @Bind
  private ComponentMapper<Frame> frameMapper = ComponentMapper.getFor(Frame.class);
  @Bind
  private ComponentMapper<LoopAnimation> loopMapper = ComponentMapper.getFor(LoopAnimation.class);
  @Bind
  private ComponentMapper<SingleAnimation> singleMapper =
      ComponentMapper.getFor(SingleAnimation.class);
  @Bind
  private ComponentMapper<Moving> movingMapper = ComponentMapper.getFor(Moving.class);
  @Bind
  private ComponentMapper<ShipComponent> shipMapper = ComponentMapper.getFor(ShipComponent.class);

  @Inject
  private Systems systems;
  @Inject
  private AnimationSystem animationSystem;
  @Inject
  private ShipSystem shipSystem;
  @Inject
  private TerrainSystem terrainSystem;
  @Inject
  private EffectsSystem effectsSystem;
  @Inject
  private MarkerSystem markerSystem;
  @Inject
  private MovingSystem movingSystem;
  @Inject
  private RemoveSystem removeSystem;
  @Inject
  private RenderSystem renderSystem;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new SystemModule()).injectMembers(this);
  }
}