package fer.gameplay;

import java.util.Random;

import fer.Cursor;
import fer.Game;
import fer.Tile;
import fer.Unit;
import fer.graphics.Animation;
import fer.graphics.Effect;
import fer.graphics.Sprite;
import fer.ui.Font;
import fer.ui.MenuCursor;
import fer.ui.TextGraphic;

/**
 * @author Evan
 */
public class BattleProcessor {

	private ExpMenus expMenus = new ExpMenus();
	private DefenderMenus defendMenus = new DefenderMenus();
	private AttackMenus attackMenus = new AttackMenus();
	private DamageDealt damageDealt = new DamageDealt();
	private Unit attacker;
	private Unit defender;
	private Random random = new Random();
	private Cursor cursor = Cursor.getCursor();
	private Effect damageEffect;
	private Effect criticalEffect;
	private boolean inCombat;
	private int stage;
	private int currentAttack;
	private int updates;
	private boolean[] missAttacker;
	private boolean[] missDefender;
	private boolean[] criticalAttacker;
	private boolean[] criticalDefender;
	private boolean melee;
	private int numAttacksAttacker;
	private int numAttacksDefender;
	private int attackerDamage;
	private int defenderDamage;
	private int attackerLevelsGained;
	private int defenderLevelsGained;
	private int levelIterations;
	private float accAttacker;
	private float accDefender;
	private float critAttacker;
	private float critDefender;
	/** Multipurpose boolean variables to mark when animations are completed. */
	private boolean attackerComplete;
	private boolean defenderComplete;
	/**
	 * Integer corresponding to the directional relationship of the defender to
	 * the attacker. 0 = Above, 1 = Below, 2 = Left, 3 = Right
	 */
	private int directionAttacker;
	/**
	 * Integer corresponding to the directional relationship of the attacker to
	 * the defender.
	 */
	private int directionDefender;

	public void startBattle(Unit iAttacker, Unit iDefender) {
		initializeBattle(iAttacker, iDefender);

		// Determine direction of attack
		// If x difference is greater than y difference, face left or right
		// accordingly.
		if (Math.abs(attacker.getMapx() - defender.getMapx()) > Math
				.abs(attacker.getMapy() - defender.getMapy())) {
			if (defender.getMapx() < attacker.getMapx()) {
				directionAttacker = 2;
				directionDefender = 3;
			} else {
				directionAttacker = 3;
				directionDefender = 2;
			}
		} else if (defender.getMapy() < attacker.getMapy()) {
			directionAttacker = 0;
			directionDefender = 1;
		} else {
			directionAttacker = 1;
			directionDefender = 0; // If, by some odd glitch, the attacker
									// and defender
			// occupy the same tile, the attacker will face down, and the
			// defender up.
		}

		// Calculate the number of attacks (Based on FE:PR formula at the
		// moment)
		if (Attack.calculateAttackSpeed(attacker) >= Attack
				.calculateAttackSpeed(defender) + 3) {
			numAttacksAttacker = 2;
		} else {
			numAttacksAttacker = 1;
		}
		if (numAttacksAttacker > attacker.getWeapon(0).getUses()) {
			numAttacksAttacker = attacker.getWeapon(0).getUses();
		}
		if (defender.getWeapon(0).getRange() >= Math.abs(defender.getMapx()
				- attacker.getMapx()) + Math.abs(defender.getMapy()
				- attacker.getMapy())) {
			if (Attack.calculateAttackSpeed(defender) >= Attack
					.calculateAttackSpeed(attacker) + 3) {
				numAttacksDefender = 2;
			} else {
				numAttacksDefender = 1;
			}
			if (numAttacksDefender > defender.getWeapon(0).getUses()) {
				numAttacksDefender = defender.getWeapon(0).getUses();
			}
		} else {
			numAttacksDefender = 0;
		}

		// Determine if attacks hit
		missAttacker = new boolean[numAttacksAttacker];
		missDefender = new boolean[numAttacksDefender];
		accAttacker = Attack.calculateAttackHitChance(attacker, defender);
		for (int i = 0; i < missAttacker.length; i++) {
			float num = random.nextFloat() * 100;
			missAttacker[i] = num > accAttacker;
		}
		accDefender = Attack.calculateAttackHitChance(defender, attacker);
		for (int i = 0; i < missDefender.length; i++) {
			float num = random.nextFloat() * 100;
			missDefender[i] = num > accDefender;
		}

		// Calculate if attack is critical
		criticalAttacker = new boolean[numAttacksAttacker];
		criticalDefender = new boolean[numAttacksDefender];
		critAttacker = attacker.calculateCriticalChance(defender);
		for (int i = 0; i < criticalAttacker.length; i++) {
			float num = random.nextFloat() * 100;
			criticalAttacker[i] = num <= critAttacker && !missAttacker[i];
		}
		critDefender = defender.calculateCriticalChance(attacker);
		for (int i = 0; i < criticalDefender.length; i++) {
			float num = random.nextFloat() * 100;
			criticalDefender[i] = num <= critDefender && !missDefender[i];
		}

		// Calculate attack damage
		attackerDamage = Attack.calculateAttackDamage(attacker, defender);
		defenderDamage = Attack.calculateAttackDamage(defender, attacker);

		attackMenus.drawMenu(cursor, attacker, attackerDamage, accAttacker,
				critAttacker);
		defendMenus.drawMenu(cursor, defender, defenderDamage, accDefender,
				critDefender);
	}

	private void initializeBattle(Unit iAttacker, Unit iDefender) {
		cursor.initializeCursorForBattle();
		inCombat = true;
		damageDealt.initializeUnits(iAttacker, iDefender, this);
		updates = 0;
		stage = 0;
		currentAttack = 0;
	}

	public void update() {
		switch (stage) {
		case 0:
			attackerComplete = false;
			defenderComplete = false;
			attacker.setActiveMapAnimation(6 + directionAttacker);
			updates = 0;
			stage++;
			break;
		case 1:
			if (updates >= attacker.getActiveAnimationUpdates(attacker
					.getActiveAnimationKeyFrame() - 1)) {
				if (missAttacker[currentAttack]) {
					defender.setActiveMapAnimation(14 + Math
							.abs(directionAttacker - 3));
				} else {
					defender.setActiveMapAnimation(18 + directionDefender);
					damageEffect = new Effect(new Animation(1,
							new Sprite[] { defender.getMapSprite() }),
							new int[] { 128 }, new int[] { 0xff0000 },
							(defender.getMapx() - cursor.getMapScrollx())
									* Tile.TILE_WIDTH - 4,
							(defender.getMapy() - cursor.getMapScrolly())
									* Tile.TILE_HEIGHT - 8);
					if (criticalAttacker[currentAttack]) {
						criticalEffect = new Effect(new Animation(1,
								new Sprite[] { new TextGraphic("CRITICAL!",
										Font.BASICFONT).getSprite() },
								new int[] { 5 }), new int[] { 192 },
								(defender.getMapx() - cursor.getMapScrollx())
										* Tile.TILE_WIDTH - 4,
								(defender.getMapy() - cursor.getMapScrolly())
										* Tile.TILE_HEIGHT - 8);
					}
				}
				updates = 0;
				stage++;
			}
			break;
		case 2:
			if (updates == attacker.getActiveAnimationUpdates(attacker
					.getActiveAnimationFrames())
					- attacker.getActiveAnimationUpdates(attacker
							.getActiveAnimationKeyFrame() - 1)) {
				attackerComplete = true;
				attacker.setActiveMapAnimation(0);
			}
			if (updates == defender.getActiveAnimationUpdates(defender
					.getActiveAnimationFrames())) {
				defenderComplete = true;
				defender.setActiveMapAnimation(0);
				Game.getEffectList().remove(damageEffect);
				Game.getEffectList().remove(criticalEffect);
			}
			if (!defenderComplete) {
				if (updates % 2 == 0 && missAttacker[currentAttack]) {
					switch (directionAttacker) {
					case 0:
						defender.setxOffset(defender.getxOffset() + 1);
						break;
					case 1:
						defender.setxOffset(defender.getxOffset() - 1);
						break;
					case 2:
						defender.setyOffset(defender.getyOffset() + 1);
						break;
					case 3:
						defender.setyOffset(defender.getyOffset() - 1);
						break;
					}
				} else if (!missAttacker[currentAttack]) {
					// Refresh damageEffect sprite to match unit animation
					Game.getEffectList().remove(damageEffect);
					damageEffect = new Effect(new Animation(1,
							new Sprite[] { defender.getMapSprite() }),
							new int[] { 128 }, new int[] { 0xff0000 },
							(defender.getMapx() - cursor.getMapScrollx())
									* Tile.TILE_WIDTH - 4,
							(defender.getMapy() - cursor.getMapScrolly())
									* Tile.TILE_HEIGHT - 8);
				}
			}
			if (attackerComplete && defenderComplete) {
				defender.setxOffset(0);
				defender.setyOffset(0);
				updates = 0;
				stage++;
			} /*
			 * else if (attackerComplete && !missAttacker[currentAttack]) {
			 * updates = 0; stage++; }
			 */
			break;
		case 3:
			if (!missAttacker[currentAttack]) {
				if (criticalAttacker[currentAttack]) {
					defender.setCurrentHp(Math.max(defender.getCurrentHp()
							- attackerDamage * 3, 0));
					damageDealt.setAttackerDamageDealt(attackerDamage * 3);
				} else {
					defender.setCurrentHp(Math.max(defender.getCurrentHp()
							- attackerDamage, 0));
					damageDealt.setAttackerDamageDealt(attackerDamage);
				}
				/*
				 * if (directionAttacker == 1) {
				 * defender.setActiveMapAnimation(18); } else if
				 * (directionAttacker == 0) {
				 * defender.setActiveMapAnimation(19); } else if
				 * (directionAttacker == 3) {
				 * defender.setActiveMapAnimation(20); } else {
				 * defender.setActiveMapAnimation(21); }
				 */
				defendMenus.drawMenu(cursor, defender, defenderDamage,
						accDefender, critDefender); // Update health display
				if (defender.getCurrentHp() == 0) { // If the defender is dead
					stage = 10;
					break;
				}
			}
			updates = 0;
			stage++;
			break;
		case 4:
			if (currentAttack < numAttacksDefender) {
				stage++;
			} else {
				stage = 9;
			}
			break;
		case 5:
			attackerComplete = false;
			defenderComplete = false;
			defender.setActiveMapAnimation(6 + directionDefender);
			updates = 0;
			stage++;
			break;
		case 6:
			if (updates >= defender.getActiveAnimationUpdates(defender
					.getActiveAnimationKeyFrame() - 1)) {
				if (missDefender[currentAttack]) {
					attacker.setActiveMapAnimation(14 + Math
							.abs(directionDefender - 3));
				} else {
					attacker.setActiveMapAnimation(18 + directionAttacker);
					damageEffect = new Effect(new Animation(1,
							new Sprite[] { attacker.getMapSprite() }),
							new int[] { 128 }, new int[] { 0xff0000 },
							(attacker.getMapx() - cursor.getMapScrollx())
									* Tile.TILE_WIDTH - 4,
							(attacker.getMapy() - cursor.getMapScrolly())
									* Tile.TILE_HEIGHT - 8);
					if (criticalDefender[currentAttack]) {
						criticalEffect = new Effect(new Animation(1,
								new Sprite[] { new TextGraphic("CRITICAL!",
										Font.BASICFONT).getSprite() },
								new int[] { 5 }), new int[] { 192 },
								(attacker.getMapx() - cursor.getMapScrollx())
										* Tile.TILE_WIDTH - 4,
								(attacker.getMapy() - cursor.getMapScrolly())
										* Tile.TILE_HEIGHT - 8);
					}
				}
				updates = 0;
				stage++;
			}
			break;
		case 7:
			if (updates == defender.getActiveAnimationUpdates(defender
					.getActiveAnimationFrames())
					- defender.getActiveAnimationUpdates(defender
							.getActiveAnimationKeyFrame() - 1)) {
				attackerComplete = true;
				defender.setActiveMapAnimation(0);
			}
			if (updates == attacker.getActiveAnimationUpdates(attacker
					.getActiveAnimationFrames())) {
				defenderComplete = true;
				attacker.setActiveMapAnimation(0);
				Game.getEffectList().remove(damageEffect);
				Game.getEffectList().remove(criticalEffect);
			}
			if (!defenderComplete) {
				if (updates % 2 == 0 && missDefender[currentAttack]) {
					switch (directionDefender) {
					case 0:
						attacker.setxOffset(attacker.getxOffset() + 1);
						break;
					case 1:
						attacker.setxOffset(attacker.getxOffset() - 1);
						break;
					case 2:
						attacker.setyOffset(attacker.getyOffset() - 1);
						break;
					case 3:
						attacker.setyOffset(attacker.getyOffset() + 1);
						break;
					}
				} else if (!missDefender[currentAttack]) {
					// Refresh damageEffect sprite to match unit animation
					Game.getEffectList().remove(damageEffect);
					damageEffect = new Effect(new Animation(1,
							new Sprite[] { attacker.getMapSprite() }),
							new int[] { 128 }, new int[] { 0xff0000 },
							(attacker.getMapx() - cursor.getMapScrollx())
									* Tile.TILE_WIDTH - 4,
							(attacker.getMapy() - cursor.getMapScrolly())
									* Tile.TILE_HEIGHT - 8);
				}
			}
			if (attackerComplete && defenderComplete) {
				attacker.setxOffset(0);
				attacker.setyOffset(0);
				updates = 0;
				stage++;
			}
			break;
		case 8:
			if (!missDefender[currentAttack]) {
				if (criticalDefender[currentAttack]) {
					attacker.setCurrentHp(Math.max(attacker.getCurrentHp()
							- defenderDamage * 3, 0));
					damageDealt.setDefenderDamageDealt(defenderDamage * 3);
				} else {
					attacker.setCurrentHp(Math.max(attacker.getCurrentHp()
							- defenderDamage, 0));
					damageDealt.setDefenderDamageDealt(defenderDamage);
				}
				/*
				 * if (directionDefender == 1) {
				 * attacker.setActiveMapAnimation(18); } else if
				 * (directionDefender == 0) {
				 * attacker.setActiveMapAnimation(19); } else if
				 * (directionDefender == 3) {
				 * attacker.setActiveMapAnimation(20); } else {
				 * attacker.setActiveMapAnimation(21); }
				 */
				attackMenus.drawMenu(cursor, attacker, attackerDamage,
						accAttacker, critAttacker); // Update health display
				if (attacker.getCurrentHp() == 0) { // If the attacker is dead
					stage = 10;
					break;
				}
			}
			updates = 0;
			stage++;
			break;
		case 9:
			currentAttack++;
			attacker.resetAnimation(22);
			defender.resetAnimation(22);
			if (currentAttack < numAttacksAttacker) {
				stage = 0; // Repeat if attacker attacks again
			} else if (currentAttack < numAttacksDefender) {
				stage = 5; // If attacker does not attack but defender does,
				// repeat from 6 instead of 0.
			} else {
				stage++; // Neither attacks again; the battle is over.
			}
			break;
		case 10:
			if (attacker.getCurrentHp() == 0) {
				attacker.setDead(true);
			}
			if (defender.getCurrentHp() == 0) {
				defender.setDead(true);
			}
			MenuCursor.getMenuCursor().setActive(true);
			if (!attacker.isDead()) {
				expMenus.drawAttackerExpMenu(
						attacker.calculateExpGain(defender,
								damageDealt.getAttackerDamageDealt(),
								defender.isDead()), attacker);
			}
			if (!defender.isDead()) {
				expMenus.drawDefenderExpMenu(
						defender.calculateExpGain(attacker,
								damageDealt.getDefenderDamageDealt(),
								attacker.isDead()), defender);
			}
			stage++;
			break;
		case 11:
			if (!expMenus.getAttackerExpOpen()
					&& !expMenus.getDefenderExpOpen()) {
				stage++;
			}
			break;
		case 12:
			attackerLevelsGained = (attacker.getExp() + attacker
					.calculateExpGain(defender,
							damageDealt.getAttackerDamageDealt(),
							defender.isDead())) / Unit.EXP_CAP;
			defenderLevelsGained = (defender.getExp() + defender
					.calculateExpGain(attacker,
							damageDealt.getDefenderDamageDealt(),
							attacker.isDead())) / Unit.EXP_CAP;
			attacker.setExp((attacker.getExp() + attacker.calculateExpGain(
					defender, damageDealt.getAttackerDamageDealt(),
					defender.isDead()))
					% Unit.EXP_CAP);
			defender.setExp((defender.getExp() + defender.calculateExpGain(
					attacker, damageDealt.getDefenderDamageDealt(),
					attacker.isDead()))
					% Unit.EXP_CAP);
			levelIterations = 0;
			stage++;
			break;
		case 13:
			if (levelIterations < attackerLevelsGained) {
				attacker.levelUnit(true);
				stage++;
			} else {
				levelIterations = 0;
				stage = 15;
			}
			break;
		case 14:
			if (!attacker.isShowingLevelMenu()) {
				levelIterations++;
				stage--;
			}
			break;
		case 15:
			if (levelIterations < defenderLevelsGained) {
				defender.levelUnit(true);
				stage++;
			} else {
				levelIterations = 0;
				stage = 17;
			}
			break;
		case 16:
			if (!defender.isShowingLevelMenu()) {
				levelIterations++;
				stage--;
			}
			break;
		case 17:
			endBattle();
			break;
		}
		updates++;
	}

	public void endBattle() {
		attackMenus.clearMenus();
		defendMenus.clearMenus();
		attacker.resetAnimation(22);
		defender.resetAnimation(22);
		attacker.setMoved(true);
		attacker.getWeapon(0).setUses(
				attacker.getWeapon(0).getUses() - numAttacksAttacker);
		if (attacker.getWeapon(0).getUses() <= 0) {
			attacker.setWeapon(0, null);
			for (int i = 1; i < attacker.getWeapons().length; i++) {
				if (attacker.getWeapon(i) != null) {
					attacker.setWeapon(0, attacker.getWeapon(i));
					attacker.setWeapon(i, null);
					break;
				}
			}
		}
		defender.getWeapon(0).setUses(
				defender.getWeapon(0).getUses() - numAttacksDefender);
		if (defender.getWeapon(0).getUses() <= 0) {
			defender.setWeapon(0, null);
			for (int i = 1; i < defender.getWeapons().length; i++) {
				if (defender.getWeapon(i) != null) {
					defender.setWeapon(0, defender.getWeapon(i));
					defender.setWeapon(i, null);
					break;
				}
			}
		}
		inCombat = false;
		cursor.setCursorForVictory();
	}

	public int calculateAttackDamage(Unit attacker, Unit defender) {
		return Attack.calculateAttackDamage(attacker, defender);
	}

	public double calculateDeathChance(Unit unit, Unit opponent,
			boolean unitAttacking) {
		return Attack.calculateDeathChance(unit, opponent, unitAttacking);
	}

	public boolean isInCombat() {
		return inCombat;
	}

	public void setAttacker(Unit attacker) {
		this.attacker = attacker;
	}

	public void setDefender(Unit defender) {
		this.defender = defender;
	}

	public Unit getAttacker() {
		return attacker;
	}

	public Unit getDefender() {
		return defender;
	}
}
