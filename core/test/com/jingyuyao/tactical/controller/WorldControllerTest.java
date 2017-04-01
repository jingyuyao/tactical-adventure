package com.jingyuyao.tactical.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldControllerTest {

  @Mock
  private Input input;
  @Mock
  private Stage worldStage;
  @Mock
  private Stage uiStage;
  @Mock
  private WorldCamera worldCamera;
  @Captor
  private ArgumentCaptor<InputProcessor> inputProcessorCaptor;

  private WorldController worldController;

  @Before
  public void setUp() {
    worldController = new WorldController(input, worldStage, uiStage, worldCamera);
  }

  @Test
  public void receive_input() {
    worldController.receiveInput();

    verify(input).setInputProcessor(inputProcessorCaptor.capture());
    assertThat(inputProcessorCaptor.getValue()).isInstanceOf(InputMultiplexer.class);
    InputMultiplexer multiplexer = (InputMultiplexer) inputProcessorCaptor.getValue();
    Array<InputProcessor> processors = multiplexer.getProcessors();
    assertThat(processors).containsExactly(uiStage, worldCamera, worldStage).inOrder();
  }

  @Test
  public void stop_receiving_input() {
    worldController.stopReceivingInput();

    verify(input).setInputProcessor(null);
  }
}