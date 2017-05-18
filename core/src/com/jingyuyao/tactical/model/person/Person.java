package com.jingyuyao.tactical.model.person;

import com.jingyuyao.tactical.model.resource.ResourceKey;

/**
 * Sentient lifeforms that could be in ships or participate in stories.
 */
public interface Person {

  ResourceKey getName();

  ResourceKey getRole();
}
