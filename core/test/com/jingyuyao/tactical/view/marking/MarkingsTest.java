package com.jingyuyao.tactical.view.marking;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.MarkerSprites;
import com.jingyuyao.tactical.view.world.World;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingsTest {

  @Mock
  private Batch batch;
  @Mock
  private World world;
  @Mock
  private MarkerSprites markerSprites;
  @Mock
  private Actor actionActor;
  @Mock
  private List<WorldActor<?>> markedActors;
  @Mock
  private MapObject mapObject;
  @Mock
  private MapObject mapObject2;
  @Mock
  private WorldActor<?> highlightActor;
  @Mock
  private WorldActor<?> activatedActor;
  @Mock
  private Sprite highlightSprite;
  @Mock
  private Sprite activatedSprite;
  @Mock
  private Action action;

  private Markings markings;

  @Before
  public void setUp() {
    markings = new Markings(batch, world, markerSprites, actionActor, markedActors);
  }

  @Test
  public void act() {
    markings.act(10f);

    verify(actionActor).act(10f);
  }

  @Test
  public void add_action() {
    markings.addAction(action);

    verify(actionActor).addAction(action);
  }

  @Test
  public void draw_nothing() {
    markings.draw();

    verify(batch).begin();
    verify(batch).end();
    verifyZeroInteractions(markerSprites);
  }

  @Test
  public void draw_highlight() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(highlightActor);
    when(markerSprites.getHighlight()).thenReturn(highlightSprite);

    markings.highlight(mapObject);
    markings.draw();

    InOrder inOrder = Mockito.inOrder(batch, highlightSprite);
    inOrder.verify(batch).begin();
    inOrder.verify(highlightSprite).draw(batch);
    inOrder.verify(batch).end();
    verifyZeroInteractions(activatedSprite);
  }

  @Test
  public void draw_activated() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(activatedActor);
    when(markerSprites.getActivated()).thenReturn(activatedSprite);

    markings.activate(mapObject);
    markings.draw();

    InOrder inOrder = Mockito.inOrder(batch, activatedSprite);
    inOrder.verify(batch).begin();
    inOrder.verify(activatedSprite).draw(batch);
    inOrder.verify(batch).end();
    verifyZeroInteractions(highlightSprite);
  }

  @Test
  public void draw_highlight_and_activate() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(highlightActor);
    Mockito.<WorldActor<?>>when(world.get(mapObject2)).thenReturn(activatedActor);
    when(markerSprites.getHighlight()).thenReturn(highlightSprite);
    when(markerSprites.getActivated()).thenReturn(activatedSprite);

    markings.highlight(mapObject);
    markings.activate(mapObject2);
    markings.draw();

    InOrder inOrder = Mockito.inOrder(batch, highlightSprite, activatedSprite);
    inOrder.verify(batch).begin();
    inOrder.verify(highlightSprite).draw(batch);
    inOrder.verify(activatedSprite).draw(batch);
    inOrder.verify(batch).end();
  }

  @Test
  public void mark() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(highlightActor);

    markings.mark(mapObject, highlightSprite);

    verify(highlightActor).addMarker(highlightSprite);
    verify(markedActors).add(highlightActor);
  }

  @Test
  public void clear_marked() {
    when(markedActors.iterator())
        .thenReturn(ImmutableList.of(highlightActor, activatedActor).iterator());

    markings.clearMarked();

    verify(highlightActor).clearMarkers();
    verify(activatedActor).clearMarkers();
    verify(markedActors).clear();
  }
}