package fer.util;

import com.thoughtworks.xstream.XStream;
import fer.Game;
import fer.UnitClass;
import fer.graphics.SpriteSheet;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 * @author Evan Stewart
 */
public class XMLReader {

    private XStream xstream;
    private Builder builder;

    public XMLReader() {
        xstream = new XStream();
        builder = new Builder();
    }

    public Document getXMLDocument(String filepath) {
    	String separator = System.getProperty("file.separator");
        Document xmlDoc = null;
        String absPath = null;
        absPath = System.getProperty("user.dir");
        if (absPath.endsWith(separator + "dist"+ separator + "Apotheosis.jar")) {
            absPath = absPath.substring(0, absPath.length() - (separator + "dist"+ separator + "Apotheosis.jar").length());
        } else if (absPath.endsWith(separator + "build" + separator +"classes" + separator)) {
            absPath = absPath.substring(0, absPath.length() - (separator + "build" + separator +"classes" + separator).length());
        } else if(absPath.endsWith(separator + "bin" + separator)){ //added for Eclipse compatibility
        	absPath = absPath.substring(0, absPath.length() -(separator + "bin" + separator).length());
        }else if(absPath.endsWith(separator + "bin")){ //added for Eclipse compatibility
        	absPath = absPath.substring(0, absPath.length() -(separator + "bin").length());
        }
        absPath += (separator + "data" + separator + filepath);
        try {
            /*File currentDir = new File(".");
            if (currentDir.getAbsoluteFile().getParentFile().getName().equals("dist")) { //If run from autogenerated 
                //dist folder in project directry
                System.out.println("Dist");
                currentDir = currentDir.getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile();
            }
            File[] currentDirFiles = currentDir.listFiles();
            for (int i = 0; i < currentDirFiles.length; i++) {
                System.out.println("Name " + currentDirFiles[i].getName());
                if (currentDirFiles[i].isDirectory() && currentDirFiles[i].getName().equals("data")) {
                    System.out.println("Found data.");
                    currentDir = currentDirFiles[i];
                    break;
                }
            }
            currentDirFiles = currentDir.listFiles();
            for (int i = 0; i < currentDirFiles.length; i++) {
                if (currentDirFiles[i].getName().equals(filename)) {
                    try {
                        xmlDoc = builder.build(currentDirFiles[i]);
                    } catch (ParsingException ex) {
                        Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }*/
            xmlDoc = builder.build(new File(absPath));
        } catch (ParsingException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (xmlDoc == null) {
            System.out.println("Blegh");
        }
        return xmlDoc;
    }

    /*public Document getMapXMLDocument(String filename, int gameType, int index) {
        Document xmlDoc = null;
        Builder builder = new Builder();
        File currentDir = new File(".");
        if (currentDir.getName().equals("dist")) { //If run from autogenerated 
            //dist folder in project directry
            currentDir = currentDir.getParentFile();
        }
        //Get the data directory
        File[] currentDirFiles = currentDir.listFiles();
        for (int i = 0; i < currentDirFiles.length; i++) {
            if (currentDirFiles[i].isDirectory() && currentDirFiles[i].getName().equals("data")) {
                currentDir = currentDirFiles[i];
                break;
            }
        }
        //Get the maps directory
        currentDirFiles = currentDir.listFiles();
        for (int i = 0; i < currentDirFiles.length; i++) {
            if (currentDirFiles[i].isDirectory() && currentDirFiles[i].getName().equals("Maps")) {
                currentDir = currentDirFiles[i];
                break;
            }
        }
        //Get the correct gamemode directory
        currentDirFiles = currentDir.listFiles();
        switch (gameType) {
            case (0):
                for (int i = 0; i < currentDirFiles.length; i++) {
                    if (currentDirFiles[i].isDirectory() && currentDirFiles[i].getName().equals("Freeplay")) {
                        currentDir = currentDirFiles[i];
                        break;
                    }
                }
                break;
            default:
                for (int i = 0; i < currentDirFiles.length; i++) {
                    if (currentDirFiles[i].isDirectory() && currentDirFiles[i].getName().equals("Freeplay")) {
                        currentDir = currentDirFiles[i];
                        break;
                    }
                }
                break;
        }
        //Get the correct map index
        currentDirFiles = currentDir.listFiles();
        for (int i = 0; i < currentDirFiles.length; i++) {
            if (currentDirFiles[i].isDirectory() && currentDirFiles[i].getName().equals(Integer.toString(index))) {
                currentDir = currentDirFiles[i];
                break;
            }
        }
        //Get the XML document
        currentDirFiles = currentDir.listFiles();
        for (int i = 0; i < currentDirFiles.length; i++) {
            if (currentDirFiles[i].getName().equals(filename)) {
                try {
                    xmlDoc = builder.build(currentDirFiles[i]);
                } catch (ParsingException ex) {
                    Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        return xmlDoc;
    }*/

    public WeaponData serializeWeaponData(int index) {
        WeaponData weaponData = null;
        Document doc = getXMLDocument("Weapons.xml");
        Element root = doc.getRootElement();
        Elements weapons = root.getChildElements();
        Elements properties;
        for (int i = 0; i < weapons.size(); i++) {
            properties = weapons.get(i).getChildElements();
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")
                        && properties.get(j).getValue().equals(Integer.toString(index))) {
                    weaponData = (WeaponData) xstream.fromXML(weapons.get(i).toXML());
                }
            }
        }
        return weaponData;
    }

    public ArrayList<WeaponData> serializeAllWeaponData() {
        ArrayList<WeaponData> weaponData = new ArrayList();
        Document doc = getXMLDocument("Weapons.xml");
        Element root = doc.getRootElement();
        Elements weapons = root.getChildElements();
        Elements properties;
        for (int i = 0; i < weapons.size(); i++) {
            properties = weapons.get(i).getChildElements();
            boolean added = false;
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")) {
                    weaponData.add(Integer.parseInt(properties.get(j).
                            getValue()), (WeaponData) xstream.fromXML(
                            weapons.get(i).toXML()));
                    added = true;
                    break;
                }
            }
            if (!added) {
                weaponData.add((WeaponData) xstream.fromXML(weapons.get(i).toXML()));
            }
        }
        return weaponData;
    }

    public ItemData serializeItemData(int index) {
        ItemData itemData = null;
        Document doc = getXMLDocument("Items.xml");
        Element root = doc.getRootElement();
        Elements items = root.getChildElements();
        Elements properties;
        for (int i = 0; i < items.size(); i++) {
            properties = items.get(i).getChildElements();
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")
                        && properties.get(j).getValue().equals(Integer.toString(index))) {
                    itemData = (ItemData) xstream.fromXML(items.get(i).toXML());
                }
            }
        }
        return itemData;
    }

    public ArrayList<ItemData> serializeAllItemData() {
        ArrayList<ItemData> itemData = new ArrayList();
        Document doc = getXMLDocument("Items.xml");
        Element root = doc.getRootElement();
        Elements items = root.getChildElements();
        Elements properties;
        for (int i = 0; i < items.size(); i++) {
            properties = items.get(i).getChildElements();
            boolean added = false;
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")) {
                    itemData.add(Integer.parseInt(properties.get(j).
                            getValue()), (ItemData) xstream.fromXML(
                            items.get(i).toXML()));
                    added = true;
                    break;
                }
            }
            if (!added) {
                itemData.add((ItemData) xstream.fromXML(items.get(i).toXML()));
            }
        }
        return itemData;
    }

    public ArmorData serializeArmorData(int index) {
        ArmorData armorData = null;
        Document doc = getXMLDocument("Armor.xml");
        Element root = doc.getRootElement();
        Elements armor = root.getChildElements();
        Elements properties;
        for (int i = 0; i < armor.size(); i++) {
            properties = armor.get(i).getChildElements();
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")
                        && properties.get(j).getValue().equals(Integer.toString(index))) {
                    armorData = (ArmorData) xstream.fromXML(armor.get(i).toXML());
                }
            }
        }
        return armorData;
    }

    public ArrayList<ArmorData> serializeAllArmorData() {
        ArrayList<ArmorData> armorData = new ArrayList();
        Document doc = getXMLDocument("Armor.xml");
        Element root = doc.getRootElement();
        Elements armor = root.getChildElements();
        Elements properties;
        for (int i = 0; i < armor.size(); i++) {
            properties = armor.get(i).getChildElements();
            boolean added = false;
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")) {
                    armorData.add(Integer.parseInt(properties.get(j).
                            getValue()), (ArmorData) xstream.fromXML(
                            armor.get(i).toXML()));
                    added = true;
                    break;
                }
            }
            if (!added) {
                armorData.add((ArmorData) xstream.fromXML(armor.get(i).toXML()));
            }
        }
        return armorData;
    }

    public TileData serializeTileData(int index) {
        TileData tileData = null;
        Document doc = getXMLDocument("Tiles.xml");
        Element root = doc.getRootElement();
        Elements tiles = root.getChildElements();
        Elements properties;
        for (int i = 0; i < tiles.size(); i++) {
            properties = tiles.get(i).getChildElements();
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")
                        && properties.get(j).getValue().equals(Integer.toString(index))) {
                    tileData = (TileData) xstream.fromXML(tiles.get(i).toXML());
                }
            }
        }
        return tileData;
    }

    public ArrayList<TileData> serializeAllTileData() {
        ArrayList<TileData> tileData = new ArrayList();
        Document doc = getXMLDocument("Tiles.xml");
        Element root = doc.getRootElement();
        Elements tiles = root.getChildElements();
        Elements properties;
        for (int i = 0; i < tiles.size(); i++) {
            properties = tiles.get(i).getChildElements();
            boolean added = false;
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")) {
                    tileData.add(Integer.parseInt(properties.get(j).
                            getValue()), (TileData) xstream.fromXML(
                            tiles.get(i).toXML()));
                    added = true;
                    break;
                }
            }
            if (!added) {
                tileData.add((TileData) xstream.fromXML(tiles.get(i).toXML()));
            }
        }
        return tileData;
    }

    public UnitClassData serializeUnitClassData(int index) {
        UnitClassData ucData = null;
        Document doc = getXMLDocument("UnitClasses.xml");
        Element root = doc.getRootElement();
        Elements uc = root.getChildElements();
        Elements properties;
        for (int i = 0; i < uc.size(); i++) {
            properties = uc.get(i).getChildElements();
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")
                        && properties.get(j).getValue().equals(Integer.toString(index))) {
                    ucData = (UnitClassData) xstream.fromXML(uc.get(i).toXML());
                }
            }
        }
        return ucData;
    }

    public ArrayList<UnitClassData> serializeAllUnitClassData() {
        ArrayList<UnitClassData> ucData = new ArrayList();
        Document doc = getXMLDocument("UnitClasses.xml");
        Element root = doc.getRootElement();
        Elements uc = root.getChildElements();
        Elements properties;
        for (int i = 0; i < uc.size(); i++) {
            properties = uc.get(i).getChildElements();
            boolean added = false;
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")) {
                    ucData.add(Integer.parseInt(properties.get(j).
                            getValue()), (UnitClassData) xstream.fromXML(
                            uc.get(i).toXML()));
                    added = true;
                    break;
                }
            }
            if (!added) {
                ucData.add((UnitClassData) xstream.fromXML(uc.get(i).toXML()));
            }
        }
        return ucData;
    }
    
    /**
     * Returns the MapData messenger class for the specified map index of the
     * specified game mode
     *
     * @param gameType The game mode (freeplay, campaign etc.) that contains the
     * requested map. Enumeration: 0 = Freeplay
     * @param index The index of the requested map. Determines which directory
     * will be searched for the Map.xml file
     * @return The MapData for the specified map
     */
    public MapData serializeMapData(int gameType, int index) {
        MapData mapData = null;
        Document doc = null;
        switch(gameType) {
            case 0: //Freeplay maps
                doc = getXMLDocument("Maps/Freeplay/" + Integer.toString(index) + "/Map.xml");
            default: //Default to freeplay, for now
                doc = getXMLDocument("Maps/Freeplay/" + Integer.toString(index) + "/Map.xml");
        }
        Element root = doc.getRootElement();
        Elements maps = root.getChildElements();
        //Get the first map, as there should only be one
        mapData = (MapData) xstream.fromXML(maps.get(0).toXML());
        return mapData;
    }

    /**
     * Returns the GoalData messenger classes for all of the goals for the
     * specified map index of the specified game mode.
     *
     * @param gameType The game mode (freeplay, campaign etc.) that contains the
     * requested map. Enumeration: 0 = Freeplay
     * @param index The index of the requested map. Determines which directory
     * will be searched for the Goals.xml file
     * @return An arraylist containing each of the map goals for the requested
     * map, indexed by their corresponding faction
     */
    public ArrayList<GoalData> serializeAllGoalData(int gameType, int index) {
        ArrayList<GoalData> goalData = new ArrayList();
        Document doc = null;
        switch(gameType) {
            case 0: //Freeplay maps
                doc = getXMLDocument("Maps/Freeplay/" + Integer.toString(index) + "/Goals.xml");
            default: //Default to freeplay, for now
                doc = getXMLDocument("Maps/Freeplay/" + Integer.toString(index) + "/Goals.xml");
        }
        Element root = doc.getRootElement();
        Elements goals = root.getChildElements();
        Elements properties;
        for (int i = 0; i < goals.size(); i++) {
            properties = goals.get(i).getChildElements();
            boolean added = false;
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")) {
                    goalData.add(Integer.parseInt(properties.get(j).
                            getValue()), (GoalData) xstream.fromXML(
                            goals.get(i).toXML()));
                    added = true;
                    break;
                }
            }
            if (!added) {
                goalData.add((GoalData) xstream.fromXML(goals.get(i).toXML()));
            }
        }
        return goalData;
    }
    
    /**
     * Returns the UnitData messenger classes for all of the units for the
     * specified map index of the specified game mode.
     *
     * @param gameType The game mode (freeplay, campaign etc.) that contains the
     * requested map. Enumeration: 0 = Freeplay
     * @param index The index of the requested map. Determines which directory
     * will be searched for the Units.xml file
     * @return An arraylist containing each of the map goals for the requested
     * map, indexed by their in-game array indices 
     */
    public ArrayList<UnitData> serializeAllUnitData(int gameType, int index) {
        ArrayList<UnitData> unitData = new ArrayList();
        Document doc = null;
        switch(gameType) {
            case 0: //Freeplay maps
                doc = getXMLDocument("Maps/Freeplay/" + Integer.toString(index) + "/Units.xml");
            default: //Default to freeplay, for now
                doc = getXMLDocument("Maps/Freeplay/" + Integer.toString(index) + "/Units.xml");
        }
        Element root = doc.getRootElement();
        Elements units = root.getChildElements();
        Elements properties;
        for (int i = 0; i < units.size(); i++) {
            properties = units.get(i).getChildElements();
            boolean added = false;
            for (int j = 0; j < properties.size(); j++) {
                if (properties.get(j).getLocalName().equals("index")) {
                    unitData.add(Integer.parseInt(properties.get(j).
                            getValue()), (UnitData) xstream.fromXML(
                            units.get(i).toXML()));
                    added = true;
                    break;
                }
            }
            if (!added) {
                unitData.add((UnitData) xstream.fromXML(units.get(i).toXML()));
            }
        }
        return unitData;
    }
    
    public int getNumFreeplayMaps() {
        URL url = SpriteSheet.class.getProtectionDomain().getCodeSource().getLocation();
        String absPath = null;
        try {
            absPath = URLDecoder.decode(url.toString(), "UTF-8");
            absPath = url.getPath();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        //absPath = absPath.substring(6); //Cut off the "file:"
        if (absPath.endsWith("/dist/Apotheosis.jar")) {
            absPath = absPath.substring(0, absPath.length() - "/dist/Apotheosis.jar".length());
        } else if (absPath.endsWith("/build/classes/")) {
            absPath = absPath.substring(0, absPath.length() - "/build/classes/".length());
        } else if (absPath.endsWith("/bin/")) {
        	absPath = absPath.substring(0, absPath.length() - "/bin/".length());
        }
        absPath += "/data/Maps/Freeplay/";
        File dir = new File (absPath);
        return dir.listFiles().length;
    }

    public UnitClass readUnitClassFromIndex(String filename, int index) {
        Document doc;
        try {
            doc = builder.build(XMLReader.class.getResourceAsStream(filename));
            Element root = doc.getRootElement();
            Elements children = root.getChildElements();
            for (int i = 0; i < children.size(); i++) {
                Element type = children.get(i).getFirstChildElement("type");
                if (Integer.parseInt(type.getValue()) == index) {
                    String xml = children.get(i).toXML();
                    UnitClass uc = (UnitClass) xstream.fromXML(xml);
                    return uc;
                }
            }
        } catch (ValidityException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParsingException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        //No class of given index was found.
        return null;
    }
    
    public SettingsData serializeSettingsData() {
        SettingsData sData = null;
        Document doc = getXMLDocument("Settings.xml");
        Element root = doc.getRootElement();
        sData = (SettingsData) xstream.fromXML(root.toXML());
        return sData;
    }
    
    public void writeSettings() {
        URL url = SpriteSheet.class.getProtectionDomain().getCodeSource().getLocation();
        String absPath = null;
        try {
            absPath = URLDecoder.decode(url.toString(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        absPath = absPath.substring(6); //Cut off the "file:"
        if (absPath.endsWith("/dist/Apotheosis.jar")) {
            absPath = absPath.substring(0, absPath.length() - "/dist/Apotheosis.jar".length());
        } else if (absPath.endsWith("/build/classes/")) {
            absPath = absPath.substring(0, absPath.length() - "/build/classes/".length());
        }
        absPath += ("/data/settings.xml");
        
        SettingsData data = new SettingsData();
        data.setFactionShadows(Game.isDrawFactionShadow());
        data.setGridOpacity(Game.getGridOpacity());
        data.setGameScale(Game.getGameScale());
        try {
            FileWriter settingsFile = new FileWriter(new File(absPath));
            PrintWriter writer = new PrintWriter(settingsFile);
            writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xstream.toXML(data));
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
