/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fer.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;

/**
 *
 * @author Evan
 */
public class UnitSerializer {
    
    public static void main(String[] args) {
        WeaponData weapon = new WeaponData();
        weapon.setAccuracy(90);
        weapon.setDamage(5);
        weapon.setName("ACR M01");
        weapon.setMelee(true);
        weapon.setPierce(2);
        weapon.setRange(3);
        weapon.setSheetHeight(18);
        weapon.setSheetPath("/res/images/icons/WeaponIconset.png");
        weapon.setSheetTransparentColor(-16776961);
        weapon.setSheetWidth(18);
        weapon.setSpriteHeight(16);
        weapon.setSpriteWidth(16);
        weapon.setUses(45);
        weapon.setWeight(2);
        weapon.setX(1);
        weapon.setY(1);
        WeaponData weapon2 = new WeaponData();
        WeaponData[] weapons = new WeaponData[] {weapon, weapon2};
        ItemData item = new ItemData();
        item.setActionName("HEAL");
        item.setHeal(10);
        item.setIndex(1);
        item.setName("MORPHINE H");
        item.setPrice(500);
        item.setSheetHeight(18);
        item.setSheetPath("/res/images/icon/ItemIconset.png");
        item.setSheetWidth(18);
        item.setSheetTransparentColor(-16776961);
        item.setSpriteHeight(16);
        item.setSpriteWidth(16);
        item.setX(1);
        item.setY(1);
        /*item.test = new int[2][2];
        item.test[0][0] = 1;
        item.test[0][1] = 2;
        item.test[1][0] = 3;
        item.test[1][1] = 4;*/
        XStream xstream = new XStream();
        String xml = xstream.toXML(item);
        System.out.println(xml);
    }
    
}
