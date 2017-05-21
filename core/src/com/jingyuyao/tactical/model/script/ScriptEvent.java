package com.jingyuyao.tactical.model.script;

/**
 * An event from the model that can trigger some script actions. Each implementation must have a
 * corresponding method in {@link Condition}.
 */
public interface ScriptEvent {

  /**
   * Return whether this event is satisfied by the given condition.
   */
  boolean satisfiedBy(Condition condition);
}
