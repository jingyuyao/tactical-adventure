package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;
import javax.inject.Provider;

public class ActorFactory {

  private final ActorConfig actorConfig;
  private final ControllerFactory controllerFactory;
  private final Animations animations;
  private final Provider<LinkedHashSet<WorldTexture>> markersProvider;

  @Inject
  ActorFactory(
      ActorConfig actorConfig,
      ControllerFactory controllerFactory,
      Animations animations,
      @InitialMarkers Provider<LinkedHashSet<WorldTexture>> markersProvider) {
    this.actorConfig = actorConfig;
    this.controllerFactory = controllerFactory;
    this.animations = animations;
    this.markersProvider = markersProvider;
  }

  public Actor create(Cell cell) {
    Actor actor = new Actor();
    setSizeAndPosition(cell.getCoordinate(), actor);
    actor.addListener(controllerFactory.create(cell));
    return actor;
  }

  public WorldActor create(Coordinate initialCoordinate) {
    WorldActor actor = new WorldActor(markersProvider.get());
    setSizeAndPosition(initialCoordinate, actor);
    return actor;
  }

  public CharacterActor create(Player player, Coordinate initialCoordinate) {
    CharacterActor actor =
        new PlayerActor(
            player, actorConfig.getMoveTimePerUnit(), markersProvider.get(), getAnimation(player));
    setSizeAndPosition(initialCoordinate, actor);
    return actor;
  }

  public CharacterActor create(Enemy enemy, Coordinate initialCoordinate) {
    CharacterActor actor =
        new CharacterActor(
            actorConfig.getMoveTimePerUnit(), markersProvider.get(), getAnimation(enemy));
    setSizeAndPosition(initialCoordinate, actor);
    actor.setColor(Color.RED);
    return actor;
  }

  private void setSizeAndPosition(Coordinate coordinate, Actor actor) {
    float size = actorConfig.getActorWorldSize();
    actor.setSize(size, size);
    actor.setPosition(coordinate.getX() * size, coordinate.getY() * size);
  }

  private LoopAnimation getAnimation(Character character) {
    return animations.getCharacter(character.getName());
  }
}
