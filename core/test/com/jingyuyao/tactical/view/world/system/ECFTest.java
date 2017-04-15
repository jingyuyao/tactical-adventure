package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.CharacterComponent;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ECFTest {

  @Mock
  private WorldTexture texture;
  @Mock
  private Character character;
  @Mock
  private Player player;

  private PooledEngine engine;
  private ECF ecf;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    ecf = new ECF(engine);
  }

  @Test
  public void entity() {
    assertThat(engine.getEntities()).containsExactly(ecf.entity());
  }

  @Test
  public void component() {
    Remove remove = ecf.component(Remove.class);
    Remove remove2 = ecf.component(Remove.class);
    assertThat(remove).isNotSameAs(remove2);
  }

  @Test
  public void position() {
    Coordinate coordinate = new Coordinate(3, 4);

    Position position = ecf.position(coordinate, 10);

    assertThat(position.getX()).isEqualTo(3f);
    assertThat(position.getY()).isEqualTo(4f);
    assertThat(position.getZ()).isEqualTo(10);
  }

  @Test
  public void frame_color() {
    Frame frame = ecf.frame(Color.NAVY);

    assertThat(frame.getColor()).isEqualTo(Color.NAVY);
  }

  @Test
  public void frame_texture() {
    Frame frame = ecf.frame(texture);

    assertThat(frame.getTexture()).hasValue(texture);
  }

  @Test
  public void character() {
    CharacterComponent characterComponent = ecf.character(character);

    assertThat(characterComponent.getCharacter()).isEqualTo(character);
  }

  @Test
  public void player() {
    PlayerComponent playerComponent = ecf.player(player);

    assertThat(playerComponent.getPlayer()).isEqualTo(player);
  }
}