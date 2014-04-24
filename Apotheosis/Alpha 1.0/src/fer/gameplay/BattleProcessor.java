package fer.gameplay;

import fer.Cursor;
import fer.Game;
import fer.Map;
import fer.Tile;
import fer.Unit;
import fer.graphics.Animation;
import fer.graphics.Effect;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import java.util.Random;

/**
 * @author Evan
 */
public class BattleProcessor {

    private Unit attacker;
    private Unit defender;
    private Menu attackerMenu;
    private Menu attackerWeaponMenu;
    private Menu attackerStatusMenu;
    private Menu attackerBattleMenu;
    private Menu attackerExpMenu;
    private Menu defenderMenu;
    private Menu defenderWeaponMenu;
    private Menu defenderStatusMenu;
    private Menu defenderBattleMenu;
    private Menu defenderExpMenu;
    private boolean attackerExpOpen = false;
    private boolean defenderExpOpen = false;
    private Random random = new Random();
    private Cursor cursor = Cursor.getCursor();
    private Effect damageEffect;
    private Effect criticalEffect;
    private boolean inCombat;
    private int stage = 0;
    private int currentAttack = 0;
    private int updates = 0;
    private boolean[] missAttacker;
    private boolean[] missDefender;
    private boolean[] criticalAttacker;
    private boolean[] criticalDefender;
    private boolean melee;
    private int numAttacksAttacker = 0;
    private int numAttacksDefender = 0;
    private int attackerDamage = 0;
    private int defenderDamage = 0;
    private int attackerDamageDealt = 0;
    private int defenderDamageDealt = 0;
    private int attackerLevelsGained = 0;
    private int defenderLevelsGained = 0;
    private int levelIterations = 0;
    private float accAttacker = 0;
    private float accDefender = 0;
    private float critAttacker = 0;
    private float critDefender = 0;
    //Multipurpose boolean variables to mark when animations are completed.
    private boolean attackerComplete;
    private boolean defenderComplete;
    //Integer corresponding to the directional relationship of the defender to
    //the attacker.  0 = Above, 1 = Below, 2 = Left, 3 = Right
    private int directionAttacker;
    //Integer corresponding to the directional relationship of the attacker to
    //the defender.
    private int directionDefender;

    public void startBattle(Unit iAttacker, Unit iDefender) {
        cursor.setActive(false);
        inCombat = true;
        attacker = iAttacker;
        defender = iDefender;
        attacker.resetAnimation(22);
        defender.resetAnimation(22);
        attackerDamageDealt = 0;
        defenderDamageDealt = 0;
        updates = 0;
        stage = 0;
        currentAttack = 0;

        //Determine direction of attack
        //If x difference is greater than y difference, face left or right accordingly.
        if (Math.abs(attacker.getMapx() - defender.getMapx()) > Math.
                abs(attacker.getMapy() - defender.getMapy())) {
            if (defender.getMapx() < attacker.getMapx()) {
                directionAttacker = 2;
                directionDefender = 3;
            } else {
                directionAttacker = 3;
                directionDefender = 2;
            }
        } else {  //Otherwise, face up or down.
            if (defender.getMapy() < attacker.getMapy()) {
                directionAttacker = 0;
                directionDefender = 1;
            } else {
                directionAttacker = 1;
                directionDefender = 0; //If, by some odd glitch, the attacker and defender
                //occupy the same tile, the attacker will face down, and the defender up.
            }
        }

        //Calculate the number of attacks (Based on FE:PR formula at the moment)
        if (calculateAttackSpeed(attacker) >= calculateAttackSpeed(defender) + 3) {
            numAttacksAttacker = 2;
        } else {
            numAttacksAttacker = 1;
        }
        if (numAttacksAttacker > attacker.getWeapon(0).getUses()) {
            numAttacksAttacker = attacker.getWeapon(0).getUses();
        }
        if (defender.getWeapon(0).getRange() >= (Math.abs(defender.getMapx()
                - attacker.getMapx()) + Math.abs(defender.getMapy() - attacker.
                getMapy()))) {
            if (calculateAttackSpeed(defender) >= calculateAttackSpeed(attacker) + 3) {
                numAttacksDefender = 2;
            } else {
                numAttacksDefender = 1;
            }
            if (numAttacksDefender > defender.getWeapon(0).getUses()) {
                numAttacksDefender = defender.getWeapon(0).getUses();
            }
        } else {
            numAttacksDefender = 0;
        }

        //Determine if attacks hit
        missAttacker = new boolean[numAttacksAttacker];
        missDefender = new boolean[numAttacksDefender];
        accAttacker = calculateAttackHitChance(attacker, defender);
        for (int i = 0; i < missAttacker.length; i++) {
            float num = random.nextFloat() * 100;
            if (num <= accAttacker) {  //Hit
                missAttacker[i] = false;
            } else {
                missAttacker[i] = true;
            }
        }
        accDefender = calculateAttackHitChance(defender, attacker);
        for (int i = 0; i < missDefender.length; i++) {
            float num = random.nextFloat() * 100;
            if (num <= accDefender) {  //Hit
                missDefender[i] = false;
            } else {
                missDefender[i] = true;
            }
        }

        //Calculate if attack is critical
        criticalAttacker = new boolean[numAttacksAttacker];
        criticalDefender = new boolean[numAttacksDefender];
        critAttacker = calculateCriticalChance(attacker, defender);
        for (int i = 0; i < criticalAttacker.length; i++) {
            float num = random.nextFloat() * 100;
            if (num <= critAttacker && !missAttacker[i]) {
                criticalAttacker[i] = true;
            } else {
                criticalAttacker[i] = false;
            }
        }
        critDefender = calculateCriticalChance(defender, attacker);
        for (int i = 0; i < criticalDefender.length; i++) {
            float num = random.nextFloat() * 100;
            if (num <= critDefender && !missDefender[i]) {
                criticalDefender[i] = true;
            } else {
                criticalDefender[i] = false;
            }
        }

        //Calculate attack damage
        attackerDamage = calculateAttackDamage(attacker, defender);
        defenderDamage = calculateAttackDamage(defender, attacker);

        drawAttackerMenus();
        drawDefenderMenus();
    }

    public void update() {
        switch (stage) {
            case 0:
                attackerComplete = false;
                defenderComplete = false;
                attacker.setActiveMapAnimation(6 + directionAttacker);
                updates = 0;
                stage++;
                break;
            case 1:
                if (updates >= attacker.getActiveAnimationUpdates(attacker.getActiveAnimationKeyFrame() - 1)) {
                    if (missAttacker[currentAttack]) {
                        defender.setActiveMapAnimation(14 + Math.abs(directionAttacker - 3));
                    } else {
                        defender.setActiveMapAnimation(18 + directionDefender);
                        damageEffect = new Effect(new Animation(1, new Sprite[]{defender.getMapSprite()}), (new int[]{128}), (new int[]{0xff0000}), (defender.getMapx() - cursor.getMapScrollx())
                                * Tile.TILE_WIDTH - 4, (defender.getMapy()
                                - cursor.getMapScrolly()) * Tile.TILE_HEIGHT - 8);
                        if (criticalAttacker[currentAttack]) {
                            criticalEffect = new Effect(new Animation(1, new Sprite[]{new TextGraphic("CRITICAL!", Font.BASICFONT).getSprite()}, new int[]{5}), new int[]{192}, (defender.getMapx()
                                    - cursor.getMapScrollx()) * Tile.TILE_WIDTH - 4,
                                    (defender.getMapy() - cursor.getMapScrolly())
                                    * Tile.TILE_HEIGHT - 8);
                        }
                    }
                    updates = 0;
                    stage++;
                }
                break;
            case 2:
                if (updates == attacker.getActiveAnimationUpdates(attacker.getActiveAnimationFrames()) - attacker.getActiveAnimationUpdates(attacker.getActiveAnimationKeyFrame() - 1)) {
                    attackerComplete = true;
                    attacker.setActiveMapAnimation(0);
                }
                if (updates == defender.getActiveAnimationUpdates(defender.getActiveAnimationFrames())) {
                    defenderComplete = true;
                    defender.setActiveMapAnimation(0);
                    Game.getEffectList().remove(damageEffect);
                    Game.getEffectList().remove(criticalEffect);
                }
                if (!defenderComplete) {
                    if (updates % 2 == 0 && missAttacker[currentAttack]) {
                        switch (directionAttacker) {
                            case 0:
                                defender.setxOffset(defender.getxOffset() + 1);
                                break;
                            case 1:
                                defender.setxOffset(defender.getxOffset() - 1);
                                break;
                            case 2:
                                defender.setyOffset(defender.getyOffset() + 1);
                                break;
                            case 3:
                                defender.setyOffset(defender.getyOffset() - 1);
                                break;
                        }
                    } else if (!missAttacker[currentAttack]) {
                        //Refresh damageEffect sprite to match unit animation
                        Game.getEffectList().remove(damageEffect);
                        damageEffect = new Effect(new Animation(1, new Sprite[]{defender.getMapSprite()}), (new int[]{128}), (new int[]{0xff0000}), (defender.getMapx() - cursor.getMapScrollx())
                                * Tile.TILE_WIDTH - 4, (defender.getMapy()
                                - cursor.getMapScrolly()) * Tile.TILE_HEIGHT - 8);
                    }
                }
                if (attackerComplete && defenderComplete) {
                    defender.setxOffset(0);
                    defender.setyOffset(0);
                    updates = 0;
                    stage++;
                } /*else if (attackerComplete && !missAttacker[currentAttack]) {
                 updates = 0;
                 stage++;
                 }*/
                break;
            case 3:
                if (!missAttacker[currentAttack]) {
                    if (criticalAttacker[currentAttack]) {
                        defender.setCurrentHp(Math.max(defender.getCurrentHp() - (attackerDamage * 3), 0));
                        attackerDamageDealt += attackerDamage * 3;
                    } else {
                        defender.setCurrentHp(Math.max(defender.getCurrentHp() - attackerDamage, 0));
                        attackerDamageDealt += attackerDamage;
                    }
                    /*if (directionAttacker == 1) {
                     defender.setActiveMapAnimation(18);
                     } else if (directionAttacker == 0) {
                     defender.setActiveMapAnimation(19);
                     } else if (directionAttacker == 3) {
                     defender.setActiveMapAnimation(20);
                     } else {
                     defender.setActiveMapAnimation(21);
                     }*/
                    drawDefenderMenus();  //Update health display    
                    if (defender.getCurrentHp() == 0) { //If the defender is dead
                        stage = 10;
                        break;
                    }
                }
                updates = 0;
                stage++;
                break;
            case 4:
                if (currentAttack < numAttacksDefender) {
                    stage++;
                } else {
                    stage = 9;
                }
                break;
            case 5:
                attackerComplete = false;
                defenderComplete = false;
                defender.setActiveMapAnimation(6 + directionDefender);
                updates = 0;
                stage++;
                break;
            case 6:
                if (updates >= defender.getActiveAnimationUpdates(defender.getActiveAnimationKeyFrame() - 1)) {
                    if (missDefender[currentAttack]) {
                        attacker.setActiveMapAnimation(14 + Math.abs(directionDefender - 3));
                    } else {
                        attacker.setActiveMapAnimation(18 + directionAttacker);
                        damageEffect = new Effect(new Animation(1, new Sprite[]{attacker.getMapSprite()}), (new int[]{128}), (new int[]{0xff0000}), (attacker.getMapx() - cursor.getMapScrollx())
                                * Tile.TILE_WIDTH - 4, (attacker.getMapy()
                                - cursor.getMapScrolly()) * Tile.TILE_HEIGHT - 8);
                        if (criticalDefender[currentAttack]) {
                            criticalEffect = new Effect(new Animation(1, new Sprite[]{new TextGraphic("CRITICAL!", Font.BASICFONT).getSprite()}, new int[]{5}), new int[]{192}, (attacker.getMapx()
                                    - cursor.getMapScrollx()) * Tile.TILE_WIDTH - 4,
                                    (attacker.getMapy() - cursor.getMapScrolly())
                                    * Tile.TILE_HEIGHT - 8);
                        }
                    }
                    updates = 0;
                    stage++;
                }
                break;
            case 7:
                if (updates == defender.getActiveAnimationUpdates(defender.getActiveAnimationFrames()) - defender.getActiveAnimationUpdates(defender.getActiveAnimationKeyFrame() - 1)) {
                    attackerComplete = true;
                    defender.setActiveMapAnimation(0);
                }
                if (updates == attacker.getActiveAnimationUpdates(attacker.getActiveAnimationFrames())) {
                    defenderComplete = true;
                    attacker.setActiveMapAnimation(0);
                    Game.getEffectList().remove(damageEffect);
                    Game.getEffectList().remove(criticalEffect);
                }
                if (!defenderComplete) {
                    if (updates % 2 == 0 && missDefender[currentAttack]) {
                        switch (directionDefender) {
                            case 0:
                                attacker.setxOffset(attacker.getxOffset() + 1);
                                break;
                            case 1:
                                attacker.setxOffset(attacker.getxOffset() - 1);
                                break;
                            case 2:
                                attacker.setyOffset(attacker.getyOffset() - 1);
                                break;
                            case 3:
                                attacker.setyOffset(attacker.getyOffset() + 1);
                                break;
                        }
                    } else if (!missDefender[currentAttack]) {
                        //Refresh damageEffect sprite to match unit animation
                        Game.getEffectList().remove(damageEffect);
                        damageEffect = new Effect(new Animation(1, new Sprite[]{attacker.getMapSprite()}), (new int[]{128}), (new int[]{0xff0000}), (attacker.getMapx() - cursor.getMapScrollx())
                                * Tile.TILE_WIDTH - 4, (attacker.getMapy()
                                - cursor.getMapScrolly()) * Tile.TILE_HEIGHT - 8);
                    }
                }
                if (attackerComplete && defenderComplete) {
                    attacker.setxOffset(0);
                    attacker.setyOffset(0);
                    updates = 0;
                    stage++;
                }
                break;
            case 8:
                if (!missDefender[currentAttack]) {
                    if (criticalDefender[currentAttack]) {
                        attacker.setCurrentHp(Math.max(attacker.getCurrentHp() - (defenderDamage * 3), 0));
                        defenderDamageDealt += defenderDamage * 3;
                    } else {
                        attacker.setCurrentHp(Math.max(attacker.getCurrentHp() - defenderDamage, 0));
                        defenderDamageDealt += defenderDamage;
                    }
                    /*if (directionDefender == 1) {
                     attacker.setActiveMapAnimation(18);
                     } else if (directionDefender == 0) {
                     attacker.setActiveMapAnimation(19);
                     } else if (directionDefender == 3) {
                     attacker.setActiveMapAnimation(20);
                     } else {
                     attacker.setActiveMapAnimation(21);
                     }*/
                    drawAttackerMenus();  //Update health display
                    if (attacker.getCurrentHp() == 0) { //If the attacker is dead
                        stage = 10;
                        break;
                    }
                }
                updates = 0;
                stage++;
                break;
            case 9:
                currentAttack++;
                attacker.resetAnimation(22);
                defender.resetAnimation(22);
                if (currentAttack < numAttacksAttacker) {
                    stage = 0; //Repeat if attacker attacks again
                } else if (currentAttack < numAttacksDefender) {
                    stage = 5; //If attacker does not attack but defender does,
                    //repeat from 6 instead of 0.
                } else {
                    stage++; //Neither attacks again; the battle is over.
                }
                break;
            case 10:
                if (attacker.getCurrentHp() == 0) {
                    attacker.setDead(true);
                }
                if (defender.getCurrentHp() == 0) {
                    defender.setDead(true);
                }
                MenuCursor.getMenuCursor().setActive(true);
                if (!attacker.isDead()) {
                    drawAttackerExpMenu(calculateExpGain(attacker, defender, attackerDamageDealt, defender.isDead()));
                }
                if (!defender.isDead()) {
                    drawDefenderExpMenu(calculateExpGain(defender, attacker, defenderDamageDealt, attacker.isDead()));
                }
                stage++;
                break;
            case 11:
                if (!attackerExpOpen && !defenderExpOpen) {
                    stage++;
                }
                break;
            case 12:
                attackerLevelsGained = (int) ((attacker.getExp() + calculateExpGain(attacker, defender, attackerDamageDealt, defender.isDead())) / Unit.EXP_CAP);
                defenderLevelsGained = (int) ((defender.getExp() + calculateExpGain(defender, attacker, defenderDamageDealt, attacker.isDead())) / Unit.EXP_CAP);
                attacker.setExp((attacker.getExp() + calculateExpGain(attacker, defender, attackerDamageDealt, defender.isDead())) % Unit.EXP_CAP);
                defender.setExp((defender.getExp() + calculateExpGain(defender, attacker, defenderDamageDealt, attacker.isDead())) % Unit.EXP_CAP);
                levelIterations = 0;
                stage++;
                break;
            case 13:
                if (levelIterations < attackerLevelsGained) {
                    attacker.levelUnit(true);
                    stage++;
                } else {
                    levelIterations = 0;
                    stage = 15;
                }
                break;
            case 14:
                if (!attacker.isShowingLevelMenu()) {
                    levelIterations++;
                    stage--;
                }
                break;
            case 15:
                if (levelIterations < defenderLevelsGained) {
                    defender.levelUnit(true);
                    stage++;
                } else {
                    levelIterations = 0;
                    stage = 17;
                }
                break;
            case 16:
                if (!defender.isShowingLevelMenu()) {
                    levelIterations++;
                    stage--;
                }
                break;
            case 17:
                endBattle();
                break;
        }
        updates++;
    }

    public void endBattle() {
        Game.getMenuList().remove(attackerMenu);
        Game.getMenuList().remove(attackerStatusMenu);
        Game.getMenuList().remove(attackerWeaponMenu);
        Game.getMenuList().remove(attackerBattleMenu);
        Game.getMenuList().remove(defenderMenu);
        Game.getMenuList().remove(defenderStatusMenu);
        Game.getMenuList().remove(defenderWeaponMenu);
        Game.getMenuList().remove(defenderBattleMenu);
        attacker.resetAnimation(22);
        defender.resetAnimation(22);
        attacker.setMoved(true);
        attacker.getWeapon(0).setUses(attacker.getWeapon(0).getUses() - numAttacksAttacker);
        if (attacker.getWeapon(0).getUses() <= 0) {
            attacker.setWeapon(0, null);
            for (int i = 1; i < attacker.getWeapons().length; i++) {
                if (attacker.getWeapon(i) != null) {
                    attacker.setWeapon(0, attacker.getWeapon(i));
                    attacker.setWeapon(i, null);
                    break;
                }
            }
        }
        defender.getWeapon(0).setUses(defender.getWeapon(0).getUses() - numAttacksDefender);
        if (defender.getWeapon(0).getUses() <= 0) {
            defender.setWeapon(0, null);
            for (int i = 1; i < defender.getWeapons().length; i++) {
                if (defender.getWeapon(i) != null) {
                    defender.setWeapon(0, defender.getWeapon(i));
                    defender.setWeapon(i, null);
                    break;
                }
            }
        }
        inCombat = false;
        cursor.setActive(true);
        cursor.processVictory();
    }

    public void drawAttackerMenus() {
        if (attackerMenu != null) {  //Clear the menus if they already exist
            Game.getMenuList().remove(attackerMenu);
            Game.getMenuList().remove(attackerWeaponMenu);
            Game.getMenuList().remove(attackerStatusMenu);
            Game.getMenuList().remove(attackerBattleMenu);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };

        if (cursor.getMapX() - cursor.getMapScrollx() > Map.MIN_MAP_WIDTH / 2) {
            attackerMenu = new Menu(46, 52, 0, 0);
            attackerWeaponMenu = new Menu(30, 76, 0, 52);
            attackerStatusMenu = new Menu(131, 16, 46, 0);
            attackerBattleMenu = new Menu(131, 16, 46, 16);
        } else {
            attackerMenu = new Menu(46, 52, 194, 0);
            attackerWeaponMenu = new Menu(30, 76, 210, 52);
            attackerStatusMenu = new Menu(131, 16, 63, 0);
            attackerBattleMenu = new Menu(131, 16, 63, 16);
        }

        attackerMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(attacker.getName(), Font.BASICFONT)).getSprite(), false, 7, 7));
        if (attacker.getMapFaceSprite() != null) {
            attackerMenu.addElement(new MenuElement(sa, sa, attacker.getMapFaceSprite(), false, 7, 13));
        }
        if (attacker.getWeapon(0) != null) {
            attackerWeaponMenu.addElement(new MenuElement(sa, sa, attacker.getWeapon(0).getIcon(), false, 7, 7));
            attackerWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("D:" + attacker.getWeapon(0).getDamage(), Font.BASICFONT)).getSprite(), false, 5, 24));
            attackerWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("P:" + attacker.getWeapon(0).getPierce(), Font.BASICFONT)).getSprite(), false, 5, 30));
            attackerWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("A:" + attacker.getWeapon(0).getAccuracy(), Font.BASICFONT)).getSprite(), false, 5, 36));
        }
        if (attacker.getArmor() != null) {
            attackerWeaponMenu.addElement(new MenuElement(sa, sa, attacker.getArmor().getIcon(), false, 7, 42));
            attackerWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("R:" + attacker.getArmor().getResilience(), Font.BASICFONT)).getSprite(), false, 5, 58));
            attackerWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("E:" + attacker.getArmor().getEncumberance(), Font.BASICFONT)).getSprite(), false, 5, 64));
        }
        attackerStatusMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("HP:" + attacker.getCurrentHp(), Font.BASICFONT)).getSprite(), false, 7, 5));
        attackerStatusMenu.addElement(new MenuElement(sa, sa, (new Sprite(92, 5, 1, 12, SpriteSheet.HEALTHBAR)), false, 32, 5));
        attackerStatusMenu.addElement(new MenuElement(sa, sa, (new Sprite((int) ((92 * attacker.getCurrentHp()) / attacker.getHp()), 5, 1, 18, SpriteSheet.HEALTHBAR)), false, 32, 5));
        attackerStatusMenu.addElement(new MenuElement(sa, sa, (new Sprite(92, 5, 1, 24, SpriteSheet.HEALTHBAR)), false, 32, 5));

        attackerBattleMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("MT: " + attackerDamage, Font.BASICFONT)).getSprite(), false, 7, 5));
        attackerBattleMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("AC: " + Math.round(accAttacker), Font.BASICFONT)).getSprite(), false, 47, 5));
        attackerBattleMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("CR: " + Math.round(critAttacker), Font.BASICFONT)).getSprite(), false, 87, 5));

    }

    public void drawDefenderMenus() {
        if (defenderMenu != null) {  //Clear the menus if they already exist
            Game.getMenuList().remove(defenderMenu);
            Game.getMenuList().remove(defenderWeaponMenu);
            Game.getMenuList().remove(defenderStatusMenu);
            Game.getMenuList().remove(defenderBattleMenu);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };

        if (cursor.getMapX() - cursor.getMapScrollx() > Map.MIN_MAP_WIDTH / 2) {
            defenderMenu = new Menu(46, 52, 194, 108);
            defenderWeaponMenu = new Menu(30, 76, 210, 32);
            defenderStatusMenu = new Menu(131, 16, 63, 144);
            defenderBattleMenu = new Menu(131, 16, 63, 128);
        } else {
            defenderMenu = new Menu(46, 52, 0, 108);
            defenderWeaponMenu = new Menu(30, 76, 0, 32);
            defenderStatusMenu = new Menu(131, 16, 46, 144);
            defenderBattleMenu = new Menu(131, 16, 46, 128);
        }

        defenderMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(defender.getName(), Font.BASICFONT)).getSprite(), false, 7, 7));
        if (defender.getMapFaceSprite() != null) {
            defenderMenu.addElement(new MenuElement(sa, sa, defender.getMapFaceSprite(), false, 7, 13));
        }
        if (defender.getWeapon(0) != null) {
            defenderWeaponMenu.addElement(new MenuElement(sa, sa, defender.getWeapon(0).getIcon(), false, 7, 7));
            defenderWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("D:" + defender.getWeapon(0).getDamage(), Font.BASICFONT)).getSprite(), false, 5, 24));
            defenderWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("P:" + defender.getWeapon(0).getPierce(), Font.BASICFONT)).getSprite(), false, 5, 30));
            defenderWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("A:" + defender.getWeapon(0).getAccuracy(), Font.BASICFONT)).getSprite(), false, 5, 36));
        }
        if (defender.getArmor() != null) {
            defenderWeaponMenu.addElement(new MenuElement(sa, sa, defender.getArmor().getIcon(), false, 7, 42));
            defenderWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("R:" + defender.getArmor().getResilience(), Font.BASICFONT)).getSprite(), false, 5, 58));
            defenderWeaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("E:" + defender.getArmor().getEncumberance(), Font.BASICFONT)).getSprite(), false, 5, 64));
        }
        defenderStatusMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("HP:" + defender.getCurrentHp(), Font.BASICFONT)).getSprite(), false, 7, 5));
        defenderStatusMenu.addElement(new MenuElement(sa, sa, (new Sprite(92, 5, 1, 12, SpriteSheet.HEALTHBAR)), false, 32, 5));
        defenderStatusMenu.addElement(new MenuElement(sa, sa, (new Sprite((int) ((92 * defender.getCurrentHp()) / defender.getHp()), 5, 1, 18, SpriteSheet.HEALTHBAR)), false, 32, 5));
        defenderStatusMenu.addElement(new MenuElement(sa, sa, (new Sprite(92, 5, 1, 24, SpriteSheet.HEALTHBAR)), false, 32, 5));

        defenderBattleMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("MT: " + defenderDamage, Font.BASICFONT)).getSprite(), false, 7, 5));
        defenderBattleMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("AC: " + Math.round(accDefender), Font.BASICFONT)).getSprite(), false, 47, 5));
        defenderBattleMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("CR: " + Math.round(critDefender), Font.BASICFONT)).getSprite(), false, 87, 5));
    }

    public void drawAttackerExpMenu(int gain) {
        attackerExpOpen = true;
        if (attackerExpMenu != null) {
            attackerExpMenu.removeMenu();
        }
        attackerExpMenu = new Menu(114, 50, 63, 30);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };

        MenuAction close = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                attackerExpMenu.removeMenu();
                attackerExpOpen = false;
                if (defenderExpMenu != null) {
                    defenderExpMenu.removeMenu();
                }
                defenderExpOpen = false;
            }
        };

        attackerExpMenu.setEscapeAction(close);

        int newExp = (attacker.getExp() + gain) % Unit.EXP_CAP;
        int levelGain = (int) (attacker.getExp() + gain) / Unit.EXP_CAP;

        attackerExpMenu.addElement(new MenuElement(sa, close, new TextGraphic(attacker.getName(), Font.BASICFONT).getSprite(), true, 7, 7));
        attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("OLD EXP: " + attacker.getExp(), Font.BASICFONT).getSprite(), false, 7, 13));
        attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("        +" + gain, Font.BASICFONT).getSprite(), false, 7, 19));
        attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("NEW EXP: " + newExp, Font.BASICFONT).getSprite(), false, 7, 25));
        attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("LVL: " + attacker.getLevel() + " -> " + (attacker.getLevel() + levelGain), Font.BASICFONT).getSprite(), false, 7, 31));
        attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("TO NEXT: " + (Unit.EXP_CAP - newExp), Font.BASICFONT).getSprite(), false, 7, 37));

        MenuCursor.getMenuCursor().setElementIndex(0);
        MenuCursor.setActiveMenu(attackerExpMenu);
    }

    public void drawDefenderExpMenu(int gain) {
        defenderExpOpen = true;
        if (defenderExpMenu != null) {
            defenderExpMenu.removeMenu();
        }
        defenderExpMenu = new Menu(114, 50, 63, 80);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };

        MenuAction close = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                defenderExpMenu.removeMenu();
                defenderExpOpen = false;
                if (attackerExpMenu != null) {
                    attackerExpMenu.removeMenu();
                }
                attackerExpOpen = false;
            }
        };

        defenderExpMenu.setEscapeAction(close);

        int newExp = (defender.getExp() + gain) % Unit.EXP_CAP;
        int levelGain = (int) (defender.getExp() + gain) / Unit.EXP_CAP;

        defenderExpMenu.addElement(new MenuElement(sa, close, new TextGraphic(defender.getName(), Font.BASICFONT).getSprite(), true, 7, 7));
        defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("OLD EXP: " + defender.getExp(), Font.BASICFONT).getSprite(), false, 7, 13));
        defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("        +" + gain, Font.BASICFONT).getSprite(), false, 7, 19));
        defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("NEW EXP: " + newExp, Font.BASICFONT).getSprite(), false, 7, 25));
        defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("LVL: " + defender.getLevel() + " -> " + (defender.getLevel() + levelGain), Font.BASICFONT).getSprite(), false, 7, 31));
        defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("TO NEXT: " + (Unit.EXP_CAP - newExp), Font.BASICFONT).getSprite(), false, 7, 37));

        MenuCursor.getMenuCursor().setElementIndex(0);
        MenuCursor.setActiveMenu(defenderExpMenu);
    }

    public int calculateAttackDamage(Unit attacker, Unit defender) {
        int wepatk;
        wepatk = attacker.getWeapon(0).getDamage() + attacker.getStr();
        /*if (attacker.getWeapon(0).isMelee()) {  //Melee weapon damage is calaculated differently than firearm
         wepatk = attacker.getWeapon(0).getDamage() * (1 + (attacker.getStr() / 10));
         } else {
         wepatk = attacker.getWeapon(0).getDamage() * (1 + (attacker.getSkl() / 100));
         }*/
        int wepdam;
        if (defender.getArmor() != null) {
            wepdam = wepatk - (defender.getDef() + Game.getCurrentMap().getTile(defender.getMapx() + defender.getMapy()).getDef()
                    + (defender.getArmor().getResilience() - attacker.getWeapon(0)
                    .getPierce()));
        } else {
            wepdam = wepatk - (defender.getDef() + Game.getCurrentMap().
                    getTile(defender.getMapx() + defender.getMapy()).getDef());
        }
        return Math.max(0, wepdam);
    }

    public float calculateAttackHitChance(Unit attacker, Unit defender) {
        float hitRate = ((attacker.getWeapon(0).getAccuracy()) + (attacker.getSkl() * 2));
        float evade = (calculateAttackSpeed(defender) * 2) + Game.
                getCurrentMap().getTile(defender.getMapx() + defender.getMapy()
                * Game.getCurrentMap().getWidth()).getAvo();
        return Math.min(Math.max(hitRate - evade, 0), 100);
        /*float wepacc = (attacker.getWeapon(0).getAccuracy() + attacker.getSkl()) / 2;
         return wepacc * (1 - (defender.getSpd() / 100));*/
    }

    public float calculateCriticalChance(Unit attacker, Unit defender) {
        return attacker.getWeapon(0).getCritical() + (attacker.getSkl() / 2)
                - defender.getRes();
    }

    public int calculateAttackSpeed(Unit unit) {
        int speed = unit.getSpd();
        if (unit.getWeapon(0).getWeight() > unit.getCon()) {
            speed -= (unit.getWeapon(0).getWeight() - unit.getCon());
        }
        if (unit.getArmor().getEncumberance() > unit.getCon()) {
            speed -= (unit.getArmor().getEncumberance() - unit.getCon());
        }
        return speed;
    }

    public double calculateDeathChance(Unit unit, Unit opponent, boolean unitAttacking) {
        if ((2 * calculateAttackDamage(opponent, unit)) < unit.getCurrentHp()) {
            return 0;
        } else {
            int numAttacksUnit, numAttacksOpponent;
            if (unit.getWeapon(0).getRange() >= (Math.abs(unit.getMapx() - opponent.
                    getMapx()) + Math.abs(unit.getMapy() - opponent.getMapy()))) {
                if (calculateAttackSpeed(unit) >= calculateAttackSpeed(opponent) + 3) {
                    numAttacksUnit = 2;
                } else {
                    numAttacksUnit = 1;
                }
                if (numAttacksUnit > unit.getWeapon(0).getUses()) {
                    numAttacksUnit = unit.getWeapon(0).getUses();
                }
            } else {
                numAttacksUnit = 0;
            }
            if (opponent.getWeapon(0).getRange() >= (Math.abs(opponent.getMapx() - unit.
                    getMapx()) + Math.abs(opponent.getMapy() - unit.getMapy()))) {
                if (calculateAttackSpeed(opponent) >= calculateAttackSpeed(unit) + 3) {
                    numAttacksOpponent = 2;
                } else {
                    numAttacksOpponent = 1;
                }
                if (numAttacksOpponent > opponent.getWeapon(0).getUses()) {
                    numAttacksOpponent = opponent.getWeapon(0).getUses();
                }
            } else {
                numAttacksOpponent = 0;
            }
            double opponentSurvival = 1;
            if (calculateAttackDamage(opponent, unit) >= unit.getCurrentHp()) {
                //One hit will kill
                if (unitAttacking && (calculateAttackDamage(unit, opponent)
                        >= opponent.getCurrentHp()) && numAttacksUnit >= 1) {
                    //If the unit can strike the opponent first and kill,
                    //survival is the probability they miss
                    opponentSurvival = 1 - (calculateAttackHitChance(unit, opponent) / 100);
                }
                return (calculateAttackHitChance(opponent, unit) / 100)
                        * opponentSurvival;//Unit misses and opponent hits;
            } else {
                //Two hits will kill
                if (!unitAttacking && (calculateAttackDamage(unit, opponent)
                        >= opponent.getCurrentHp()) && numAttacksUnit >= 1) {
                    //If the unit can strike the opponent after its first attack and 
                    //kill, survival is the probability they miss
                    opponentSurvival = 1 - (calculateAttackHitChance(unit, opponent) / 100);
                } else if (unitAttacking && ((2 * calculateAttackDamage(unit,
                        opponent)) >= opponent.getCurrentHp()) && numAttacksUnit >= 2) {
                    //If the unit strikes first twice and will kill if they hit both
                    //times, survival is the probability they do not
                    opponentSurvival = 1 - Math.pow((calculateAttackHitChance(unit, opponent) / 100), 2);
                }
            }
            return Math.pow((calculateAttackHitChance(opponent, unit) / 100), 2)
                    * opponentSurvival;//Unit does not hit twice, opponent does
        }
    }

    public int calculateExpGain(Unit attacker, Unit defender, int damageDealt, boolean defeated) {
        double expCoeff = defender.getLevel() == attacker.getLevel() ? 1 : (Math
                .min(4, Math.max(0, defender.getLevel() > attacker.getLevel()
                ? 1 + (((double)((double)defender.getLevel() / (double)attacker.getLevel())) / 10) : 1 - (((double)((double)attacker.
                getLevel() / (double)defender.getLevel())) / 10))));
        System.out.println(((double)((double)defender.getLevel() / (double)attacker.getLevel())));
        int base = defeated ? 30 : (damageDealt / 2);
        return (int) Math.round(base * expCoeff);
    }

    public boolean isInCombat() {
        return inCombat;
    }
}
