package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.Map;
import javax.inject.Inject;

class MapGroup<K, A extends Actor> {

  private final Map<K, A> map;
  private final Group group;

  @Inject
  MapGroup(Map<K, A> map, Group group) {
    this.map = map;
    this.group = group;
  }

  void addToStage(Stage stage) {
    stage.addActor(group);
  }

  void add(K key, A actor) {
    group.addActor(actor);
    map.put(key, actor);
  }

  A get(K key) {
    return map.get(key);
  }

  void remove(K key) {
    group.removeActor(map.remove(key));
  }

  void clear() {
    group.clear();
    map.clear();
  }
}
