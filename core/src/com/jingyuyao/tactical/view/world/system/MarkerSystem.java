package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Markers;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MarkerSystem extends EntitySystem {

  private final ECF ecf;
  private final Markers markers;
  private ImmutableArray<Entity> marked;
  private ImmutableArray<Entity> highlight;

  @Inject
  MarkerSystem(ECF ecf, Markers markers) {
    super(SystemPriority.MARKER);
    this.ecf = ecf;
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
      entity = ecf.entity();
      entity.add(ecf.frame(markers.getHighlight()));
      entity.add(ecf.component(Highlight.class));
    }

    Cell cell = selectCell.getObject();
    entity.add(ecf.position(cell.getCoordinate(), WorldZIndex.HIGHLIGHT_MARKER));
  }

  @Subscribe
  void moving(Moving moving) {
    for (Cell cell : moving.getMovement().getCells()) {
      mark(cell.getCoordinate(), WorldZIndex.MOVE_MARKER, markers.getMove());
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
      mark(cell.getCoordinate(), WorldZIndex.ATTACK_MARKER, markers.getAttack());
    }
    for (Cell cell : selectCells) {
      mark(cell.getCoordinate(), WorldZIndex.TARGET_SELECT_MARKER, markers.getTargetSelect());
    }
  }

  @Subscribe
  void battling(Battling battling) {
    Target target = battling.getTarget();
    for (Cell cell : target.getTargetCells()) {
      mark(cell.getCoordinate(), WorldZIndex.ATTACK_MARKER, markers.getAttack());
    }
    for (Cell cell : target.getSelectCells()) {
      mark(cell.getCoordinate(), WorldZIndex.TARGET_SELECT_MARKER, markers.getTargetSelect());
    }
  }

  @Subscribe
  void exitState(ExitState exitState) {
    for (Entity entity : marked) {
      entity.add(ecf.component(Remove.class));
    }
  }

  private void mark(Coordinate coordinate, int zIndex, WorldTexture worldTexture) {
    Entity entity = ecf.entity();
    entity.add(ecf.position(coordinate, zIndex));
    entity.add(ecf.frame(worldTexture));
    entity.add(ecf.component(Marked.class));
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
