package fer.gameplay;

import fer.Unit;

public class DamageDealt {
	private int attackerDamageDealt;
	private int defenderDamageDealt;

	public int getAttackerDamageDealt() {
		return attackerDamageDealt;
	}

	public void setAttackerDamageDealt(int attackerDamageDealt) {
		this.attackerDamageDealt = attackerDamageDealt;
	}

	public int getDefenderDamageDealt() {
		return defenderDamageDealt;
	}

	public void setDefenderDamageDealt(int defenderDamageDealt) {
		this.defenderDamageDealt = defenderDamageDealt;
	}

	public void initializeUnits(Unit iAttacker, Unit iDefender,
			BattleProcessor battleProcessor) {
		battleProcessor.setAttacker(iAttacker);
		battleProcessor.setDefender(iDefender);
		battleProcessor.getAttacker().resetAnimation(22);
		battleProcessor.getDefender().resetAnimation(22);
		attackerDamageDealt = 0;
		defenderDamageDealt = 0;
	}
}