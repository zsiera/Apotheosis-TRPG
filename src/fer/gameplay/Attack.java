/*
 * 
 */
package fer.gameplay;

import java.util.ArrayList;

import fer.Tile;
import fer.Unit;
import fer.Game;
import fer.ai.PathFinder;

// TODO: Auto-generated Javadoc
/**
 * The Class Attack.
 */
public class Attack {
	
	/**
	 * Calculate attack damage.
	 *
	 * @param attacker the attacker
	 * @param defender the defender
	 * @return the int
	 */
	public static int calculateAttackDamage(final Unit attacker, final Unit defender) {
		int wepatk;
		wepatk = attacker.getWeapon(0).getDamage() + attacker.getStr();
		int wepdam;
		wepdam = !(defender.getArmor() != null) ? wepatk
				- (defender.getDef() + Game.getCurrentMap()
						.getTile(defender.getMapx() + defender.getMapy())
						.getDef()) : wepatk
				- (defender.getDef()
						+ Game.getCurrentMap()
								.getTile(
										defender.getMapx()
												+ defender.getMapy())
								.getDef() + (defender.getArmor()
						.getResilience() - attacker.getWeapon(0)
						.getPierce()));
		return Math.max(0, wepdam);
	}

	/**
	 * Calculate attack hit chance.
	 *
	 * @param attacker the attacker
	 * @param defender the defender
	 * @return the float
	 */
	public static float calculateAttackHitChance(final Unit attacker, final Unit defender) {
		float hitRate = attacker.getWeapon(0).getAccuracy() + attacker
				.getSkl() * 2;
		float evade = calculateAttackSpeed(defender) * 2
				+ Game.getCurrentMap()
						.getTile(
								defender.getMapx() + defender.getMapy()
										* Game.getCurrentMap().getWidth())
						.getAvo();
		return Math.min(Math.max(hitRate - evade, 0), 100);
	}

	/**
	 * Gets the number of attacks.
	 *
	 * @param unit the unit
	 * @param opponent the opponent
	 * @return the number of attacks
	 */
	public static int getNumberOfAttacks(final Unit unit, final Unit opponent) {
		int numAttacksUnit, numAttacksOpponent;
		numAttacksUnit = !(unit.getWeapon(0).getRange() >= Math.abs(unit.getMapx()
				- opponent.getMapx()) + Math.abs(unit.getMapy()
				- opponent.getMapy())) ? 0 : getAttackUnits(unit, opponent);
		numAttacksOpponent = !(opponent.getWeapon(0).getRange() >= Math.abs(opponent.getMapx()
				- unit.getMapx()) + Math.abs(opponent.getMapy()
				- unit.getMapy())) ? 0 : getAttackUnits(opponent, unit);
		return numAttacksUnit;
	}

	/**
	 * Gets the attack units.
	 *
	 * @param unit the unit
	 * @param opponent the opponent
	 * @return the attack units
	 */
	public static int getAttackUnits(final Unit unit, final Unit opponent) {
		int numAttacksUnit;
		numAttacksUnit = calculateAttackSpeed(unit) >= calculateAttackSpeed(opponent) + 3 ? 2 : 1;
		if (numAttacksUnit > unit.getWeapon(0).getUses()) {
			numAttacksUnit = unit.getWeapon(0).getUses();
		}
		return numAttacksUnit;
	}

	/**
	 * Calculate attack speed.
	 *
	 * @param unit the unit
	 * @return the int
	 */
	public static int calculateAttackSpeed(final Unit unit) {
		int speed = unit.getSpd();
		if (unit.getWeapon(0).getWeight() > unit.getCon()) {
			speed -= unit.getWeapon(0).getWeight() - unit.getCon();
		}
		if (unit.getArmor().getEncumberance() > unit.getCon()) {
			speed -= unit.getArmor().getEncumberance() - unit.getCon();
		}
		return speed;
	}

	/**
	 * Calculate death chance.
	 *
	 * @param unit the unit
	 * @param opponent the opponent
	 * @param unitAttacking the unit attacking
	 * @return the double
	 */
	public static double calculateDeathChance(final Unit unit, final Unit opponent,
			final boolean unitAttacking) {
		if (2 * calculateAttackDamage(opponent, unit) < unit.getCurrentHp()) {
			return 0;
		} else {
			int numAttacksUnit = getNumberOfAttacks(unit, opponent);
			double opponentSurvival = 1;
			if (calculateAttackDamage(opponent, unit) >= unit.getCurrentHp()) {
				// One hit will kill
				if (unitAttacking
						&& calculateAttackDamage(unit, opponent) >= opponent
								.getCurrentHp() && numAttacksUnit >= 1) {
					// If the unit can strike the opponent first and kill,
					// survival is the probability they miss
					opponentSurvival = 1 - calculateAttackHitChance(unit,
							opponent) / 100;
				}
				return (calculateAttackHitChance(opponent, unit) / 100)
						* opponentSurvival;// Unit misses and opponent hits;
			} else // Two hits will kill
			if (!unitAttacking
					&& calculateAttackDamage(unit, opponent) >= opponent
							.getCurrentHp() && numAttacksUnit >= 1) {
				// If the unit can strike the opponent after its first
				// attack and
				// kill, survival is the probability they miss
				opponentSurvival = 1 - calculateAttackHitChance(unit,
						opponent) / 100;
			} else if (unitAttacking
					&& 2 * calculateAttackDamage(unit, opponent) >= opponent
							.getCurrentHp() && numAttacksUnit >= 2) {
				// If the unit strikes first twice and will kill if they hit
				// both
				// times, survival is the probability they do not
				opponentSurvival = 1 - Math
						.pow(calculateAttackHitChance(unit, opponent) / 100,
								2);
			}
			return Math
					.pow(calculateAttackHitChance(opponent, unit) / 100, 2)
					* opponentSurvival;// Unit does not hit twice, opponent does
		}
	}

	/**
	 * Attack with weapon in range.
	 *
	 * @param pf the pf
	 * @param bp the bp
	 * @param unit the unit
	 * @param target the target
	 */
	public static void attackWithWeaponInRange(final PathFinder pf,
			final BattleProcessor bp, final Unit unit, final Unit target) {
		for (int i = 0; i < unit.getWeapons().length; i++) {
			// Test each weapon's range
			if (unit.getWeapon(i) != null) {
				ArrayList<Tile> attackableTiles = pf
						.getAttackableTiles(unit, unit.getMapx(),
								unit.getMapy(), Game.getCurrentMap(), i);
				if (attackableTiles.contains(Game.getCurrentMap().getTile(
						target.getMapx() + target.getMapy()
								* Game.getCurrentMap().getWidth()))) {
					// Target is in firing range, attack
					if (i != 0) {
						equipWeapon(unit, i);
					}
					bp.startBattle(unit, target);
					break;
				}
			}
		}
	}

	/**
	 * Equip weapon.
	 *
	 * @param testUnit the test unit
	 * @param longest the longest
	 */
	private static void equipWeapon(final Unit testUnit, final int longest) {
		Weapon temp = testUnit.getWeapon(0);
		testUnit.setWeapon(0, testUnit.getWeapon(longest));
		testUnit.setWeapon(longest, temp);
	}
}