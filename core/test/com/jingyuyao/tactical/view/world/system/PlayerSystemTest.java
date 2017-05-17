package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.resource.Colors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerSystemTest {

  @Mock
  private Ship player;

  private PooledEngine engine;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    PlayerSystem playerSystem =
        new PlayerSystem(
            ComponentMapper.getFor(PlayerComponent.class),
            ComponentMapper.getFor(Frame.class));
    assertThat(playerSystem.priority).isEqualTo(SystemPriority.PLAYER);
    engine.addSystem(playerSystem);
  }

  @Test
  public void update() {
    when(player.isControllable()).thenReturn(true, false);
    Entity entity = engine.createEntity();
    Frame frame = engine.createComponent(Frame.class);
    entity.add(frame);
    PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
    playerComponent.setPlayer(player);
    entity.add(playerComponent);
    engine.addEntity(entity);

    engine.update(0f);

    assertThat(frame.getColor()).isEqualTo(Colors.BLUE_300);

    engine.update(0f);

    assertThat(frame.getColor()).isEqualTo(Colors.GREY_500);
  }
}