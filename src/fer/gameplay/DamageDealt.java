/*
 * 
 */
package fer.gameplay;

import fer.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class DamageDealt.
 */
public class DamageDealt {
	
	/** The attacker damage dealt. */
	private int attackerDamageDealt;
	
	/** The defender damage dealt. */
	private int defenderDamageDealt;

	/**
	 * Gets the attacker damage dealt.
	 *
	 * @return the attacker damage dealt
	 */
	public final int getAttackerDamageDealt() {
		return attackerDamageDealt;
	}

	/**
	 * Sets the attacker damage dealt.
	 *
	 * @param attackerDamageDealt the new attacker damage dealt
	 */
	public final void setAttackerDamageDealt(final int attackerDamageDealt) {
		this.attackerDamageDealt = attackerDamageDealt;
	}

	/**
	 * Gets the defender damage dealt.
	 *
	 * @return the defender damage dealt
	 */
	public final int getDefenderDamageDealt() {
		return defenderDamageDealt;
	}

	/**
	 * Sets the defender damage dealt.
	 *
	 * @param defenderDamageDealt the new defender damage dealt
	 */
	public final void setDefenderDamageDealt(final int defenderDamageDealt) {
		this.defenderDamageDealt = defenderDamageDealt;
	}

	/**
	 * Initialize units.
	 *
	 * @param iAttacker the i attacker
	 * @param iDefender the i defender
	 * @param battleProcessor the battle processor
	 */
	public final void initializeUnits(final Unit iAttacker, final Unit iDefender,
			final BattleProcessor battleProcessor) {
		battleProcessor.setAttacker(iAttacker);
		battleProcessor.setDefender(iDefender);
		battleProcessor.getAttacker().resetAnimation(22);
		battleProcessor.getDefender().resetAnimation(22);
		attackerDamageDealt = 0;
		defenderDamageDealt = 0;
	}
}