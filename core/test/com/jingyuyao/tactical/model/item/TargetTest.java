package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
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
  private MarkingFactory markingFactory;
  @Mock
  private Character character;
  @Mock
  private Terrain terrain;
  @Mock
  private Marking marking;
  @Captor
  private ArgumentCaptor<Map<MapObject, Marker>> markerMapCaptor;

  private Target target;

  @Before
  public void setUp() {
    target = new Target(selectCoordinates, targetCoordinates, characters, terrains, markingFactory);
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
    when(characters.getAll(targetCoordinates)).thenReturn(ImmutableList.of(character));
    when(markingFactory.create(Mockito.<Map<MapObject, Marker>>any())).thenReturn(marking);

    target.showMarking();

    verify(markingFactory).create(markerMapCaptor.capture());
    verify(marking).apply();
    assertThat(markerMapCaptor.getValue())
        .containsExactly(terrain, Marker.CAN_ATTACK, character, Marker.POTENTIAL_TARGET);
  }

  @Test
  public void hide_marking() {
    show_marking();

    target.hideMarking();

    verify(marking).clear();
  }
}