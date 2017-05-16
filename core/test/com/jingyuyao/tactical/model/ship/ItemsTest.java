package com.jingyuyao.tactical.model.ship;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Bulkheads;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Hull;
import com.jingyuyao.tactical.model.item.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemsTest {

  @Mock
  private Consumable consumable1;
  @Mock
  private Weapon weapon1;
  @Mock
  private Hull hull1;
  @Mock
  private Hull hull2;
  @Mock
  private Bulkheads bulkheads;

  private Items items;

  @Before
  public void setUp() {
    items = new Items(
        Lists.newArrayList(consumable1),
        Lists.newArrayList(weapon1),
        Lists.<Armor>newArrayList(hull1),
        Lists.<Armor>newArrayList(bulkheads, hull2)
    );
    assertThat(hull1.getClass()).isAssignableTo(hull2.getClass());
    assertThat(hull2.getClass()).isAssignableTo(hull1.getClass());
  }

  @Test
  public void consumables() {
    assertThat(items.getConsumables()).containsExactly(consumable1);
  }

  @Test
  public void weapons() {
    assertThat(items.getWeapons()).containsExactly(weapon1);
  }

  @Test
  public void equipped_armors() {
    assertThat(items.getEquippedArmors()).containsExactly(hull1);
  }

  @Test
  public void stashed_armors() {
    assertThat(items.getStashedArmors()).containsExactly(bulkheads, hull2);
  }

  @Test
  public void get_defense() {
    when(hull1.getDefense()).thenReturn(123);

    assertThat(items.getDefense()).isEqualTo(123);
  }

  @Test
  public void use_consumable() {
    when(consumable1.getUsageLeft()).thenReturn(1);

    items.useConsumable(consumable1);

    verify(consumable1).useOnce();
    assertThat(items.getConsumables()).containsExactly(consumable1);
  }

  @Test
  public void use_consumable_no_more() {
    when(consumable1.getUsageLeft()).thenReturn(0);

    items.useConsumable(consumable1);

    verify(consumable1).useOnce();
    assertThat(items.getConsumables()).isEmpty();
  }

  @Test
  public void use_weapon() {
    when(weapon1.getUsageLeft()).thenReturn(1);

    items.useWeapon(weapon1);

    verify(weapon1).useOnce();
    assertThat(items.getWeapons()).containsExactly(weapon1);
  }

  @Test
  public void use_weapon_broken() {
    when(weapon1.getUsageLeft()).thenReturn(0);

    items.useWeapon(weapon1);

    verify(weapon1).useOnce();
    assertThat(items.getWeapons()).isEmpty();
  }

  @Test
  public void use_armors() {
    when(hull1.getUsageLeft()).thenReturn(1);

    items.useEquippedArmors();

    verify(hull1).useOnce();
    assertThat(items.getEquippedArmors()).containsExactly(hull1);
  }

  @Test
  public void use_armors_broken() {
    when(hull1.getUsageLeft()).thenReturn(0);

    items.useEquippedArmors();

    verify(hull1).useOnce();
    assertThat(items.getEquippedArmors()).isEmpty();
  }

  @Test
  public void equip_armor_already_equipped_class() {
    items.equipArmor(hull2);

    assertThat(items.getEquippedArmors()).containsExactly(hull2);
    assertThat(items.getStashedArmors()).containsExactly(bulkheads, hull1);
  }

  @Test
  public void equip_armor_no_already_equipped_class() {
    items.equipArmor(bulkheads);

    assertThat(items.getEquippedArmors()).containsExactly(hull1, bulkheads);
    assertThat(items.getStashedArmors()).containsExactly(hull2);
  }

  @Test
  public void unequip_armor() {
    items.unequipArmor(hull1);

    assertThat(items.getEquippedArmors()).isEmpty();
    assertThat(items.getStashedArmors()).containsExactly(hull1, hull2, bulkheads);
  }
}