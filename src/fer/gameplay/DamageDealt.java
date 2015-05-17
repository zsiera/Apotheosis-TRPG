package fer.gameplay;

import fer.Unit;

public class DamageDealt {
	private int attackerDamageDealt;
	private int defenderDamageDealt;

	public final int getAttackerDamageDealt() {
		return attackerDamageDealt;
	}

	public final void setAttackerDamageDealt(final int attackerDamageDealt) {
		this.attackerDamageDealt = attackerDamageDealt;
	}

	public final int getDefenderDamageDealt() {
		return defenderDamageDealt;
	}

	public final void setDefenderDamageDealt(final int defenderDamageDealt) {
		this.defenderDamageDealt = defenderDamageDealt;
	}

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