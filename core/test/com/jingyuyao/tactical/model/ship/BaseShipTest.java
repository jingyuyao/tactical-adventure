package com.jingyuyao.tactical.model.ship;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseShipTest {

  private static final String NAME_KEY = "yo";
  private static final String RESOURCE_KEY = "yo_face";

  @Mock
  private Stats stats;
  @Mock
  private Cockpit cockpit;
  @Mock
  private Items items;
  @Mock
  private Armor armor;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;
  @Mock
  private Person person;

  private Ship ship;

  @Before
  public void setUp() {
    ship = new BaseShip(NAME_KEY, RESOURCE_KEY, stats, cockpit, items);
  }

  @Test
  public void get_name() {
    ResourceKey name = ship.getName();
    assertThat(name.getBundle()).isSameAs(ModelBundle.SHIP_NAME);
    assertThat(name.getKey()).isEqualTo(NAME_KEY);
  }

  @Test
  public void get_defense() {
    when(items.getDefense()).thenReturn(24);

    assertThat(ship.getDefense()).isEqualTo(24);
  }

  @Test
  public void damage_by() {
    ship.damageBy(5);

    verify(stats).damageBy(5);
  }

  @Test
  public void heal_by() {
    ship.healBy(5);

    verify(stats).healBy(5);
  }

  @Test
  public void full_heal() {
    ship.fullHeal();

    verify(stats).fullHeal();
  }

  @Test
  public void get_pilots() {
    when(cockpit.getPilots()).thenReturn(ImmutableList.of(person));

    assertThat(ship.getPilots()).containsExactly(person);
  }

  @Test
  public void get_consumables() {
    when(items.getConsumables()).thenReturn(ImmutableList.of(consumable));

    assertThat(ship.getConsumables()).containsExactly(consumable);
  }

  @Test
  public void get_weapons() {
    when(items.getWeapons()).thenReturn(ImmutableList.of(weapon));

    assertThat(ship.getWeapons()).containsExactly(weapon);
  }

  @Test
  public void get_equipped_armors() {
    when(items.getEquippedArmors()).thenReturn(ImmutableList.of(armor));

    assertThat(ship.getEquippedArmors()).containsExactly(armor);
  }

  @Test
  public void get_stashed_armors() {
    when(items.getStashedArmors()).thenReturn(ImmutableList.of(armor));

    assertThat(ship.getStashedArmors()).containsExactly(armor);
  }

  @Test
  public void use_consumable() {
    ship.useConsumable(consumable);

    verify(items).useConsumable(consumable);
  }

  @Test
  public void use_weapon() {
    ship.useWeapon(weapon);

    verify(items).useWeapon(weapon);
  }

  @Test
  public void use_equipped_armors() {
    ship.useEquippedArmors();

    verify(items).useEquippedArmors();
  }

  @Test
  public void equip_armor() {
    ship.equipArmor(armor);

    verify(items).equipArmor(armor);
  }

  @Test
  public void unequip_armor() {
    ship.unequipArmor(armor);

    verify(items).unequipArmor(armor);
  }
}