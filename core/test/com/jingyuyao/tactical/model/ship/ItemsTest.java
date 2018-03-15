package com.jingyuyao.tactical.model.ship;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.BlastArmor;
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
  private Armor armor;
  @Mock
  private BlastArmor blastArmor1;
  @Mock
  private BlastArmor blastArmor2;

  private Items items;

  @Before
  public void setUp() {
    items = new Items(
        Lists.newArrayList(consumable1),
        Lists.newArrayList(weapon1),
        Lists.<Armor>newArrayList(blastArmor1),
        Lists.<Armor>newArrayList(armor, blastArmor2)
    );
    assertThat(blastArmor1.getClass()).isAssignableTo(blastArmor2.getClass());
    assertThat(blastArmor2.getClass()).isAssignableTo(blastArmor1.getClass());
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
    assertThat(items.getEquippedArmors()).containsExactly(blastArmor1);
  }

  @Test
  public void stashed_armors() {
    assertThat(items.getStashedArmors()).containsExactly(armor, blastArmor2);
  }

  @Test
  public void get_defense() {
    when(blastArmor1.getDefense()).thenReturn(123);

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
    when(blastArmor1.getUsageLeft()).thenReturn(1);

    items.useEquippedArmors();

    verify(blastArmor1).useOnce();
    assertThat(items.getEquippedArmors()).containsExactly(blastArmor1);
  }

  @Test
  public void use_armors_broken() {
    when(blastArmor1.getUsageLeft()).thenReturn(0);

    items.useEquippedArmors();

    verify(blastArmor1).useOnce();
    assertThat(items.getEquippedArmors()).isEmpty();
  }

  @Test
  public void equip_armor_already_equipped_class() {
    items.equipArmor(blastArmor2);

    assertThat(items.getEquippedArmors()).containsExactly(blastArmor2);
    assertThat(items.getStashedArmors()).containsExactly(armor, blastArmor1);
  }

  @Test
  public void equip_armor_no_already_equipped_class() {
    items.equipArmor(armor);

    assertThat(items.getEquippedArmors()).containsExactly(blastArmor1, armor);
    assertThat(items.getStashedArmors()).containsExactly(blastArmor2);
  }

  @Test
  public void unequip_armor() {
    items.unequipArmor(blastArmor1);

    assertThat(items.getEquippedArmors()).isEmpty();
    assertThat(items.getStashedArmors()).containsExactly(blastArmor1, blastArmor2,
        armor);
  }
}