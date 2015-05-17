package fer.util;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * @author Evan Stewart
 */
public class WeaponReader {

	public static void main(String[] args) {
		WeaponData weapon = null;
		Builder builder = new Builder();
		File dir = new File(".");
		File[] dirFiles = dir.listFiles();
		File xmlFile = null;
		for (int i = 0; i < dirFiles.length; i++) {
			System.out.println(dirFiles[i].getName());
			if (dirFiles[i].isDirectory()
					&& "data".equals(dirFiles[i].getName())) {
				File[] dataFiles = dirFiles[i].listFiles();
				System.out.println("===In data:===");
				for (int j = 0; j < dataFiles.length; j++) {
					System.out.println(dataFiles[j].getName());
					if ("Weapons.xml".equals(dataFiles[j].getName())) {
						xmlFile = dataFiles[j];
						System.out.println("===File found===");
					}
				}
			}
		}
		if (xmlFile != null) {
			try {
				Document doc = builder.build(xmlFile);
				System.out.println("===Got document===");
				Element root = doc.getRootElement();
				System.out.println("===Got root===");
				Elements weapons = root.getChildElements();
				System.out.println("===Got " + weapons.size() + " children===");
				for (int i = 0; i < weapons.size(); i++) {
					Elements properties = weapons.get(i).getChildElements();
					System.out.println("===Got " + properties.size()
							+ " children===");
					for (int j = 0; j < properties.size(); j++) {
						if ("1".equals(properties.get(j).getValue())
								&& "index".equals(properties.get(j)
										.getLocalName())) {
							System.out.println("===Found index===");
							System.out.println("===Index xml:===");
							System.out.println(weapons.get(i).toXML());
							XStream xstream = new XStream();
							weapon = (WeaponData) xstream.fromXML(weapons
									.get(i).toXML());
							System.out.println("===Serialized===");
						}
					}
				}
			} catch (ParsingException ex) {
				Logger.getLogger(WeaponReader.class.getName()).log(
						Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(WeaponReader.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			System.out.println("Has probbum.");
		}
		if (weapon != null) {
			System.out.println("Yayz");
		}
	}
}
