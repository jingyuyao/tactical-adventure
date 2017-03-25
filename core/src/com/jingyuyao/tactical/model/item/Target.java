package com.jingyuyao.tactical.model.item;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Cell;
import java.util.Set;

public class Target {

  private final ImmutableSet<Cell> selectCells;
  private final ImmutableSet<Cell> targetCells;

  Target(Set<Cell> selectCells, Set<Cell> targetCells) {
    this.selectCells = ImmutableSet.copyOf(selectCells);
    this.targetCells = ImmutableSet.copyOf(targetCells);
  }

  public boolean selectedBy(Cell cell) {
    return selectCells.contains(cell);
  }

  public boolean canTarget(Cell cell) {
    return targetCells.contains(cell);
  }

  public Iterable<Cell> getSelectCells() {
    return selectCells;
  }

  public Iterable<Cell> getTargetCells() {
    return targetCells;
  }

  public FluentIterable<Character> getTargetCharacters() {
    return FluentIterable.from(targetCells)
        .filter(new Predicate<Cell>() {
          @Override
          public boolean apply(Cell input) {
            return input.hasCharacter();
          }
        })
        .transform(new Function<Cell, Character>() {
          @Override
          public Character apply(Cell input) {
            return input.getCharacter();
          }
        });
  }

}
