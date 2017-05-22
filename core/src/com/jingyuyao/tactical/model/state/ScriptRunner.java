package com.jingyuyao.tactical.model.state;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multimaps;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.GroupActivation;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptEvent;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ScriptRunner {

  private final ModelBus modelBus;
  private final World world;

  @Inject
  ScriptRunner(ModelBus modelBus, World world) {
    this.modelBus = modelBus;
    this.world = world;
  }

  /**
   * Trigger any scripts for the given {@link ScriptEvent} and {@link Script}.
   *
   * @return a promise that is completed when the scripts finishes executing
   */
  Promise triggerScripts(final ScriptEvent event, final Script script) {
    final Promise promise = new Promise();
    triggerDialogues(event, script).done(new Runnable() {
      @Override
      public void run() {
        triggerGroupActivations(event, script);
        triggerWinLose(event, script, promise);
      }
    });
    return promise;
  }

  /**
   * Trigger all dialogue for the given event, if any.
   *
   * @return a promise that is resolved when all dialogues finishes diplaying
   */
  private Promise triggerDialogues(ScriptEvent event, Script script) {
    Promise promise = new Promise();
    triggerDialogues(event, Multimaps.asMap(script.getDialogues()).entrySet().iterator(), promise);
    return promise;
  }

  private void triggerDialogues(
      final ScriptEvent event,
      final Iterator<Entry<Condition, List<Dialogue>>> entryIterator,
      final Promise promise) {
    if (entryIterator.hasNext()) {
      Entry<Condition, List<Dialogue>> entry = entryIterator.next();
      final Condition condition = entry.getKey();
      if (event.satisfiedBy(condition)) {
        List<Dialogue> dialogues = entry.getValue();
        modelBus.post(new ShowDialogues(dialogues, new Promise(new Runnable() {
          @Override
          public void run() {
            triggerDialogues(event, entryIterator, promise);
          }
        })));
      } else {
        triggerDialogues(event, entryIterator, promise);
      }
    } else {
      promise.complete();
    }
  }

  private void triggerGroupActivations(ScriptEvent event, Script script) {
    for (Entry<Condition, GroupActivation> entry : script.getGroupActivations().entrySet()) {
      Condition condition = entry.getKey();
      if (event.satisfiedBy(condition)) {
        GroupActivation groupActivation = entry.getValue();
        final ShipGroup group = groupActivation.getGroup();
        List<Ship> ships = FluentIterable
            .from(world.getInactiveShips())
            .filter(new Predicate<Ship>() {
              @Override
              public boolean apply(Ship ship) {
                return ship.inGroup(group);
              }
            }).toList();
        List<Coordinate> spawns = groupActivation.getSpawns();
        for (int i = 0; i < spawns.size() && i < ships.size(); i++) {
          world.activateShip(spawns.get(i), ships.get(i));
        }
      }
    }
  }

  /**
   * Fires any win/lose conditions trigger by the event. Resolves the promise if the event does not
   * trigger any win/lose conditions.
   */
  private void triggerWinLose(ScriptEvent event, Script script, Promise promise) {
    // lose condition goes first so YOLOs are not encouraged
    for (Condition condition : script.getLoseConditions()) {
      if (event.satisfiedBy(condition)) {
        modelBus.post(new LevelLost());
        return;
      }
    }
    for (Condition condition : script.getWinConditions()) {
      if (event.satisfiedBy(condition)) {
        modelBus.post(new LevelWon());
        return;
      }
    }
    promise.complete();
  }
}
