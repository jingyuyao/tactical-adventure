package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.battle.Battle2;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EffectsSystemTest {

  private static final Coordinate C1 = new Coordinate(5, 5);

  @Mock
  private Animations animations;
  @Mock
  private Battle2 battle2;
  @Mock
  private Target target;
  @Mock
  private Weapon weapon;
  @Mock
  private Cell cell;
  @Mock
  private WorldTexture worldTexture;

  private PooledEngine engine;
  private EffectsSystem effectsSystem;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    effectsSystem = new EffectsSystem(animations);
    assertThat(effectsSystem.priority).isEqualTo(SystemPriority.EFFECTS);
    engine.addSystem(effectsSystem);
  }

  @Test
  public void battle_no_direction() {
    SingleAnimation animation = new SingleAnimation(10, new WorldTexture[]{worldTexture});

    when(target.getOrigin()).thenReturn(cell);
    when(target.getDirection()).thenReturn(Optional.<Direction>absent());
    when(cell.getCoordinate()).thenReturn(C1);
    when(weapon.getName()).thenReturn("titan");
    when(animations.getWeapon("titan")).thenReturn(animation);
    when(battle2.getWeapon()).thenReturn(weapon);
    when(battle2.getTarget()).thenReturn(target);

    effectsSystem.battle2(battle2);

    verify(battle2, never()).execute();
    assertThat(engine.getEntities()).hasSize(1);
    Entity entity = engine.getEntities().first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getDirection()).isAbsent();
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C1.getX());
    assertThat(position.getY()).isEqualTo((float) C1.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.EFFECTS);
    assertThat(entity.getComponent(SingleAnimation.class)).isSameAs(animation);

    animation.advanceTime(100);

    verify(battle2).execute();
  }

  @Test
  public void battle_direction() {
    SingleAnimation animation = new SingleAnimation(10, new WorldTexture[]{worldTexture});

    when(target.getOrigin()).thenReturn(cell);
    when(target.getDirection()).thenReturn(Optional.of(Direction.DOWN));
    when(cell.getCoordinate()).thenReturn(C1);
    when(weapon.getName()).thenReturn("titan");
    when(animations.getWeapon("titan")).thenReturn(animation);
    when(battle2.getWeapon()).thenReturn(weapon);
    when(battle2.getTarget()).thenReturn(target);

    effectsSystem.battle2(battle2);

    verify(battle2, never()).execute();
    assertThat(engine.getEntities()).hasSize(1);
    Entity entity = engine.getEntities().first();
    Frame frame = entity.getComponent(Frame.class);
    assertThat(frame).isNotNull();
    assertThat(frame.getDirection()).hasValue(Direction.DOWN);
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C1.getX());
    assertThat(position.getY()).isEqualTo((float) C1.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.EFFECTS);
    assertThat(entity.getComponent(SingleAnimation.class)).isSameAs(animation);

    animation.advanceTime(100);

    verify(battle2).execute();
  }
}