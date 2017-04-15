package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
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
  private Attack attack;
  @Mock
  private Target target;
  @Mock
  private Weapon weapon;
  @Mock
  private Cell cell;
  @Mock
  private MyFuture myFuture;
  @Mock
  private WorldTexture worldTexture;

  private PooledEngine engine;
  private EffectsSystem effectsSystem;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    effectsSystem = new EffectsSystem(new ECF(engine), animations);
    assertThat(effectsSystem.priority).isEqualTo(SystemPriority.EFFECTS);
    engine.addSystem(effectsSystem);
  }

  @Test
  public void attack() {
    SingleAnimation animation = new SingleAnimation(10, new WorldTexture[]{worldTexture});

    when(attack.getObject()).thenReturn(target);
    when(target.getSelectCells()).thenReturn(ImmutableSet.of(cell));
    when(cell.getCoordinate()).thenReturn(C1);
    when(attack.getWeapon()).thenReturn(weapon);
    when(weapon.getName()).thenReturn("titan");
    when(animations.getWeapon("titan")).thenReturn(animation);
    when(attack.getFuture()).thenReturn(myFuture);

    effectsSystem.attack(attack);

    verify(myFuture).completedBy(animation.getFuture());
    assertThat(engine.getEntities()).hasSize(1);
    Entity entity = engine.getEntities().first();
    assertThat(entity.getComponent(Frame.class)).isNotNull();
    Position position = entity.getComponent(Position.class);
    assertThat(position).isNotNull();
    assertThat(position.getX()).isEqualTo((float) C1.getX());
    assertThat(position.getY()).isEqualTo((float) C1.getY());
    assertThat(position.getZ()).isEqualTo(WorldZIndex.EFFECTS);
    assertThat(entity.getComponent(SingleAnimation.class)).isSameAs(animation);
  }
}