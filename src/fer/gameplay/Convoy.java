package fer.gameplay;

/**
 * @author Evan Stewart A data storage class that holds an inventory of items,
 *         weapons, and armor as well as \a quantity of funds. Used to keep
 *         track of inventories and for each faction on a map as well as shop
 *         inventories.
 */
public class Convoy {

	public static final int STORAGE_LIMIT = 100;
	public static final int FUNDS_LIMIT = 999999;

	private Item[] items;
	private Weapon[] weapons;
	private Armor[] armors;
	private int funds;

	public final Item getItem(final int index) {
		return items[index];
	}

	public final void setItem(final Item item, final int index) {
		items[index] = item;
	}

	public final void swapItems(final int index1, final int index2) {
		Item temp = items[index1];
		items[index1] = items[index2];
		items[index2] = temp;
	}

	public final Weapon getWeapon(final int index) {
		return weapons[index];
	}

	public final void setWeapon(final Weapon weapon, final int index) {
		weapons[index] = weapon;
	}

	public final void swapWeapons(final int index1, final int index2) {
		Weapon temp = weapons[index1];
		weapons[index1] = weapons[index2];
		weapons[index2] = temp;
	}

	public final Armor getArmor(final int index) {
		return armors[index];
	}

	public final void setArmor(final Armor armor, final int index) {
		armors[index] = armor;
	}

	public final void swapArmors(final int index1, final int index2) {
		Armor temp = armors[index1];
		armors[index1] = armors[index2];
		armors[index2] = temp;
	}

	public final int getFunds() {
		return funds;
	}

	public final void setFunds(final int funds) {
		this.funds = funds;
	}
}
