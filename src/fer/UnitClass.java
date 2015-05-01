package fer;

import fer.graphics.Animation;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.util.UnitClassData;
import java.util.ArrayList;

/**
 * @author Evan Stewart
 * 
 * Stores the base stats and properties related to each class of unit.
 * 
 * TODO: Read data from XML as opposed to the use of static constants.
 */
public class UnitClass {
    
     public static int[] MOVETYPE_FOOT = new int[] {1, 2, 60};
    
     private int type;
     private String name;
     private boolean female;
     private int hp, str, skl, spd, def, res, mov, con;
     private int sword, axe, lance, bow, anima, light, dark, staff;
     private int[] movetype;
     private Animation defaultMapIdle;
     private Animation defaultMapMoveUp;
     private Animation defaultMapMoveLeft;
     private Animation defaultMapMoveRight;
     private Animation defaultMapMoveDown;
     private Animation defaultMapSelected;
     private Animation defaultMapAttackUp;
     private Animation defaultMapAttackLeft;
     private Animation defaultMapAttackRight;
     private Animation defaultMapAttackDown;
     private Animation defaultMapMeleeUp;
     private Animation defaultMapMeleeLeft;
     private Animation defaultMapMeleeRight;
     private Animation defaultMapMeleeDown;
     private Animation defaultMapEvadeUp;
     private Animation defaultMapEvadeLeft;
     private Animation defaultMapEvadeRight;
     private Animation defaultMapEvadeDown;
     private Animation defaultMapDamageUp;
     private Animation defaultMapDamageLeft;
     private Animation defaultMapDamageRight;
     private Animation defaultMapDamageDown;
     
     /*public UnitClass(int typeIndex) {
         type = typeIndex;
         initClass();
     }*/
     
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
         
         ArrayList<Sprite[]> anims = new ArrayList(22);
         SpriteSheet sheet = Game.getSpriteSheet(data.getSheetIndex());
                 
         for (int i = 0; i < 22; i++) {
             Sprite[] sprites = new Sprite[data.getNumFrames()[i]];
             for (int j = 0; j < sprites.length; j++) {
                 int y = 0;
                 for (int k = 0; k < i; k++) {
                     y += data.getSpriteHeights()[k];
                 }
                 sprites[j] = new Sprite(data.getSpriteWidths()[i], data.
                         getSpriteHeights()[i], (data.getSheetSpacing() * (j + 
                         1)) + (data.getSpriteWidths()[i] * j), (data.
                         getSheetSpacing() * (i + 1)) + y, sheet);
             }
             anims.add(i, sprites);
         }
         
         defaultMapIdle = new Animation(data.getNumFrames()[0], anims.get(0), 
                 data.getFrameDurations()[0]);
         defaultMapIdle.setKeyFrame(data.getKeyFrames()[0]);
         defaultMapMoveDown = new Animation(data.getNumFrames()[1], anims.get(1), 
                 data.getFrameDurations()[1]);
         defaultMapMoveDown.setKeyFrame(data.getKeyFrames()[1]);
         defaultMapMoveLeft = new Animation(data.getNumFrames()[2], anims.get(2), 
                 data.getFrameDurations()[2]);
         defaultMapMoveLeft.setKeyFrame(data.getKeyFrames()[2]);
         defaultMapMoveRight = new Animation(data.getNumFrames()[3], anims.get(3), 
                 data.getFrameDurations()[3]);
         defaultMapMoveRight.setKeyFrame(data.getKeyFrames()[3]);
         defaultMapMoveUp = new Animation(data.getNumFrames()[4], anims.get(4), 
                 data.getFrameDurations()[4]);
         defaultMapMoveUp.setKeyFrame(data.getKeyFrames()[4]);
         defaultMapSelected = new Animation(data.getNumFrames()[5], anims.get(5), 
                 data.getFrameDurations()[5]);
         defaultMapSelected.setKeyFrame(data.getKeyFrames()[5]);
         defaultMapAttackDown = new Animation(data.getNumFrames()[6], anims.get(6), 
                 data.getFrameDurations()[6]);
         defaultMapAttackDown.setKeyFrame(data.getKeyFrames()[6]);
         defaultMapAttackLeft = new Animation(data.getNumFrames()[7], anims.get(7), 
                 data.getFrameDurations()[7]);
         defaultMapAttackLeft.setKeyFrame(data.getKeyFrames()[7]);
         defaultMapAttackRight = new Animation(data.getNumFrames()[8], anims.get(8), 
                 data.getFrameDurations()[8]);
         defaultMapAttackRight.setKeyFrame(data.getKeyFrames()[8]);
         defaultMapAttackUp = new Animation(data.getNumFrames()[9], anims.get(9), 
                 data.getFrameDurations()[9]);
         defaultMapAttackUp.setKeyFrame(data.getKeyFrames()[9]);
         defaultMapMeleeDown = new Animation(data.getNumFrames()[10], anims.get(10), 
                 data.getFrameDurations()[10]);
         defaultMapMeleeDown.setKeyFrame(data.getKeyFrames()[10]);
         defaultMapMeleeLeft = new Animation(data.getNumFrames()[11], anims.get(11), 
                 data.getFrameDurations()[11]);
         defaultMapMeleeLeft.setKeyFrame(data.getKeyFrames()[11]);
         defaultMapMeleeRight = new Animation(data.getNumFrames()[12], anims.get(12), 
                 data.getFrameDurations()[12]);
         defaultMapMeleeRight.setKeyFrame(data.getKeyFrames()[12]);
         defaultMapMeleeUp = new Animation(data.getNumFrames()[13], anims.get(13), 
                 data.getFrameDurations()[13]);
         defaultMapMeleeUp.setKeyFrame(data.getKeyFrames()[13]);
         defaultMapEvadeDown = new Animation(data.getNumFrames()[14], anims.get(14), 
                 data.getFrameDurations()[14]);
         defaultMapEvadeDown.setKeyFrame(data.getKeyFrames()[14]);
         defaultMapEvadeLeft = new Animation(data.getNumFrames()[15], anims.get(15), 
                 data.getFrameDurations()[15]);
         defaultMapEvadeLeft.setKeyFrame(data.getKeyFrames()[15]);
         defaultMapEvadeRight = new Animation(data.getNumFrames()[16], anims.get(16), 
                 data.getFrameDurations()[16]);
         defaultMapEvadeRight.setKeyFrame(data.getKeyFrames()[16]);
         defaultMapEvadeUp = new Animation(data.getNumFrames()[17], anims.get(17), 
                 data.getFrameDurations()[17]);
         defaultMapEvadeUp.setKeyFrame(data.getKeyFrames()[17]);
         defaultMapDamageDown = new Animation(data.getNumFrames()[18], anims.get(18), 
                 data.getFrameDurations()[18]);
         defaultMapDamageDown.setKeyFrame(data.getKeyFrames()[18]);
         defaultMapDamageLeft = new Animation(data.getNumFrames()[19], anims.get(19), 
                 data.getFrameDurations()[19]);
         defaultMapDamageLeft.setKeyFrame(data.getKeyFrames()[19]);
         defaultMapDamageRight = new Animation(data.getNumFrames()[20], anims.get(20), 
                 data.getFrameDurations()[20]);
         defaultMapDamageRight.setKeyFrame(data.getKeyFrames()[20]);
         defaultMapDamageUp = new Animation(data.getNumFrames()[21], anims.get(21), 
                 data.getFrameDurations()[21]);
         defaultMapDamageUp.setKeyFrame(data.getKeyFrames()[21]);
         
     }

    public String getName() {
        return name;
    }

    public boolean isFemale() {
        return female;
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

    public Animation getDefaultMapIdle() {
        return defaultMapIdle;
    }

    public Animation getDefaultMapMoveUp() {
        return defaultMapMoveUp;
    }

    public Animation getDefaultMapMoveLeft() {
        return defaultMapMoveLeft;
    }

    public Animation getDefaultMapMoveRight() {
        return defaultMapMoveRight;
    }

    public Animation getDefaultMapMoveDown() {
        return defaultMapMoveDown;
    }

    public Animation getDefaultMapSelected() {
        return defaultMapSelected;
    }
    
    public Animation getDefaultMapAttackUp() {
        return defaultMapAttackUp;
    }

    public Animation getDefaultMapAttackLeft() {
        return defaultMapAttackLeft;
    }

    public Animation getDefaultMapAttackRight() {
        return defaultMapAttackRight;
    }

    public Animation getDefaultMapAttackDown() {
        return defaultMapAttackDown;
    }

    public Animation getDefaultMapMeleeUp() {
        return defaultMapMeleeUp;
    }

    public Animation getDefaultMapMeleeLeft() {
        return defaultMapMeleeLeft;
    }

    public Animation getDefaultMapMeleeRight() {
        return defaultMapMeleeRight;
    }

    public Animation getDefaultMapMeleeDown() {
        return defaultMapMeleeDown;
    } 

    public Animation getDefaultMapEvadeUp() {
        return defaultMapEvadeUp;
    }

    public Animation getDefaultMapEvadeLeft() {
        return defaultMapEvadeLeft;
    }

    public Animation getDefaultMapEvadeRight() {
        return defaultMapEvadeRight;
    }

    public Animation getDefaultMapEvadeDown() {
        return defaultMapEvadeDown;
    }

    public Animation getDefaultMapDamageUp() {
        return defaultMapDamageUp;
    }

    public Animation getDefaultMapDamageLeft() {
        return defaultMapDamageLeft;
    }

    public Animation getDefaultMapDamageRight() {
        return defaultMapDamageRight;
    }

    public Animation getDefaultMapDamageDown() {
        return defaultMapDamageDown;
    }
    
    public int[] getMovetype() {
        return movetype;
    }
    
    public int getMoveCost(int index) {
        return movetype[index];
    }
}
