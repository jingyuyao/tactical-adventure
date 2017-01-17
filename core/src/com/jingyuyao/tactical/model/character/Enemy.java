package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.CharacterModule.DefaultRetaliation;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.retaliation.Retaliation;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import javax.inject.Inject;

/**
 * An enemy character
 */
public class Enemy extends Character {

  private final Retaliation retaliation;

  @Inject
  Enemy(
      EventBus eventBus,
      @Assisted Coordinate coordinate,
      @InitialMarkers List<Marker> markers,
      @Assisted String name,
      @Assisted Stats stats,
      @Assisted Items items,
      MovementFactory movementFactory,
      @DefaultRetaliation Retaliation retaliation) {
    super(eventBus, coordinate, markers, name, stats, items, movementFactory);
    this.retaliation = retaliation;
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  public ListenableFuture<Void> retaliate() {
    return retaliation.run(this);
  }
}
