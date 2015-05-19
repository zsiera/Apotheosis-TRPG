/*
 * 
 */
package fer.gameplay;

// TODO: Auto-generated Javadoc
/**
 * The Class Convoy.
 *
 * @author Evan Stewart A data storage class that holds an inventory of items,
 *         weapons, and armor as well as \a quantity of funds. Used to keep
 *         track of inventories and for each faction on a map as well as shop
 *         inventories.
 */
public class Convoy {

	/** The Constant STORAGE_LIMIT. */
	public static final int STORAGE_LIMIT = 100;
	
	/** The Constant FUNDS_LIMIT. */
	public static final int FUNDS_LIMIT = 999999;

	/** The items. */
	private Item[] items=new Item[0];
	
	/** The weapons. */
	private Weapon[] weapons=new Weapon[0];
	
	/** The armors. */
	private Armor[] armors=new Armor[0];
	
	/** The funds. */
	private int funds;

	/**
	 * Gets the item.
	 *
	 * @param index the index
	 * @return the item
	 */
	public final Item getItem(final int index) {
		return items[index];
	}

	/**
	 * Sets the item.
	 *
	 * @param item the item
	 * @param index the index
	 */
	public final void setItem(final Item item, final int index) {
		items[index] = item;
	}

	/**
	 * Swap items.
	 *
	 * @param index1 the index1
	 * @param index2 the index2
	 */
	public final void swapItems(final int index1, final int index2) {
		Item temp = items[index1];
		items[index1] = items[index2];
		items[index2] = temp;
	}

	/**
	 * Gets the weapon.
	 *
	 * @param index the index
	 * @return the weapon
	 */
	public final Weapon getWeapon(final int index) {
		return weapons[index];
	}

	/**
	 * Sets the weapon.
	 *
	 * @param weapon the weapon
	 * @param index the index
	 */
	public final void setWeapon(final Weapon weapon, final int index) {
		weapons[index] = weapon;
	}

	/**
	 * Swap weapons.
	 *
	 * @param index1 the index1
	 * @param index2 the index2
	 */
	public final void swapWeapons(final int index1, final int index2) {
		Weapon temp = weapons[index1];
		weapons[index1] = weapons[index2];
		weapons[index2] = temp;
	}

	/**
	 * Gets the armor.
	 *
	 * @param index the index
	 * @return the armor
	 */
	public final Armor getArmor(final int index) {
		return armors[index];
	}

	/**
	 * Sets the armor.
	 *
	 * @param armor the armor
	 * @param index the index
	 */
	public final void setArmor(final Armor armor, final int index) {
		armors[index] = armor;
	}

	/**
	 * Swap armors.
	 *
	 * @param index1 the index1
	 * @param index2 the index2
	 */
	public final void swapArmors(final int index1, final int index2) {
		Armor temp = armors[index1];
		armors[index1] = armors[index2];
		armors[index2] = temp;
	}

	/**
	 * Gets the funds.
	 *
	 * @return the funds
	 */
	public final int getFunds() {
		return funds;
	}

	/**
	 * Sets the funds.
	 *
	 * @param funds the new funds
	 */
	public final void setFunds(final int funds) {
		this.funds = funds;
	}
}
