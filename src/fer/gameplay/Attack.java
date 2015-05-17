package fer.gameplay;

import java.util.ArrayList;

import fer.Tile;
import fer.Unit;
import fer.Game;
import fer.ai.PathFinder;

public class Attack {
	public static int calculateAttackDamage(Unit attacker, Unit defender) {
		int wepatk;
		wepatk = attacker.getWeapon(0).getDamage() + attacker.getStr();
		int wepdam;
		if (defender.getArmor() != null) {
			wepdam = wepatk
					- (defender.getDef()
							+ Game.getCurrentMap()
									.getTile(
											defender.getMapx()
													+ defender.getMapy())
									.getDef() + (defender.getArmor()
							.getResilience() - attacker.getWeapon(0)
							.getPierce()));
		} else {
			wepdam = wepatk
					- (defender.getDef() + Game.getCurrentMap()
							.getTile(defender.getMapx() + defender.getMapy())
							.getDef());
		}
		return Math.max(0, wepdam);
	}

	public static float calculateAttackHitChance(Unit attacker, Unit defender) {
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

	public static int getNumberOfAttacks(Unit unit, Unit opponent) {
		int numAttacksUnit, numAttacksOpponent;
		if (unit.getWeapon(0).getRange() >= Math.abs(unit.getMapx()
				- opponent.getMapx()) + Math.abs(unit.getMapy()
				- opponent.getMapy())) {
			numAttacksUnit = getAttackUnits(unit, opponent);
		} else {
			numAttacksUnit = 0;
		}
		if (opponent.getWeapon(0).getRange() >= Math.abs(opponent.getMapx()
				- unit.getMapx()) + Math.abs(opponent.getMapy()
				- unit.getMapy())) {
			numAttacksOpponent = getAttackUnits(opponent, unit);
		} else {
			numAttacksOpponent = 0;
		}
		return numAttacksUnit;
	}

	public static int getAttackUnits(Unit unit, Unit opponent) {
		int numAttacksUnit;
		if (calculateAttackSpeed(unit) >= calculateAttackSpeed(opponent) + 3) {
			numAttacksUnit = 2;
		} else {
			numAttacksUnit = 1;
		}
		if (numAttacksUnit > unit.getWeapon(0).getUses()) {
			numAttacksUnit = unit.getWeapon(0).getUses();
		}
		return numAttacksUnit;
	}

	public static int calculateAttackSpeed(Unit unit) {
		int speed = unit.getSpd();
		if (unit.getWeapon(0).getWeight() > unit.getCon()) {
			speed -= unit.getWeapon(0).getWeight() - unit.getCon();
		}
		if (unit.getArmor().getEncumberance() > unit.getCon()) {
			speed -= unit.getArmor().getEncumberance() - unit.getCon();
		}
		return speed;
	}

	public static double calculateDeathChance(Unit unit, Unit opponent,
			boolean unitAttacking) {
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

	public static void attackWithWeaponInRange(PathFinder pf,
			BattleProcessor bp, Unit unit, Unit target) {
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

	private static void equipWeapon(Unit testUnit, int longest) {
		Weapon temp = testUnit.getWeapon(0);
		testUnit.setWeapon(0, testUnit.getWeapon(longest));
		testUnit.setWeapon(longest, temp);
	}
}