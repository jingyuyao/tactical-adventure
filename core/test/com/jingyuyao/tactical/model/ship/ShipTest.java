package com.jingyuyao.tactical.model.ship;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.person.Pilot;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ShipTest {

  private static final String NAME = "yo";

  @Mock
  private AutoPilot autoPilot;
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
  private Pilot pilot;
  @Mock
  private Cell cell;
  @Mock
  private World world;
  @Mock
  private Ship anotherShip;
  @Mock
  private PilotResponse pilotResponse;

  private Ship ship;

  @Before
  public void setUp() {
    ship = new Ship(NAME, autoPilot, stats, cockpit, items);
  }

  @Test
  public void get_name() {
    StringKey name = ship.getName();
    assertThat(name.getBundle()).isSameAs(ModelBundle.SHIP_NAME);
    assertThat(name.getId()).isEqualTo(NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void auto_pilot_response_no_ship() {
    when(cell.ship()).thenReturn(Optional.<Ship>absent());

    ship.getAutoPilotResponse(world, cell);
  }

  @Test(expected = IllegalArgumentException.class)
  public void auto_pilot_response_wrong_ship() {
    when(cell.ship()).thenReturn(Optional.of(anotherShip));

    ship.getAutoPilotResponse(world, cell);
  }

  @Test
  public void auto_pilot_response() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(autoPilot.getResponse(world, cell)).thenReturn(pilotResponse);

    assertThat(ship.getAutoPilotResponse(world, cell)).isSameAs(pilotResponse);
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
  public void get_pilots() {
    when(cockpit.getPilots()).thenReturn(Collections.singletonList(pilot));

    assertThat(ship.getCrew()).containsExactly(pilot);
  }

  @Test
  public void get_consumables() {
    when(items.getConsumables()).thenReturn(Collections.singletonList(consumable));

    assertThat(ship.getConsumables()).containsExactly(consumable);
  }

  @Test
  public void get_weapons() {
    when(items.getWeapons()).thenReturn(Collections.singletonList(weapon));

    assertThat(ship.getWeapons()).containsExactly(weapon);
  }

  @Test
  public void get_equipped_armors() {
    when(items.getEquippedArmors()).thenReturn(Collections.singletonList(armor));

    assertThat(ship.getEquippedArmors()).containsExactly(armor);
  }

  @Test
  public void get_stashed_armors() {
    when(items.getStashedArmors()).thenReturn(Collections.singletonList(armor));

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