package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.resource.Colors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class PlayerSystem extends IteratingSystem {

  private final ComponentMapper<PlayerComponent> playerMapper;
  private final ComponentMapper<Frame> frameMapper;

  @Inject
  public PlayerSystem(
      ComponentMapper<PlayerComponent> playerMapper,
      ComponentMapper<Frame> frameMapper) {
    super(Family.all(PlayerComponent.class, Frame.class).get());
    this.playerMapper = playerMapper;
    this.frameMapper = frameMapper;
    this.priority = SystemPriority.PLAYER;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PlayerComponent playerComponent = playerMapper.get(entity);
    Frame frame = frameMapper.get(entity);
    frame.setColor(playerComponent.getPlayer().isActionable() ? Colors.BLUE_300 : Colors.GREY_500);
  }
}
