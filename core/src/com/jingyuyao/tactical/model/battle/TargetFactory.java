package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Weapon;

public interface TargetFactory {

  ImmutableList<Target> create(Character attacker, Weapon weapon);
}
