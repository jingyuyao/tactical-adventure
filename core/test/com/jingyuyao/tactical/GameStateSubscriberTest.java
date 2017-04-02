package com.jingyuyao.tactical;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import com.jingyuyao.tactical.view.WorldScreenSubscriber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameStateSubscriberTest {

  @Mock
  private GameState gameState;
  @Mock
  private WorldScreenSubscriber worldScreenSubscriber;
  @Mock
  private Application application;
  @Mock
  private LevelComplete levelComplete;
  @Mock
  private LevelFailed levelFailed;
  @Mock
  private EventBus eventBus;
  @Mock
  private DeadEvent deadEvent;

  private GameStateSubscriber gameStateSubscriber;

  @Before
  public void setUp() {
    gameStateSubscriber = new GameStateSubscriber(gameState, worldScreenSubscriber, application);
  }

  @Test
  public void register() {
    gameStateSubscriber.register(eventBus);

    verify(eventBus).register(gameStateSubscriber);
    verify(worldScreenSubscriber).register(eventBus);
  }

  @Test
  public void level_complete() {
    gameStateSubscriber.levelComplete(levelComplete);

    verify(gameState).advanceLevel();
  }

  @Test
  public void level_failed() {
    gameStateSubscriber.levelFailed(levelFailed);

    verify(gameState).replayLevel();
  }

  @Test
  public void dead_event() {
    Object object = new Object();
    when(deadEvent.getEvent()).thenReturn(object);

    gameStateSubscriber.deadEvent(deadEvent);

    verify(application).log(anyString(), eq(object.toString()));
  }
}