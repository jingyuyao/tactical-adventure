package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.collect.Multimap;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.marking.MarkingModule.Attack;
import com.jingyuyao.tactical.view.marking.MarkingModule.BackingMap;
import com.jingyuyao.tactical.view.marking.MarkingModule.Hit;
import com.jingyuyao.tactical.view.marking.MarkingModule.Move;
import com.jingyuyao.tactical.view.marking.MarkingModule.TargetSelect;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class MarkingFactory {

  private final Provider<Multimap<MapObject, Sprite>> backingMapProvider;
  private final Sprite moveSprite;
  private final Sprite attackSprite;
  private final Sprite targetSelectSprite;
  private final Sprite hitSprite;

  @Inject
  MarkingFactory(
      @BackingMap Provider<Multimap<MapObject, Sprite>> backingMapProvider,
      @Move Sprite moveSprite,
      @Attack Sprite attackSprite,
      @TargetSelect Sprite targetSelectSprite,
      @Hit Sprite hitSprite) {
    this.backingMapProvider = backingMapProvider;
    this.moveSprite = moveSprite;
    this.attackSprite = attackSprite;
    this.targetSelectSprite = targetSelectSprite;
    this.hitSprite = hitSprite;
  }

  public Multimap<MapObject, Sprite> createMovement(Movement movement) {
    Multimap<MapObject, Sprite> multimap = backingMapProvider.get();
    for (Terrain terrain : movement.getTerrains()) {
      multimap.put(terrain, moveSprite);
    }
    return multimap;
  }

  public Multimap<MapObject, Sprite> createTarget(Target target) {
    Multimap<MapObject, Sprite> multimap = backingMapProvider.get();
    for (Terrain terrain : target.getTargetTerrains()) {
      multimap.put(terrain, attackSprite);
    }
    for (Terrain terrain : target.getSelectTerrains()) {
      multimap.put(terrain, targetSelectSprite);
    }
    return multimap;
  }

  public Multimap<MapObject, Sprite> createHit(Target target) {
    Multimap<MapObject, Sprite> multimap = backingMapProvider.get();
    for (MapObject object : target.getHitObjects()) {
      multimap.put(object, hitSprite);
    }
    return multimap;
  }
}
