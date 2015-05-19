/*
 * 
 */
package fer;

import fer.graphics.Animation;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.util.UnitClassData;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class UnitClass.
 *
 * @author Evan Stewart
 * 
 *         Stores the base stats and properties related to each class of unit.
 * 
 *         TODO: Read data from XML as opposed to the use of static constants.
 */
public class UnitClass {

	/** The movetype foot. */
	public static int[] MOVETYPE_FOOT = new int[] { 1, 2, 60 };

	/** The type. */
	private int type;
	
	/** The name. */
	private String name;
	
	/** The female. */
	private boolean female;
	
	/** The con. */
	private int hp, str, skl, spd, def, res, mov, con;
	
	/** The staff. */
	private int sword, axe, lance, bow, anima, light, dark, staff;
	
	/** The movetype. */
	private int[] movetype;
	
	/** The default map idle. */
	private Animation defaultMapIdle;
	
	/** The default map move up. */
	private Animation defaultMapMoveUp;
	
	/** The default map move left. */
	private Animation defaultMapMoveLeft;
	
	/** The default map move right. */
	private Animation defaultMapMoveRight;
	
	/** The default map move down. */
	private Animation defaultMapMoveDown;
	
	/** The default map selected. */
	private Animation defaultMapSelected;
	
	/** The default map attack up. */
	private Animation defaultMapAttackUp;
	
	/** The default map attack left. */
	private Animation defaultMapAttackLeft;
	
	/** The default map attack right. */
	private Animation defaultMapAttackRight;
	
	/** The default map attack down. */
	private Animation defaultMapAttackDown;
	
	/** The default map melee up. */
	private Animation defaultMapMeleeUp;
	
	/** The default map melee left. */
	private Animation defaultMapMeleeLeft;
	
	/** The default map melee right. */
	private Animation defaultMapMeleeRight;
	
	/** The default map melee down. */
	private Animation defaultMapMeleeDown;
	
	/** The default map evade up. */
	private Animation defaultMapEvadeUp;
	
	/** The default map evade left. */
	private Animation defaultMapEvadeLeft;
	
	/** The default map evade right. */
	private Animation defaultMapEvadeRight;
	
	/** The default map evade down. */
	private Animation defaultMapEvadeDown;
	
	/** The default map damage up. */
	private Animation defaultMapDamageUp;
	
	/** The default map damage left. */
	private Animation defaultMapDamageLeft;
	
	/** The default map damage right. */
	private Animation defaultMapDamageRight;
	
	/** The default map damage down. */
	private Animation defaultMapDamageDown;

	/*
	 * public UnitClass(int typeIndex) { type = typeIndex; initClass(); }
	 */

	/**
	 * Instantiates a new unit class.
	 *
	 * @param typeindex the typeindex
	 */
	public UnitClass(int typeindex) {
		UnitClassData data = Game.getUnitClassData(typeindex);
		type = data.getIndex();
		name = data.getName();
		female = data.isFemale();
		hp = data.getHp();
		str = data.getStr();
		skl = data.getSkl();
		spd = data.getSpd();
		def = data.getDef();
		res = data.getRes();
		mov = data.getMov();
		con = data.getCon();
		movetype = data.getMovetype();

		ArrayList<Sprite[]> anims = new ArrayList<>(22);
		SpriteSheet sheet = Game.getSpriteSheet(data.getSheetIndex());

		for (int i = 0; i < 22; i++) {
			Sprite[] sprites = new Sprite[data.getNumFrames()[i]];
			for (int j = 0; j < sprites.length; j++) {
				int y = 0;
				for (int k = 0; k < i; k++) {
					y += data.getSpriteHeights()[k];
				}
				sprites[j] = new Sprite(data.getSpriteWidths()[i],
						data.getSpriteHeights()[i],
						(data.getSheetSpacing() * (j + 1))
								+ (data.getSpriteWidths()[i] * j),
						(data.getSheetSpacing() * (i + 1)) + y, sheet);
			}
			anims.add(i, sprites);
		}

		defaultMapIdle = new Animation(data.getNumFrames()[0], anims.get(0),
				data.getFrameDurations()[0]);
		defaultMapIdle.setKeyFrame(data.getKeyFrames()[0]);
		defaultMapMoveDown = new Animation(data.getNumFrames()[1],
				anims.get(1), data.getFrameDurations()[1]);
		defaultMapMoveDown.setKeyFrame(data.getKeyFrames()[1]);
		defaultMapMoveLeft = new Animation(data.getNumFrames()[2],
				anims.get(2), data.getFrameDurations()[2]);
		defaultMapMoveLeft.setKeyFrame(data.getKeyFrames()[2]);
		defaultMapMoveRight = new Animation(data.getNumFrames()[3],
				anims.get(3), data.getFrameDurations()[3]);
		defaultMapMoveRight.setKeyFrame(data.getKeyFrames()[3]);
		defaultMapMoveUp = new Animation(data.getNumFrames()[4], anims.get(4),
				data.getFrameDurations()[4]);
		defaultMapMoveUp.setKeyFrame(data.getKeyFrames()[4]);
		defaultMapSelected = new Animation(data.getNumFrames()[5],
				anims.get(5), data.getFrameDurations()[5]);
		defaultMapSelected.setKeyFrame(data.getKeyFrames()[5]);
		defaultMapAttackDown = new Animation(data.getNumFrames()[6],
				anims.get(6), data.getFrameDurations()[6]);
		defaultMapAttackDown.setKeyFrame(data.getKeyFrames()[6]);
		defaultMapAttackLeft = new Animation(data.getNumFrames()[7],
				anims.get(7), data.getFrameDurations()[7]);
		defaultMapAttackLeft.setKeyFrame(data.getKeyFrames()[7]);
		defaultMapAttackRight = new Animation(data.getNumFrames()[8],
				anims.get(8), data.getFrameDurations()[8]);
		defaultMapAttackRight.setKeyFrame(data.getKeyFrames()[8]);
		defaultMapAttackUp = new Animation(data.getNumFrames()[9],
				anims.get(9), data.getFrameDurations()[9]);
		defaultMapAttackUp.setKeyFrame(data.getKeyFrames()[9]);
		defaultMapMeleeDown = new Animation(data.getNumFrames()[10],
				anims.get(10), data.getFrameDurations()[10]);
		defaultMapMeleeDown.setKeyFrame(data.getKeyFrames()[10]);
		defaultMapMeleeLeft = new Animation(data.getNumFrames()[11],
				anims.get(11), data.getFrameDurations()[11]);
		defaultMapMeleeLeft.setKeyFrame(data.getKeyFrames()[11]);
		defaultMapMeleeRight = new Animation(data.getNumFrames()[12],
				anims.get(12), data.getFrameDurations()[12]);
		defaultMapMeleeRight.setKeyFrame(data.getKeyFrames()[12]);
		defaultMapMeleeUp = new Animation(data.getNumFrames()[13],
				anims.get(13), data.getFrameDurations()[13]);
		defaultMapMeleeUp.setKeyFrame(data.getKeyFrames()[13]);
		defaultMapEvadeDown = new Animation(data.getNumFrames()[14],
				anims.get(14), data.getFrameDurations()[14]);
		defaultMapEvadeDown.setKeyFrame(data.getKeyFrames()[14]);
		defaultMapEvadeLeft = new Animation(data.getNumFrames()[15],
				anims.get(15), data.getFrameDurations()[15]);
		defaultMapEvadeLeft.setKeyFrame(data.getKeyFrames()[15]);
		defaultMapEvadeRight = new Animation(data.getNumFrames()[16],
				anims.get(16), data.getFrameDurations()[16]);
		defaultMapEvadeRight.setKeyFrame(data.getKeyFrames()[16]);
		defaultMapEvadeUp = new Animation(data.getNumFrames()[17],
				anims.get(17), data.getFrameDurations()[17]);
		defaultMapEvadeUp.setKeyFrame(data.getKeyFrames()[17]);
		defaultMapDamageDown = new Animation(data.getNumFrames()[18],
				anims.get(18), data.getFrameDurations()[18]);
		defaultMapDamageDown.setKeyFrame(data.getKeyFrames()[18]);
		defaultMapDamageLeft = new Animation(data.getNumFrames()[19],
				anims.get(19), data.getFrameDurations()[19]);
		defaultMapDamageLeft.setKeyFrame(data.getKeyFrames()[19]);
		defaultMapDamageRight = new Animation(data.getNumFrames()[20],
				anims.get(20), data.getFrameDurations()[20]);
		defaultMapDamageRight.setKeyFrame(data.getKeyFrames()[20]);
		defaultMapDamageUp = new Animation(data.getNumFrames()[21],
				anims.get(21), data.getFrameDurations()[21]);
		defaultMapDamageUp.setKeyFrame(data.getKeyFrames()[21]);

	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks if is female.
	 *
	 * @return true, if is female
	 */
	public boolean isFemale() {
		return female;
	}

	/**
	 * Gets the hp.
	 *
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Gets the str.
	 *
	 * @return the str
	 */
	public int getStr() {
		return str;
	}

	/**
	 * Gets the skl.
	 *
	 * @return the skl
	 */
	public int getSkl() {
		return skl;
	}

	/**
	 * Gets the spd.
	 *
	 * @return the spd
	 */
	public int getSpd() {
		return spd;
	}

	/**
	 * Gets the def.
	 *
	 * @return the def
	 */
	public int getDef() {
		return def;
	}

	/**
	 * Gets the res.
	 *
	 * @return the res
	 */
	public int getRes() {
		return res;
	}

	/**
	 * Gets the mov.
	 *
	 * @return the mov
	 */
	public int getMov() {
		return mov;
	}

	/**
	 * Gets the con.
	 *
	 * @return the con
	 */
	public int getCon() {
		return con;
	}

	/**
	 * Gets the sword.
	 *
	 * @return the sword
	 */
	public int getSword() {
		return sword;
	}

	/**
	 * Gets the axe.
	 *
	 * @return the axe
	 */
	public int getAxe() {
		return axe;
	}

	/**
	 * Gets the lance.
	 *
	 * @return the lance
	 */
	public int getLance() {
		return lance;
	}

	/**
	 * Gets the bow.
	 *
	 * @return the bow
	 */
	public int getBow() {
		return bow;
	}

	/**
	 * Gets the anima.
	 *
	 * @return the anima
	 */
	public int getAnima() {
		return anima;
	}

	/**
	 * Gets the light.
	 *
	 * @return the light
	 */
	public int getLight() {
		return light;
	}

	/**
	 * Gets the dark.
	 *
	 * @return the dark
	 */
	public int getDark() {
		return dark;
	}

	/**
	 * Gets the staff.
	 *
	 * @return the staff
	 */
	public int getStaff() {
		return staff;
	}

	/**
	 * Gets the default map idle.
	 *
	 * @return the default map idle
	 */
	public Animation getDefaultMapIdle() {
		return defaultMapIdle;
	}

	/**
	 * Gets the default map move up.
	 *
	 * @return the default map move up
	 */
	public Animation getDefaultMapMoveUp() {
		return defaultMapMoveUp;
	}

	/**
	 * Gets the default map move left.
	 *
	 * @return the default map move left
	 */
	public Animation getDefaultMapMoveLeft() {
		return defaultMapMoveLeft;
	}

	/**
	 * Gets the default map move right.
	 *
	 * @return the default map move right
	 */
	public Animation getDefaultMapMoveRight() {
		return defaultMapMoveRight;
	}

	/**
	 * Gets the default map move down.
	 *
	 * @return the default map move down
	 */
	public Animation getDefaultMapMoveDown() {
		return defaultMapMoveDown;
	}

	/**
	 * Gets the default map selected.
	 *
	 * @return the default map selected
	 */
	public Animation getDefaultMapSelected() {
		return defaultMapSelected;
	}

	/**
	 * Gets the default map attack up.
	 *
	 * @return the default map attack up
	 */
	public Animation getDefaultMapAttackUp() {
		return defaultMapAttackUp;
	}

	/**
	 * Gets the default map attack left.
	 *
	 * @return the default map attack left
	 */
	public Animation getDefaultMapAttackLeft() {
		return defaultMapAttackLeft;
	}

	/**
	 * Gets the default map attack right.
	 *
	 * @return the default map attack right
	 */
	public Animation getDefaultMapAttackRight() {
		return defaultMapAttackRight;
	}

	/**
	 * Gets the default map attack down.
	 *
	 * @return the default map attack down
	 */
	public Animation getDefaultMapAttackDown() {
		return defaultMapAttackDown;
	}

	/**
	 * Gets the default map melee up.
	 *
	 * @return the default map melee up
	 */
	public Animation getDefaultMapMeleeUp() {
		return defaultMapMeleeUp;
	}

	/**
	 * Gets the default map melee left.
	 *
	 * @return the default map melee left
	 */
	public Animation getDefaultMapMeleeLeft() {
		return defaultMapMeleeLeft;
	}

	/**
	 * Gets the default map melee right.
	 *
	 * @return the default map melee right
	 */
	public Animation getDefaultMapMeleeRight() {
		return defaultMapMeleeRight;
	}

	/**
	 * Gets the default map melee down.
	 *
	 * @return the default map melee down
	 */
	public Animation getDefaultMapMeleeDown() {
		return defaultMapMeleeDown;
	}

	/**
	 * Gets the default map evade up.
	 *
	 * @return the default map evade up
	 */
	public Animation getDefaultMapEvadeUp() {
		return defaultMapEvadeUp;
	}

	/**
	 * Gets the default map evade left.
	 *
	 * @return the default map evade left
	 */
	public Animation getDefaultMapEvadeLeft() {
		return defaultMapEvadeLeft;
	}

	/**
	 * Gets the default map evade right.
	 *
	 * @return the default map evade right
	 */
	public Animation getDefaultMapEvadeRight() {
		return defaultMapEvadeRight;
	}

	/**
	 * Gets the default map evade down.
	 *
	 * @return the default map evade down
	 */
	public Animation getDefaultMapEvadeDown() {
		return defaultMapEvadeDown;
	}

	/**
	 * Gets the default map damage up.
	 *
	 * @return the default map damage up
	 */
	public Animation getDefaultMapDamageUp() {
		return defaultMapDamageUp;
	}

	/**
	 * Gets the default map damage left.
	 *
	 * @return the default map damage left
	 */
	public Animation getDefaultMapDamageLeft() {
		return defaultMapDamageLeft;
	}

	/**
	 * Gets the default map damage right.
	 *
	 * @return the default map damage right
	 */
	public Animation getDefaultMapDamageRight() {
		return defaultMapDamageRight;
	}

	/**
	 * Gets the default map damage down.
	 *
	 * @return the default map damage down
	 */
	public Animation getDefaultMapDamageDown() {
		return defaultMapDamageDown;
	}

	/**
	 * Gets the movetype.
	 *
	 * @return the movetype
	 */
	public int[] getMovetype() {
		return movetype;
	}

	/**
	 * Gets the move cost.
	 *
	 * @param index the index
	 * @return the move cost
	 */
	public int getMoveCost(int index) {
		return movetype[index];
	}
}
