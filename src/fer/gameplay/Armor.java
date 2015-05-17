package fer.gameplay;

import fer.Game;
import fer.graphics.Sprite;
import fer.util.ArmorData;

/**
 * @author Evan
 */
public class Armor {

	private Sprite icon;
	private String name;

	private int resilience, encumberance, price;

	/*
	 Public Armor(int typeindex) { switch(typeindex) { case 0: //Leather
	 Bodyarmor icon = new Sprite(16, 16, 1, 1, SpriteSheet.ARMORICONSET); name
	  = "Leather BA";
	  
	  resilience = 5; encumberance = 2; } }
	 */

	public Armor(final int typeindex) {
		ArmorData data = Game.getArmorData(typeindex);
		name = data.getName();
		resilience = data.getResilience();
		encumberance = data.getEncumberance();
		price = data.getPrice();

		icon = new Sprite(data.getSpriteWidth(), data.getSpriteHeight(),
				data.getX(), data.getY(), Game.getSpriteSheet(data
						.getSheetIndex()));
	}

	public final Sprite getIcon() {
		return icon;
	}

	public final void setIcon(final Sprite icon) {
		this.icon = icon;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final int getResilience() {
		return resilience;
	}

	public final void setResilience(final int resilience) {
		this.resilience = resilience;
	}

	public final int getEncumberance() {
		return encumberance;
	}

	public final void setEncumberance(final int encumberance) {
		this.encumberance = encumberance;
	}
}
