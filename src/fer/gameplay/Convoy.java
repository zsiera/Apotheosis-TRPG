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

	public Item getItem(int index) {
		return items[index];
	}

	public void setItem(Item item, int index) {
		items[index] = item;
	}

	public void swapItems(int index1, int index2) {
		Item temp = items[index1];
		items[index1] = items[index2];
		items[index2] = temp;
	}

	public Weapon getWeapon(int index) {
		return weapons[index];
	}

	public void setWeapon(Weapon weapon, int index) {
		weapons[index] = weapon;
	}

	public void swapWeapons(int index1, int index2) {
		Weapon temp = weapons[index1];
		weapons[index1] = weapons[index2];
		weapons[index2] = temp;
	}

	public Armor getArmor(int index) {
		return armors[index];
	}

	public void setArmor(Armor armor, int index) {
		armors[index] = armor;
	}

	public void swapArmors(int index1, int index2) {
		Armor temp = armors[index1];
		armors[index1] = armors[index2];
		armors[index2] = temp;
	}

	public int getFunds() {
		return funds;
	}

	public void setFunds(int funds) {
		this.funds = funds;
	}
}
