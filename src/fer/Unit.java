package fer;

import fer.ai.AiPlayer;
import fer.ai.PathFinder;
import fer.gameplay.Armor;
import fer.gameplay.Item;
import fer.gameplay.Weapon;
import fer.graphics.Animation;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.util.UnitData;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Evan
 *
 *         Stores the properties, state, location, etc. of a particular unit in
 *         the game.
 *
 *         TODO: Load from XML.
 */
public class Unit {

	public static final int EXP_CAP = 40;
	private int mapIndex;
	private String name;
	private UnitClass unitClass;
	private int level, exp;
	private int hp, str, skl, spd, def, res, mov, con;
	private int curHp;
	private int sword, axe, lance, bow, anima, light, dark, staff;
	private int mapx = 0;
	private int mapy = 0;
	private int xOffset = 0;
	private int yOffset = 0;
	private int factionNum;
	private boolean moved = false;
	private boolean dead = false;
	private Animation mapIdle;
	private Animation mapSelected;
	private Animation mapMoveUp;
	private Animation mapMoveDown;
	private Animation mapMoveLeft;
	private Animation mapMoveRight;
	private Animation mapAttackUp;
	private Animation mapAttackDown;
	private Animation mapAttackLeft;
	private Animation mapAttackRight;
	private Animation mapMeleeUp;
	private Animation mapMeleeDown;
	private Animation mapMeleeLeft;
	private Animation mapMeleeRight;
	private Animation mapEvadeUp;
	private Animation mapEvadeDown;
	private Animation mapEvadeLeft;
	private Animation mapEvadeRight;
	private Animation mapDamageUp;
	private Animation mapDamageDown;
	private Animation mapDamageLeft;
	private Animation mapDamageRight;
	private Animation activeAnimation;
	private Sprite mapSprite;
	private Sprite mapFaceSprite;
	private Weapon[] weapons;
	private Item[] items;
	private Armor armor;
	private Menu levelMenu;
	private Menu statusMenu;
	private Menu infoMenu;
	private boolean showingLevelMenu = false;

	public Unit(UnitClass uClass, int iLevel, int iFaction) {
		unitClass = uClass;
		if (iLevel < 1) {
			level = 1;
		} else if (iLevel > 20) {
			level = 20;
		} else {
			level = iLevel;
		}

		factionNum = iFaction;

		hp = uClass.getHp();
		curHp = hp;
		str = uClass.getStr();
		skl = uClass.getSkl();
		spd = uClass.getSpd();
		def = uClass.getDef();
		res = uClass.getRes();
		mov = uClass.getMov();
		con = uClass.getCon();

		sword = uClass.getSword();
		axe = uClass.getAxe();
		lance = uClass.getLance();
		bow = uClass.getBow();
		anima = uClass.getAnima();
		light = uClass.getLight();
		dark = uClass.getDark();
		staff = uClass.getStaff();

		mapIdle = uClass.getDefaultMapIdle();
		mapSelected = uClass.getDefaultMapSelected();
		mapMoveUp = uClass.getDefaultMapMoveUp();
		mapMoveDown = uClass.getDefaultMapMoveDown();
		mapMoveLeft = uClass.getDefaultMapMoveLeft();
		mapMoveRight = uClass.getDefaultMapMoveRight();
		mapAttackUp = uClass.getDefaultMapAttackUp();
		mapAttackDown = uClass.getDefaultMapAttackDown();
		mapAttackLeft = uClass.getDefaultMapAttackLeft();
		mapAttackRight = uClass.getDefaultMapAttackRight();
		mapMeleeUp = uClass.getDefaultMapMeleeUp();
		mapMeleeDown = uClass.getDefaultMapMeleeDown();
		mapMeleeLeft = uClass.getDefaultMapMeleeLeft();
		mapMeleeRight = uClass.getDefaultMapMeleeRight();
		mapEvadeUp = uClass.getDefaultMapEvadeUp();
		mapEvadeDown = uClass.getDefaultMapEvadeDown();
		mapEvadeLeft = uClass.getDefaultMapEvadeLeft();
		mapEvadeRight = uClass.getDefaultMapEvadeRight();
		mapDamageUp = uClass.getDefaultMapDamageUp();
		mapDamageDown = uClass.getDefaultMapDamageDown();
		mapDamageLeft = uClass.getDefaultMapDamageLeft();
		mapDamageRight = uClass.getDefaultMapDamageRight();

		activeAnimation = mapIdle;
		mapSprite = activeAnimation.getCurrentFrameSprite();

		weapons = new Weapon[5];
		items = new Item[5];
	}

	public Unit(UnitData data) {
		mapIndex = data.getIndex();
		name = data.getName();
		level = data.getLevel();
		exp = data.getExp();
		if (level < 1) {
			level = 1;
		} else if (level > 20) {
			level = 20;
		}
		if (exp < 0) {
			exp = 0;
		} else if (exp >= EXP_CAP) {
			exp = 19;
		}
		unitClass = new UnitClass(data.getUnitClass());
		if (data.isAutoLevel()) {
			int levelCap = level;
			level = 1;
			hp = unitClass.getHp();
			str = unitClass.getStr();
			skl = unitClass.getSkl();
			spd = unitClass.getSpd();
			def = unitClass.getDef();
			res = unitClass.getRes();
			con = unitClass.getCon();
			mov = unitClass.getMov();
			for (int i = level; i <= levelCap - 1; i++) {
				levelUnit(false);
			}
		} else {
			hp = data.getHp();
			str = data.getStr();
			skl = data.getSkl();
			spd = data.getSpd();
			def = data.getDef();
			res = data.getRes();
			con = data.getCon();
			mov = data.getMov();
		}
		curHp = hp;

		mapIdle = unitClass.getDefaultMapIdle();
		mapSelected = unitClass.getDefaultMapSelected();
		mapMoveUp = unitClass.getDefaultMapMoveUp();
		mapMoveDown = unitClass.getDefaultMapMoveDown();
		mapMoveLeft = unitClass.getDefaultMapMoveLeft();
		mapMoveRight = unitClass.getDefaultMapMoveRight();
		mapAttackUp = unitClass.getDefaultMapAttackUp();
		mapAttackDown = unitClass.getDefaultMapAttackDown();
		mapAttackLeft = unitClass.getDefaultMapAttackLeft();
		mapAttackRight = unitClass.getDefaultMapAttackRight();
		mapMeleeUp = unitClass.getDefaultMapMeleeUp();
		mapMeleeDown = unitClass.getDefaultMapMeleeDown();
		mapMeleeLeft = unitClass.getDefaultMapMeleeLeft();
		mapMeleeRight = unitClass.getDefaultMapMeleeRight();
		mapEvadeUp = unitClass.getDefaultMapEvadeUp();
		mapEvadeDown = unitClass.getDefaultMapEvadeDown();
		mapEvadeLeft = unitClass.getDefaultMapEvadeLeft();
		mapEvadeRight = unitClass.getDefaultMapEvadeRight();
		mapDamageUp = unitClass.getDefaultMapDamageUp();
		mapDamageDown = unitClass.getDefaultMapDamageDown();
		mapDamageLeft = unitClass.getDefaultMapDamageLeft();
		mapDamageRight = unitClass.getDefaultMapDamageRight();

		if (data.isHasFaceSprite()) {
			mapFaceSprite = new Sprite(data.getSpriteWidth(),
					data.getSpriteHeight(), data.getX(), data.getY(),
					new SpriteSheet(data.getSheetPath(), data.getSheetWidth(),
							data.getSheetHeight(),
							data.getSheetTransparentColor()));
		}

		weapons = new Weapon[5];
		for (int i = 0; i < weapons.length; i++) {
			if (data.getWeapons()[i] == -1) {
				weapons[i] = null;
			} else {
				weapons[i] = new Weapon(data.getWeapons()[i]);
			}
		}
		items = new Item[5];
		for (int i = 0; i < items.length; i++) {
			if (data.getItems()[i] == -1) {
				items[i] = null;
			} else {
				items[i] = new Item(data.getItems()[i]);
			}
		}
		if (data.getArmor() == -1) {
			armor = null;
		} else {
			armor = new Armor(data.getArmor());
		}

		mapx = data.getMapx();
		mapy = data.getMapy();
		factionNum = data.getFaction();

		activeAnimation = mapIdle;
		mapSprite = activeAnimation.getCurrentFrameSprite();
	}

	public void updateAnimation() {
		activeAnimation.updateAnimation();
		mapSprite = activeAnimation.getCurrentFrameSprite();
	}

	/**
	 * Sets the animation that the unit will pull its map sprite from.
	 *
	 * @param animIndex
	 *            : The index corresponding to the desired animation. 0 = Idle,
	 *            1 = Selected, 2 = Move Up, 3 = Move Down, 4 = Move Left, 5 =
	 *            Move Right.
	 *
	 */
	public void setActiveMapAnimation(int animIndex) {
		switch (animIndex) {
		case 0:
			activeAnimation = mapIdle;
			break;
		case 1:
			activeAnimation = mapSelected;
			break;
		case 2:
			activeAnimation = mapMoveUp;
			break;
		case 3:
			activeAnimation = mapMoveDown;
			break;
		case 4:
			activeAnimation = mapMoveLeft;
			break;
		case 5:
			activeAnimation = mapMoveRight;
			break;
		case 6:
			activeAnimation = mapAttackUp;
			break;
		case 7:
			activeAnimation = mapAttackDown;
			break;
		case 8:
			activeAnimation = mapAttackLeft;
			break;
		case 9:
			activeAnimation = mapAttackRight;
			break;
		case 10:
			activeAnimation = mapMeleeUp;
			break;
		case 11:
			activeAnimation = mapMeleeDown;
			break;
		case 12:
			activeAnimation = mapMeleeLeft;
			break;
		case 13:
			activeAnimation = mapMeleeRight;
			break;
		case 14:
			activeAnimation = mapEvadeUp;
			break;
		case 15:
			activeAnimation = mapEvadeDown;
			break;
		case 16:
			activeAnimation = mapEvadeLeft;
			break;
		case 17:
			activeAnimation = mapEvadeRight;
			break;
		case 18:
			activeAnimation = mapDamageUp;
			break;
		case 19:
			activeAnimation = mapDamageDown;
			break;
		case 20:
			activeAnimation = mapDamageLeft;
			break;
		case 21:
			activeAnimation = mapDamageRight;
			break;
		default:
			activeAnimation = mapIdle;
			break;
		}
		mapSprite = activeAnimation.getCurrentFrameSprite();
	}

	/**
	 * Resets the given animation.
	 *
	 * @param animIndex
	 *            : The index corresponding to the desired animation. 0 = Idle,
	 *            1 = Selected, 2 = Move Up, 3 = Move Down, 4 = Move Left, 5 =
	 *            Move Right, 22 = Reset All.
	 *
	 */
	public void resetAnimation(int animIndex) {
		switch (animIndex) {
		case 0:
			mapIdle.resetAnimation();
			break;
		case 1:
			mapSelected.resetAnimation();
			break;
		case 2:
			mapMoveUp.resetAnimation();
			break;
		case 3:
			mapMoveDown.resetAnimation();
			break;
		case 4:
			mapMoveLeft.resetAnimation();
			break;
		case 5:
			mapMoveRight.resetAnimation();
			break;
		case 6:
			mapAttackUp.resetAnimation();
			break;
		case 7:
			mapAttackDown.resetAnimation();
			break;
		case 8:
			mapAttackLeft.resetAnimation();
			break;
		case 9:
			mapAttackRight.resetAnimation();
			break;
		case 10:
			mapMeleeUp.resetAnimation();
			break;
		case 11:
			mapMeleeDown.resetAnimation();
			break;
		case 12:
			mapMeleeLeft.resetAnimation();
			break;
		case 13:
			mapMeleeRight.resetAnimation();
			break;
		case 14:
			mapEvadeUp.resetAnimation();
			break;
		case 15:
			mapEvadeDown.resetAnimation();
			break;
		case 16:
			mapEvadeLeft.resetAnimation();
			break;
		case 17:
			mapEvadeRight.resetAnimation();
			break;
		case 18:
			mapDamageUp.resetAnimation();
			break;
		case 19:
			mapDamageDown.resetAnimation();
			break;
		case 20:
			mapDamageLeft.resetAnimation();
			break;
		case 21:
			mapDamageRight.resetAnimation();
			break;
		case 22:
			mapIdle.resetAnimation();
			mapSelected.resetAnimation();
			mapMoveUp.resetAnimation();
			mapMoveDown.resetAnimation();
			mapMoveLeft.resetAnimation();
			mapMoveRight.resetAnimation();
			mapAttackUp.resetAnimation();
			mapAttackDown.resetAnimation();
			mapAttackLeft.resetAnimation();
			mapAttackRight.resetAnimation();
			mapMeleeUp.resetAnimation();
			mapMeleeDown.resetAnimation();
			mapMeleeLeft.resetAnimation();
			mapMeleeRight.resetAnimation();
			mapEvadeUp.resetAnimation();
			mapEvadeDown.resetAnimation();
			mapEvadeLeft.resetAnimation();
			mapEvadeRight.resetAnimation();
			mapDamageUp.resetAnimation();
			mapDamageDown.resetAnimation();
			mapDamageLeft.resetAnimation();
			mapDamageRight.resetAnimation();
			break;
		default:
			mapIdle.resetAnimation();
			break;
		}
		mapSprite = activeAnimation.getCurrentFrameSprite();
	}

	public void levelUnit(boolean showMenu) {
		Random r = new Random();
		r.setSeed(System.nanoTime());
		int hpDif = 0, strDif = 0, sklDif = 0, spdDif = 0, defDif = 0, resDif = 0;
		int rand = 0;
		while (rand == 0) { // Prevent gainless level ups
			rand = r.nextInt();
			rand &= 0b111111;
		}
		if ((rand & 1) == 1) {
			hpDif++;
		}
		if (((rand >> 1) & 1) == 1) {
			strDif++;
		}
		if (((rand >> 2) & 1) == 1) {
			sklDif++;
		}
		if (((rand >> 3) & 1) == 1) {
			spdDif++;
		}
		if (((rand >> 4) & 1) == 1) {
			defDif++;
		}
		if (((rand >> 5) & 1) == 1) {
			resDif++;
		}
		if (showMenu) {
			if (levelMenu != null) {
				levelMenu.removeMenu();
			}
			showingLevelMenu = true;
			levelMenu = new Menu(114, 80, 63, 43);

			MenuAction sa = new MenuAction() {
				@Override
				public void execute(MenuElement caller) {
					// Do nothing
				}
			};

			MenuAction close = new MenuAction() {
				@Override
				public void execute(MenuElement caller) {
					levelMenu.removeMenu();
					showingLevelMenu = false;
				}
			};

			levelMenu.setEscapeAction(close);

			levelMenu.addElement(new MenuElement(sa, close, new TextGraphic(
					"LEVEL UP!", Font.BASICFONT).getSprite(), true, 7, 7));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(name,
					Font.BASICFONT).getSprite(), false, 7, 13));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					unitClass.getName(), Font.BASICFONT).getSprite(), false,
					57, 13));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("LEVEL " + level + " -> LEVEL " + (level + 1)),
					Font.BASICFONT).getSprite(), false, 7, 19));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("HP  " + hp + " -> " + (hp + hpDif)), Font.BASICFONT)
					.getSprite(), false, 12, 25));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("STR " + str + " -> " + (str + strDif)), Font.BASICFONT)
					.getSprite(), false, 12, 31));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("SKL " + skl + " -> " + (skl + sklDif)), Font.BASICFONT)
					.getSprite(), false, 12, 37));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("SPD " + spd + " -> " + (spd + spdDif)), Font.BASICFONT)
					.getSprite(), false, 12, 43));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("DEF " + def + " -> " + (def + defDif)), Font.BASICFONT)
					.getSprite(), false, 12, 49));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("RES " + res + " -> " + (res + sklDif)), Font.BASICFONT)
					.getSprite(), false, 12, 55));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("CON " + con + " -> " + con), Font.BASICFONT).getSprite(),
					false, 12, 61));
			levelMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					("MOV " + mov + " -> " + mov), Font.BASICFONT).getSprite(),
					false, 12, 67));
			levelMenu.addElement(new MenuElement(sa, sa, mapIdle.getSprite(0),
					false, 107 - mapIdle.getSprite(0).getWidth(), 73 - mapIdle
							.getSprite(0).getHeight()));

			MenuCursor.getMenuCursor().setElementIndex(0);
			MenuCursor.setActiveMenu(levelMenu);
		}
		hp = Math.min(hp + hpDif, 60);
		str = Math.min(str + strDif, 40);
		skl = Math.min(skl + sklDif, 40);
		spd = Math.min(spd + spdDif, 40);
		def = Math.min(def + defDif, 40);
		res = Math.min(res + resDif, 40);
		level++;
	}

	public void updateExp(int exp, boolean showMenu) {
		this.exp += exp;
		if (exp >= EXP_CAP) {
		}
	}

	public int getActiveAnimationFrames() {
		return activeAnimation.getFrames();
	}

	/**
	 * @param frameCount
	 *            The number of frames to count updates for.
	 * @return The number of updates required to traverse the specified number
	 *         of frames, beginning at frame zero.
	 */
	public int getActiveAnimationUpdates(int frameCount) {
		return activeAnimation.getUpdates(frameCount);
	}

	public int getActiveAnimationKeyFrame() {
		return activeAnimation.getKeyFrame();
	}

	public Sprite getMapSprite() {
		return mapSprite;
	}

	public int getMapIndex() {
		return mapIndex;
	}

	public int getMapx() {
		return mapx;
	}

	public int getMapy() {
		return mapy;
	}

	public void setMapx(int mapx) {
		this.mapx = mapx;
		if (Game.getCurrentMap() != null) {
			Game.getCurrentMap().updateUnitDisplay();
		}
	}

	public void setMapy(int mapy) {
		this.mapy = mapy;
		if (Game.getCurrentMap() != null) {
			Game.getCurrentMap().updateUnitDisplay();
		}
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getHp() {
		return hp;
	}

	public int getStr() {
		return str;
	}

	public int getSkl() {
		return skl;
	}

	public int getSpd() {
		return spd;
	}

	public int getDef() {
		return def;
	}

	public int getRes() {
		return res;
	}

	public int getMov() {
		return mov;
	}

	public int getCon() {
		return con;
	}

	public int getSword() {
		return sword;
	}

	public int getAxe() {
		return axe;
	}

	public int getLance() {
		return lance;
	}

	public int getBow() {
		return bow;
	}

	public int getAnima() {
		return anima;
	}

	public int getLight() {
		return light;
	}

	public int getDark() {
		return dark;
	}

	public int getStaff() {
		return staff;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setStr(int str) {
		this.str = str;
	}

	public void setSkl(int skl) {
		this.skl = skl;
	}

	public void setSpd(int spd) {
		this.spd = spd;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public void setMov(int mov) {
		this.mov = mov;
	}

	public void setCon(int con) {
		this.con = con;
	}

	public void setSword(int sword) {
		this.sword = sword;
	}

	public void setAxe(int axe) {
		this.axe = axe;
	}

	public void setLance(int lance) {
		this.lance = lance;
	}

	public void setBow(int bow) {
		this.bow = bow;
	}

	public void setAnima(int anima) {
		this.anima = anima;
	}

	public void setLight(int light) {
		this.light = light;
	}

	public void setDark(int dark) {
		this.dark = dark;
	}

	public void setStaff(int staff) {
		this.staff = staff;
	}

	public UnitClass getUnitClass() {
		return unitClass;
	}

	public int getFaction() {
		return factionNum;
	}

	public void setFaction(int iFaction) {
		factionNum = iFaction;
	}

	public Weapon[] getWeapons() {
		return weapons;
	}

	public void setWeapons(Weapon[] weapons) {
		this.weapons = weapons;
	}

	public Weapon getWeapon(int index) {
		return weapons[index];
	}

	public void setWeapon(int index, Weapon iWeapon) {
		weapons[index] = iWeapon;
	}

	public Item[] getItems() {
		return items;
	}

	public void setItems(Item[] items) {
		this.items = items;
	}

	public Item getItem(int index) {
		return items[index];
	}

	public void setItem(int index, Item iItem) {
		items[index] = iItem;
	}

	public Armor getArmor() {
		return armor;
	}

	public void setArmor(Armor armor) {
		this.armor = armor;
	}

	public Sprite getMapFaceSprite() {
		return mapFaceSprite;
	}

	public void setMapFaceSprite(Sprite mapFaceSprite) {
		this.mapFaceSprite = mapFaceSprite;
	}

	public int getCurrentHp() {
		return curHp;
	}

	public void setCurrentHp(int iHp) {
		curHp = iHp;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void drawStatusMenu(Menu callingMenu, boolean help) {
		statusMenu = new Menu(220, 140, 10, 10);
		final Menu cMenu = callingMenu;
		final MenuElement[] weaponList = new MenuElement[5];
		final MenuElement[] itemList = new MenuElement[5];

		final MenuAction nil = new MenuAction() {
			@Override
			public void execute(MenuElement caller) { // Do nothing
			}
		};
		MenuAction back = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				if (cMenu == null) {
					statusMenu.removeMenu();
					MenuCursor.getMenuCursor().setActive(false);
					Cursor.getCursor().setActive(true);
				} else {
					statusMenu.removeMenu();
					MenuCursor.getMenuCursor().setElementIndex(0);
					cMenu.setVisible(true);
					MenuCursor.setActiveMenu(cMenu);
				}
			}
		};
		MenuAction helpMode = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				statusMenu.removeMenu();
				drawStatusMenu(cMenu, true);
			}
		};
		MenuAction exitHelpMode = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				for (int i = 0; i < weapons.length; i++) {
					if (weapons[i] != null) {
						if (weapons[i].infoMenuDrawn()) {
							weapons[i].clearInfoMenu();
						}
					}
				}
				statusMenu.removeMenu();
				drawStatusMenu(cMenu, false);
			}
		};
		MenuAction clearInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				for (int i = 0; i < weapons.length; i++) {
					if (weapons[i] != null) {
						if (weapons[i].infoMenuDrawn()) {
							weapons[i].clearInfoMenu();
						}
					}
					if (items[i] != null) {
						if (items[i].infoMenuDrawn()) {
							items[i].clearInfoMenu();
						}
					}
				}

			}
		};
		MenuAction levelInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 50, (caller.getX() + 10),
						(caller.getY() + 10) + caller.getGraphic().getHeight());
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"The level of", Font.BASICFONT)).getSprite(), true, 7,
						7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"the unit.  Cycles", Font.BASICFONT)).getSprite(),
						true, 7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"based on gained", Font.BASICFONT)).getSprite(), true,
						7, 19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"experience.  Each", Font.BASICFONT)).getSprite(),
						true, 7, 25));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"level brings stat", Font.BASICFONT)).getSprite(),
						true, 7, 31));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"gains.", Font.BASICFONT)).getSprite(), true, 7, 37));
			}
		};
		MenuAction expInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 50, (caller.getX() + 10) - 100,
						(caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"The current", Font.BASICFONT)).getSprite(), true, 7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"progress of the", Font.BASICFONT)).getSprite(), true,
						7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"unit towards its", Font.BASICFONT)).getSprite(), true,
						7, 19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"next level.", Font.BASICFONT)).getSprite(), true, 7,
						25));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Gained through", Font.BASICFONT)).getSprite(), true,
						7, 31));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"combat.", Font.BASICFONT)).getSprite(), true, 7, 37));
			}
		};
		MenuAction factionInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 32, (caller.getX() + 10) - 100,
						(caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"The faction to", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"which this unit", Font.BASICFONT)).getSprite(), true,
						7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"belongs.", Font.BASICFONT)).getSprite(), true, 7, 19));
			}
		};
		MenuAction coordinateInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(90, 32, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"The unit's X and", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Y coordinates on", Font.BASICFONT)).getSprite(), true,
						7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"the map.", Font.BASICFONT)).getSprite(), true, 7, 19));
			}
		};
		MenuAction hpInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 38, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"The unit's", Font.BASICFONT)).getSprite(), true, 7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"current health.", Font.BASICFONT)).getSprite(), true,
						7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Death occurs when", Font.BASICFONT)).getSprite(),
						true, 7, 19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"this reaches zero.", Font.BASICFONT)).getSprite(),
						true, 7, 25));
			}
		};
		MenuAction strInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 38, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Determines the", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"base attack", Font.BASICFONT)).getSprite(), true, 7,
						13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"damage dealt by", Font.BASICFONT)).getSprite(), true,
						7, 19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"the unit.", Font.BASICFONT)).getSprite(), true, 7, 25));
			}
		};
		MenuAction sklInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 38, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Determines the", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"hit, dodge, and", Font.BASICFONT)).getSprite(), true,
						7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"critical chance", Font.BASICFONT)).getSprite(), true,
						7, 19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"of the unit.", Font.BASICFONT)).getSprite(), true, 7,
						25));
			}
		};
		MenuAction spdInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 38, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Determines the", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"number of times", Font.BASICFONT)).getSprite(), true,
						7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"the unit may", Font.BASICFONT)).getSprite(), true, 7,
						19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"attack.", Font.BASICFONT)).getSprite(), true, 7, 25));
			}
		};
		MenuAction defInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 38, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Determines the", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"base enemy", Font.BASICFONT)).getSprite(), true, 7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"damage reduction", Font.BASICFONT)).getSprite(), true,
						7, 19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"of the unit", Font.BASICFONT)).getSprite(), true, 7,
						25));
			}
		};
		MenuAction resInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 38, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Determines many", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"factors such as", Font.BASICFONT)).getSprite(), true,
						7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"critical avoid", Font.BASICFONT)).getSprite(), true,
						7, 19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"chance.", Font.BASICFONT)).getSprite(), true, 7, 25));
			}
		};
		MenuAction conInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				infoMenu = new Menu(100, 38, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Determines the", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"unit's ability to", Font.BASICFONT)).getSprite(),
						true, 7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"weild heavy", Font.BASICFONT)).getSprite(), true, 7,
						19));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"weapons.", Font.BASICFONT)).getSprite(), true, 7, 25));
			}
		};
		MenuAction movInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				if (infoMenu != null) {
					infoMenu.removeMenu();
				}
				for (int i = 0; i < weapons.length; i++) {
					if (weapons[i] != null) {
						if (weapons[i].infoMenuDrawn()) {
							weapons[i].clearInfoMenu();
						}
					}
					if (items[i] != null) {
						if (items[i].infoMenuDrawn()) {
							items[i].clearInfoMenu();
						}
					}
				}
				infoMenu = new Menu(100, 32, (caller.getX() + 10)
						+ caller.getGraphic().getWidth(), (caller.getY() + 10));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"Determines how", Font.BASICFONT)).getSprite(), true,
						7, 7));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"far the unit may", Font.BASICFONT)).getSprite(), true,
						7, 13));
				infoMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
						"move in a turn.", Font.BASICFONT)).getSprite(), true,
						7, 19));
			}
		};
		MenuAction weaponInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				for (int i = 0; i < weaponList.length; i++) {
					if (infoMenu != null) {
						infoMenu.removeMenu();
					}
					if (weapons[i] != null) {
						if (weapons[i].infoMenuDrawn()) {
							weapons[i].clearInfoMenu();
						}
					}
					if (items[i] != null) {
						if (items[i].infoMenuDrawn()) {
							items[i].clearInfoMenu();
						}
					}
					if (caller.equals(weaponList[i])) {
						weapons[i].drawInfoMenu(20, 27 + (10 * i), statusMenu,
								false);
					}
				}
			}
		};
		MenuAction itemInfo = new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				for (int i = 0; i < itemList.length; i++) {
					if (infoMenu != null) {
						infoMenu.removeMenu();
					}
					if (weapons[i] != null) {
						if (weapons[i].infoMenuDrawn()) {
							weapons[i].clearInfoMenu();
						}
					}
					if (items[i] != null) {
						if (items[i].infoMenuDrawn()) {
							items[i].clearInfoMenu();
						}
					}
					if (caller.equals(itemList[i])) {
						items[i].drawInfoMenu(20, 27 + (10 * i), statusMenu,
								false);
					}
				}
			}
		};

		statusMenu.addElement(new MenuElement(clearInfo, back,
				(new TextGraphic("BACK", Font.BASICFONT)).getSprite(), true, 7,
				128));
		statusMenu.addElement(new MenuElement(clearInfo, help ? exitHelpMode
				: helpMode, (new TextGraphic("HELP", Font.BASICFONT))
				.getSprite(), true, 32, 128));

		statusMenu.addElement(new MenuElement(nil, nil, mapIdle.getSprite(0),
				false, 70, 7));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(name,
				Font.BASICFONT)).getSprite(), false, 95, 15));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
				unitClass.getName(), Font.BASICFONT)).getSprite(), false, 150,
				15));
		statusMenu.addElement(new MenuElement(levelInfo, nil, (new TextGraphic(
				"LEVEL " + level, Font.BASICFONT)).getSprite(), help, 95, 21));
		statusMenu.addElement(new MenuElement(expInfo, nil, (new TextGraphic(
				"EXP: " + exp + "/" + EXP_CAP, Font.BASICFONT)).getSprite(),
				help, 150, 21));
		statusMenu.addElement(new MenuElement(factionInfo, nil,
				(new TextGraphic("FACTION " + factionNum, Font.BASICFONT))
						.getSprite(), help, 150, 27));
		statusMenu.addElement(new MenuElement(coordinateInfo, nil,
				(new TextGraphic("X:" + mapx, Font.BASICFONT)).getSprite(),
				help, 95, 27));
		statusMenu.addElement(new MenuElement(coordinateInfo, nil,
				(new TextGraphic("Y:" + mapy, Font.BASICFONT)).getSprite(),
				help, 120, 27));
		if (mapFaceSprite != null) {
			statusMenu.addElement(new MenuElement(nil, nil, mapFaceSprite,
					false, 7, 7));
		}

		statusMenu.addElement(new MenuElement(hpInfo, nil, (new TextGraphic(
				"HP:" + curHp + "/" + hp, Font.BASICFONT)).getSprite(), help,
				70, 7));
		statusMenu.addElement(new MenuElement(nil, nil, (new Sprite(92, 5, 1,
				12, SpriteSheet.HEALTHBAR)), false, 110, 7));
		statusMenu.addElement(new MenuElement(nil, nil, (new Sprite(
				(int) ((92 * curHp) / hp), 5, 1, 18, SpriteSheet.HEALTHBAR)),
				false, 110, 7));
		statusMenu.addElement(new MenuElement(nil, nil, (new Sprite(92, 5, 1,
				24, SpriteSheet.HEALTHBAR)), false, 110, 7));

		statusMenu.addElement(new MenuElement(strInfo, nil, (new TextGraphic(
				"STR:", Font.BASICFONT)).getSprite(), help, 7, 40));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(40, 5, 1, 1,
				SpriteSheet.STATSBAR), false, 30, 40));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(str, 5, 42,
				1, SpriteSheet.STATSBAR), false, 30, 40));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
				Integer.toString(str), Font.STATFONT)).getSprite(), false, 45,
				38));

		statusMenu.addElement(new MenuElement(sklInfo, nil, (new TextGraphic(
				"SKL:", Font.BASICFONT)).getSprite(), help, 7, 50));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(40, 5, 1, 1,
				SpriteSheet.STATSBAR), false, 30, 50));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(skl, 5, 42,
				1, SpriteSheet.STATSBAR), false, 30, 50));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
				Integer.toString(skl), Font.STATFONT)).getSprite(), false, 45,
				48));

		statusMenu.addElement(new MenuElement(spdInfo, nil, (new TextGraphic(
				"SPD:", Font.BASICFONT)).getSprite(), help, 7, 60));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(40, 5, 1, 1,
				SpriteSheet.STATSBAR), false, 30, 60));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(spd, 5, 42,
				1, SpriteSheet.STATSBAR), false, 30, 60));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
				Integer.toString(spd), Font.STATFONT)).getSprite(), false, 45,
				58));

		statusMenu.addElement(new MenuElement(defInfo, nil, (new TextGraphic(
				"DEF:", Font.BASICFONT)).getSprite(), help, 7, 70));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(40, 5, 1, 1,
				SpriteSheet.STATSBAR), false, 30, 70));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(def, 5, 42,
				1, SpriteSheet.STATSBAR), false, 30, 70));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
				Integer.toString(def), Font.STATFONT)).getSprite(), false, 45,
				68));

		statusMenu.addElement(new MenuElement(resInfo, nil, (new TextGraphic(
				"RES:", Font.BASICFONT)).getSprite(), help, 7, 80));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(40, 5, 1, 1,
				SpriteSheet.STATSBAR), false, 30, 80));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(res, 5, 42,
				1, SpriteSheet.STATSBAR), false, 30, 80));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
				Integer.toString(res), Font.STATFONT)).getSprite(), false, 45,
				78));

		statusMenu.addElement(new MenuElement(conInfo, nil, (new TextGraphic(
				"CON:", Font.BASICFONT)).getSprite(), help, 7, 90));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(40, 5, 1, 1,
				SpriteSheet.STATSBAR), false, 30, 90));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(con * 2, 5,
				42, 1, SpriteSheet.STATSBAR), false, 30, 90));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
				Integer.toString(con), Font.STATFONT)).getSprite(), false, 45,
				88));

		statusMenu.addElement(new MenuElement(movInfo, nil, (new TextGraphic(
				"MOV:", Font.BASICFONT)).getSprite(), help, 7, 100));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(40, 5, 1, 1,
				SpriteSheet.STATSBAR), false, 30, 100));
		statusMenu.addElement(new MenuElement(nil, nil, new Sprite(mov * 4, 5,
				42, 1, SpriteSheet.STATSBAR), false, 30, 100));
		statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
				Integer.toString(mov), Font.STATFONT)).getSprite(), false, 45,
				98));

		for (int i = 0; i < weapons.length; i++) {
			try {
				statusMenu.addElement(new MenuElement(nil, nil, weapons[i]
						.getIcon(), false, 75, 40 + (16 * i)));
				weaponList[i] = (new MenuElement(weaponInfo, nil,
						(new TextGraphic(weapons[i].getName(), Font.BASICFONT))
								.getSprite(), help, 91, 40 + (16 * i)));
				statusMenu.addElement(weaponList[i]);
				statusMenu.addElement(new MenuElement(nil, nil,
						(new TextGraphic("" + weapons[i].getUses(),
								Font.BASICFONT)).getSprite(), false, 91,
						46 + (16 * i)));
			} catch (NullPointerException e) { // Do nothing
			}
			try {
				statusMenu.addElement(new MenuElement(nil, nil, items[i]
						.getIcon(), false, 146, 40 + (16 * i)));
				itemList[i] = (new MenuElement(itemInfo, nil, (new TextGraphic(
						items[i].getName(), Font.BASICFONT)).getSprite(), help,
						162, 40 + (16 * i)));
				statusMenu.addElement(itemList[i]);
				statusMenu.addElement(new MenuElement(nil, nil,
						(new TextGraphic("" + items[i].getUses(),
								Font.BASICFONT)).getSprite(), false, 162,
						46 + (16 * i)));
			} catch (NullPointerException e) { // Do nothing
			}
		}
		try {
			statusMenu.addElement(new MenuElement(nil, nil, armor.getIcon(),
					false, 7, 110));
			statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
					armor.getName(), Font.BASICFONT)).getSprite(), false, 23,
					110));
			statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
					"DEF:" + armor.getResilience(), Font.BASICFONT))
					.getSprite(), false, 23, 116));
			statusMenu.addElement(new MenuElement(nil, nil, (new TextGraphic(
					"ENC:" + armor.getEncumberance(), Font.BASICFONT))
					.getSprite(), false, 23, 122));
		} catch (NullPointerException e) { // Do nothing
		}

		if (!help) {
			statusMenu.setEscapeAction(back);
		} else {
			statusMenu.setEscapeAction(exitHelpMode);
		}

		MenuCursor.getMenuCursor().setElementIndex(help ? 1 : 0);
		MenuCursor.setActiveMenu(statusMenu);
	}

	public boolean isShowingLevelMenu() {
		return showingLevelMenu;
	}

	public void equipWeapon(int longest) {
		Weapon temp = getWeapon(0);
		setWeapon(0, getWeapon(longest));
		setWeapon(longest, temp);
	}

	public int getLongestRange(int longest, int i) {
		if (getWeapon(i) != null) {
			if (getWeapon(i).getRange() > getWeapon(longest).getRange()) {
				longest = i;
			}
		}
		return longest;
	}

	public int findLongestRange() {
		int range = 0;
		for (int i = 0; i < getWeapons().length; i++) {
			if (getWeapon(i) != null) {
				if (getWeapon(i).getRange() > range) {
					range = getWeapon(i).getRange();
				}
			}
		}
		return range;
	}

	public boolean canAttack(Unit defender, AiPlayer aIPlayer) {
		PathFinder pf = new PathFinder();
		pf.setUnitCollision(false);
		System.out.println("Getting path...");
		ArrayList<Tile> path = pf.getShortestPathAStar(
				Game.getCurrentMap(),
				this,
				Game.getCurrentMap()
						.getTile(
								getMapx() + getMapy()
										* Game.getCurrentMap().getWidth()),
				Game.getCurrentMap().getTile(
						defender.getMapx() + defender.getMapy()
								* Game.getCurrentMap().getWidth()));
		System.out.println("Path got.");
		int range = findLongestRange();
		removeTiles(path, range);
		int pathCost = aIPlayer.attackerCanTraverse(this, path);
		return getMov() <= pathCost;
	}

	private void removeTiles(ArrayList<Tile> path, int range) {
		for (int i = 0; i < range; i++) {
			if (path.size() > 0) {
				path.remove(0);
			}
		}
	}

	public void addElementsToAttackerMenu(int gain, MenuAction sa,
			MenuAction close, int newExp, int levelGain, Menu attackerExpMenu) {
		attackerExpMenu.addElement(new MenuElement(sa, close, new TextGraphic(
				getName(), Font.BASICFONT).getSprite(), true, 7, 7));
		attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"OLD EXP: " + getExp(), Font.BASICFONT).getSprite(), false, 7,
				13));
		attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"        +" + gain, Font.BASICFONT).getSprite(), false, 7, 19));
		attackerExpMenu
				.addElement(new MenuElement(sa, sa, new TextGraphic("NEW EXP: "
						+ newExp, Font.BASICFONT).getSprite(), false, 7, 25));
		attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"LVL: " + getLevel() + " -> " + (getLevel() + levelGain),
				Font.BASICFONT).getSprite(), false, 7, 31));
		attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"TO NEXT: " + (Unit.EXP_CAP - newExp), Font.BASICFONT)
				.getSprite(), false, 7, 37));
	}

	public float calculateCriticalChance(Unit defender) {
		return getWeapon(0).getCritical() + (getSkl() / 2) - defender.getRes();
	}

	public int calculateExpGain(Unit defender, int damageDealt, boolean defeated) {
		double expCoeff = defender.getLevel() == getLevel() ? 1
				: (Math.min(
						4,
						Math.max(
								0,
								defender.getLevel() > getLevel() ? 1 + (((double) ((double) defender
										.getLevel() / (double) getLevel())) / 10)
										: 1 - (((double) ((double) getLevel() / (double) defender
												.getLevel())) / 10))));
		System.out
				.println(((double) ((double) defender.getLevel() / (double) getLevel())));
		int base = defeated ? 30 : (damageDealt / 2);
		return (int) Math.round(base * expCoeff);
	}
}
