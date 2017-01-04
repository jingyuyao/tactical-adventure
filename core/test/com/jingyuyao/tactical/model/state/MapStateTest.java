package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.event.HighlightCharacter;
import com.jingyuyao.tactical.model.state.event.HighlightTerrain;
import com.jingyuyao.tactical.model.state.event.StateChanged;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Deque;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MapStateTest {
    private final Coordinate CORD = new Coordinate(0, 0);

    @Mock private EventBus eventBus;
    @Mock private Deque<State> stateStack;
    @Mock private Terrains terrains;
    @Mock private State state1;
    @Mock private State state2;
    @Mock private State state3;
    @Mock private NewMap newMap;
    @Mock private ClearMap clearMap;
    @Mock private Player player;
    @Mock private Enemy enemy;
    @Mock private Terrain terrain;
    @Captor private ArgumentCaptor<Object> argumentCaptor;

    private MapState mapState;

    @Before
    public void setUp() {
        mapState = new MapState(eventBus, terrains, stateStack);
        verify(eventBus).register(mapState);
    }

    @Test
    public void subscribers() {
        when(newMap.getInitialState()).thenReturn(state1);

        TestHelpers.verifyNoDeadEvents(mapState, newMap, clearMap);
    }

    @Test
    public void initialize() throws Exception {
        when(newMap.getInitialState()).thenReturn(state1);

        mapState.initialize(newMap);

        verify(stateStack).push(state1);
        verify(state1).enter();
        verify(eventBus).post(argumentCaptor.capture());
        verifyStateChangedTo(argumentCaptor.getValue(), state1);
    }

    @Test
    public void dispose() throws Exception {
        mapState.dispose(clearMap);

        verify(stateStack).clear();
    }

    @Test
    public void selectPlayer() throws Exception {
        when(stateStack.peek()).thenReturn(state1);

        mapState.select(player);

        verify(state1).select(player);
    }

    @Test
    public void selectEnemy() throws Exception {
        when(stateStack.peek()).thenReturn(state1);

        mapState.select(enemy);

        verify(state1).select(enemy);
    }

    @Test
    public void selectTerrain() throws Exception {
        when(stateStack.peek()).thenReturn(state1);

        mapState.select(terrain);

        verify(state1).select(terrain);
    }

    @Test
    public void highlight() throws Exception {
        when(player.getCoordinate()).thenReturn(CORD);
        when(terrains.get(CORD)).thenReturn(terrain);

        mapState.highlight(player);
        mapState.highlight(terrain);

        InOrder inOrder = inOrder(player, terrain, eventBus);

        inOrder.verify(player).addMarker(Marker.HIGHLIGHT);
        inOrder.verify(eventBus).post(argumentCaptor.capture());
        HighlightCharacter highlightCharacter =
                TestHelpers.isInstanceOf(argumentCaptor.getValue(), HighlightCharacter.class);
        assertThat(highlightCharacter.getObject()).isEqualTo(player);
        assertThat(highlightCharacter.getTerrain()).isEqualTo(terrain);

        inOrder.verify(player).removeMarker(Marker.HIGHLIGHT);
        inOrder.verify(terrain).addMarker(Marker.HIGHLIGHT);
        inOrder.verify(eventBus).post(argumentCaptor.capture());
        HighlightTerrain highlightTerrain = TestHelpers.isInstanceOf(argumentCaptor.getValue(), HighlightTerrain.class);
        assertThat(highlightTerrain.getObject()).isEqualTo(terrain);
    }

    @Test
    public void push() throws Exception {
        when(stateStack.peek()).thenReturn(state1);

        mapState.push(state2);

        verify(state1).exit();
        verify(stateStack).push(state2);
        verify(state2).enter();
        verify(eventBus).post(argumentCaptor.capture());

        verifyStateChangedTo(argumentCaptor.getValue(), state2);
    }

    @Test
    public void pop_nothing() throws Exception {
        when(stateStack.size()).thenReturn(1);

        mapState.pop();

        verify(stateStack).size();
        verifyNoMoreInteractions(stateStack);
        verifyZeroInteractions(eventBus);
    }

    @Test
    public void pop() throws Exception {
        when(stateStack.size()).thenReturn(2);
        when(stateStack.pop()).thenReturn(state2);
        when(stateStack.peek()).thenReturn(state1);

        mapState.pop();

        InOrder inOrder = inOrder(stateStack, state2, state1, eventBus);

        inOrder.verify(stateStack).pop();
        inOrder.verify(state2).exit();
        inOrder.verify(stateStack).peek();
        inOrder.verify(state1).canceled();
        inOrder.verify(state1).enter();
        inOrder.verify(eventBus).post(argumentCaptor.capture());
        verifyStateChangedTo(argumentCaptor.getValue(), state1);
    }

    @Test
    public void rollback_nothing() throws Exception {
        when(stateStack.size()).thenReturn(1);

        mapState.rollback();

        verify(stateStack).size();
        verifyNoMoreInteractions(stateStack);
        verifyZeroInteractions(eventBus);
    }

    @Test
    public void rollback() throws Exception {
        when(stateStack.size())
                .thenReturn(3)
                .thenReturn(2)
                .thenReturn(1);
        when(stateStack.pop())
                .thenReturn(state3)
                .thenReturn(state2);
        when(stateStack.peek())
                .thenReturn(state2)
                .thenReturn(state1);

        mapState.rollback();

        InOrder inOrder = inOrder(stateStack, state3, state2, state1, eventBus);

        // oh boy...
        inOrder.verify(stateStack).pop();
        inOrder.verify(state3).exit();
        inOrder.verify(stateStack).peek();
        inOrder.verify(state2).canceled();
        inOrder.verify(state2).enter();
        inOrder.verify(eventBus).post(argumentCaptor.capture());
        verifyStateChangedTo(argumentCaptor.getValue(), state2);

        inOrder.verify(stateStack).pop();
        inOrder.verify(state2).exit();
        inOrder.verify(stateStack).peek();
        inOrder.verify(state1).canceled();
        inOrder.verify(state1).enter();
        inOrder.verify(eventBus).post(argumentCaptor.capture());
        verifyStateChangedTo(argumentCaptor.getValue(), state1);
    }

    @Test
    public void newStack() throws Exception {
        when(stateStack.peek()).thenReturn(state2);

        mapState.newStack(state1);

        InOrder inOrder = inOrder(stateStack, state1, state2, eventBus);

        inOrder.verify(stateStack).peek();
        inOrder.verify(state2).exit();
        inOrder.verify(stateStack).clear();
        inOrder.verify(stateStack).push(state1);
        inOrder.verify(state1).enter();
        inOrder.verify(eventBus).post(argumentCaptor.capture());
        verifyStateChangedTo(argumentCaptor.getValue(), state1);
    }

    private void verifyStateChangedTo(Object event, State targetState) {
        StateChanged stateChanged = TestHelpers.isInstanceOf(event, StateChanged.class);
        assertThat(stateChanged.getObject()).isEqualTo(targetState);
    }
}