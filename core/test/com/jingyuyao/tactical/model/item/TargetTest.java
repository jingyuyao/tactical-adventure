package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Marking;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TargetTest {

  private static final Coordinate COORDINATE = new Coordinate(10, 10);

  @Mock
  private ImmutableSet<Coordinate> selectCoordinates;
  @Mock
  private ImmutableSet<Coordinate> targetCoordinates;
  @Mock
  private Characters characters;
  @Mock
  private Terrains terrains;
  @Mock
  private Character character;
  @Mock
  private Terrain terrain;
  @Mock
  private Terrain terrain2;

  private Target target;

  @Before
  public void setUp() {
    target = new Target(selectCoordinates, targetCoordinates, characters, terrains);
  }

  @Test
  public void selected_by() {
    when(selectCoordinates.contains(COORDINATE)).thenReturn(true, false);

    assertThat(target.selectedBy(COORDINATE)).isTrue();
    assertThat(target.selectedBy(COORDINATE)).isFalse();
  }

  @Test
  public void get_target_characters() {
    when(characters.getAll(targetCoordinates)).thenReturn(ImmutableList.of(character));

    assertThat(target.getTargetCharacters()).containsExactly(character);
  }

  @Test
  public void show_marking() {
    when(terrains.getAll(targetCoordinates)).thenReturn(ImmutableList.of(terrain));
    when(terrains.getAll(selectCoordinates)).thenReturn(ImmutableList.of(terrain2));
    when(characters.getAll(targetCoordinates)).thenReturn(ImmutableList.of(character));

    target.showMarking();

    verify(terrain).addMarker(Marker.CAN_ATTACK);
    verify(terrain2).addMarker(Marker.TARGET_SELECT);
    verify(character).addMarker(Marker.POTENTIAL_TARGET);
  }

  @Test
  public void hide_marking() {
    show_marking();

    target.hideMarking();

    verify(terrain).removeMarker(Marker.CAN_ATTACK);
    verify(terrain2).removeMarker(Marker.TARGET_SELECT);
    verify(character).removeMarker(Marker.POTENTIAL_TARGET);
  }

  @Test
  public void create_hit_marking() {
    when(terrains.getAll(targetCoordinates)).thenReturn(ImmutableList.of(terrain));
    when(characters.getAll(targetCoordinates)).thenReturn(ImmutableList.of(character));

    Marking hitMarking = target.createHitMarking();
    hitMarking.apply();

    verify(terrain).addMarker(Marker.HIT);
    verify(character).addMarker(Marker.HIT);
  }
}