/*
 * 
 */
package fer.gameplay;

import fer.Game;
import fer.graphics.Sprite;
import fer.util.ArmorData;

// TODO: Auto-generated Javadoc
/**
 * The Class Armor.
 *
 * @author Evan
 */
public class Armor {

	/** The icon. */
	private Sprite icon;
	
	/** The name. */
	private String name;

	/** The price. */
	private int resilience, encumberance, price;

	/*
	 Public Armor(int typeindex) { switch(typeindex) { case 0: //Leather
	 Bodyarmor icon = new Sprite(16, 16, 1, 1, SpriteSheet.ARMORICONSET); name
	  = "Leather BA";
	  
	  resilience = 5; encumberance = 2; } }
	 */

	/**
	 * Instantiates a new armor.
	 *
	 * @param typeindex the typeindex
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

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public final Sprite getIcon() {
		return icon;
	}

	/**
	 * Sets the icon.
	 *
	 * @param icon the new icon
	 */
	public final void setIcon(final Sprite icon) {
		this.icon = icon;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets the resilience.
	 *
	 * @return the resilience
	 */
	public final int getResilience() {
		return resilience;
	}

	/**
	 * Sets the resilience.
	 *
	 * @param resilience the new resilience
	 */
	public final void setResilience(final int resilience) {
		this.resilience = resilience;
	}

	/**
	 * Gets the encumberance.
	 *
	 * @return the encumberance
	 */
	public final int getEncumberance() {
		return encumberance;
	}

	/**
	 * Sets the encumberance.
	 *
	 * @param encumberance the new encumberance
	 */
	public final void setEncumberance(final int encumberance) {
		this.encumberance = encumberance;
	}
}
