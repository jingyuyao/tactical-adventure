package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Markers;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class MarkerSystem extends EntitySystem {

  private final Markers markers;
  private ImmutableArray<Entity> marked;
  private ImmutableArray<Entity> highlight;

  @Inject
  MarkerSystem(Markers markers) {
    super(SystemPriority.MARKER);
    this.markers = markers;
  }

  @Override
  public void addedToEngine(Engine engine) {
    marked = engine.getEntitiesFor(Family.all(Marked.class).get());
    highlight = engine.getEntitiesFor(Family.all(Highlight.class).get());
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    Entity entity;

    // only one active highlight entity at a time
    if (highlight.size() > 0) {
      entity = highlight.first();
    } else {
      entity = getEngine().createEntity();

      Frame frame = getEngine().createComponent(Frame.class);
      frame.setTexture(markers.getHighlight());

      entity.add(frame);
      entity.add(getEngine().createComponent(Highlight.class));

      getEngine().addEntity(entity);
    }

    Cell cell = selectCell.getObject();
    Position position = getEngine().createComponent(Position.class);
    position.set(cell.getCoordinate(), WorldZIndex.HIGHLIGHT_MARKER);

    entity.add(position);
  }

  @Subscribe
  void moving(Moving moving) {
    for (Cell cell : moving.getMovement().getCells()) {
      mark(cell, WorldZIndex.MOVE_MARKER, markers.getMove());
    }
  }

  @Subscribe
  void selectingTarget(SelectingTarget selectingTarget) {
    Set<Cell> targetCells = new HashSet<>();
    Set<Cell> selectCells = new HashSet<>();
    for (Target target : selectingTarget.getTargets()) {
      targetCells.addAll(target.getTargetCells());
      selectCells.addAll(target.getSelectCells());
    }
    for (Cell cell : targetCells) {
      mark(cell, WorldZIndex.ATTACK_MARKER, markers.getAttack());
    }
    for (Cell cell : selectCells) {
      mark(cell, WorldZIndex.TARGET_SELECT_MARKER, markers.getTargetSelect());
    }
  }

  @Subscribe
  void battling(Battling battling) {
    Target target = battling.getTarget();
    for (Cell cell : target.getTargetCells()) {
      mark(cell, WorldZIndex.ATTACK_MARKER, markers.getAttack());
    }
    for (Cell cell : target.getSelectCells()) {
      mark(cell, WorldZIndex.TARGET_SELECT_MARKER, markers.getTargetSelect());
    }
  }

  @Subscribe
  void exitState(ExitState exitState) {
    for (Entity entity : marked) {
      entity.add(getEngine().createComponent(Remove.class));
    }
  }

  private void mark(Cell cell, int zIndex, WorldTexture worldTexture) {
    Entity entity = getEngine().createEntity();

    Position position = getEngine().createComponent(Position.class);
    position.set(cell.getCoordinate(), zIndex);

    Frame frame = getEngine().createComponent(Frame.class);
    frame.setTexture(worldTexture);

    entity.add(position);
    entity.add(frame);
    entity.add(getEngine().createComponent(Marked.class));

    getEngine().addEntity(entity);
  }

  private static class Marked implements Component, Poolable {

    @Override
    public void reset() {

    }
  }

  private static class Highlight implements Component, Poolable {

    @Override
    public void reset() {

    }
  }
}
