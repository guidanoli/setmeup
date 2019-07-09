package vars.properties;
import java.io.*;
import java.util.Properties;

import gui.defaults.DefaultNotificationPopup;
import gui.dialog.preferences.types.*;
import gui.dialog.preferences.types.combo.*;
import gui.error.FatalError;
import svn.BranchManager;
import vars.properties.types.LongBooleanProperty;

/**
 * <h1>Global Properties</h1>
 * 
 * <p>A way to store global data locally in XML files information about
 * the software usage, file states, etc.
 * <p>Keys are set with the <b>set</b> function
 * <p>and gotten by the <b>get</b> function 
 * <p>and stored by the <b>save</b> function, very intuitively.
 * 
 * @author guidanoli
 *
 */
@SuppressWarnings("serial")
public class GlobalProperties extends Properties {

	/* Local Resources */
	private static final String configFolder = vars.LocalResources.datafolder;
	private static final String propertiesFile = vars.LocalResources.properties;

	/* Properties */
	private static LongBooleanProperty notificationProperty;
	private static final Property [] properties = {
			new Property( "path", getDefaultPath(), new DirectoryPreferenceType(), false),
			new Property( "lang", "English", new ComboPreferenceType(new LanguageComboListener()), true ),
			new Property( "maxthreads", "3", new NumberPreferenceType(1,10), false ),
			new Property( "cleanups", "2", new NumberPreferenceType(1,10), false ),
			new Property( "makecmd", "mlldamt", new ComboPreferenceType(new MakeCmdCommandListener()), false ),
			new Property( "notify", getDefaultNotifications(), new TogglePreferenceType(GlobalProperties.notificationProperty), false),
	};
	
	/* Singleton instance */
	private static final GlobalProperties INSTANCE = new GlobalProperties();
	
	/**
	 * @return Global Properties singleton
	 */
	public static GlobalProperties getInstance() { return INSTANCE; }
	
	private GlobalProperties() {
		for( Property prop : properties )
			setProperty(prop.getKey(),prop.getDefaultValue());
		try {
			new File(configFolder).mkdirs();
			if ( !new File(propertiesFile).createNewFile() )
				loadFromXML(new FileInputStream(propertiesFile)); // if XML file is found, load it
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		save();
	}
	
	/* **************
	 * MAIN FUNCTIONS
	 * ************** */
	
	/**
	 * Get property value by dot separated key
	 * If key is branches.branch.name then the parameters
	 * passed should be "branches", "branch", "name".
	 * @param dot_separated_keys - strings separated by '.' in key
	 * @return property value for said key
	 */
	public String get(String... dot_separated_keys) {
		String resulting_key = String.join(".", dot_separated_keys);
		return get(resulting_key);
	}
	
	/**
	 * Get property value by its key
	 * @param key - property key
	 * @return property value for said key
	 */
	public String get(String key) { return getProperty(key); }
	
	/**
	 * Sets value to property of key equal to the result
	 * of joining the dot separated keys with '.'
	 * @param value - the value that will be attributed to key
	 * @param dot_separated_keys - fragments of key separated by '.'
	 */
	public void set(String value, String... dot_separated_keys ) {
		String resulting_key = String.join(".", dot_separated_keys);
		setProperty(resulting_key, value);
	}
	
	/**
	 * Sets value to property of a given key
	 * @param value - the value to be attributed to key
	 * @param key - the property key
	 */
	public void set(String value, String key) { setProperty(value,key); }
	
	/**
	 * Saves all changes to property object to a XML file in UTF-8 encoding
	 * @return true if stored successfully
	 */
	public boolean save() {
		try {
			storeToXML(new FileOutputStream(propertiesFile), null, "UTF-8");
			return true;
		} catch (IOException e) {
			FatalError.show(e,null,false);
			return false;
		}
	}
	
	/**
	 * Deletes unnecessary information stored within the XML file
	 * For example: keys set to their default value are removed
	 */
	public void cleanUp() {
		for(String key : stringPropertyNames()) {
			boolean removable = false;
			if( key.startsWith(BranchManager.KEY_BRANCHES) )
			{
				removable |= get(key).equals("false"); // removes "false" keys of check boxes from main dialog JTable
			}
			if( removable ) remove(key);
		}
		// save difference
		save();
	}
	
	/**
	 * Resets property value to default value
	 * @param key - property key
	 * @return the property default and current value or
	 * {@code null} if property key does not exist
	 * 
	 */
	public String resetPropertyToDefault(String key)
	{
		for( Property prop : properties )
		{
			if( prop.getKey().equals(key) )
			{
				setProperty(key, prop.getDefaultValue());
				return get(key);
			}
		}
		return null;
	}
	
	/**
	 * @return array of property objects
	 * @see Property
	 */
	public static Property [] getPropertiesArray() { return properties; }
	
	/* *******************
	 * AUXILIARY FUNCITONS
	 * ******************* */
	
	// get default path
	private static String getDefaultPath()
	{
		String [] candidates = {
				"D:\\users\\"+System.getProperty("user.name"),
				System.getProperty("user.home")
		};
		for( String c : candidates ) if(new File(c).exists()) return c;
		return null;
	}
	
	private static String getDefaultNotifications()
	{
		notificationProperty = new LongBooleanProperty(DefaultNotificationPopup.NOTIFICATION_TYPES_COUNT, true, "notify");
		return notificationProperty.getDefaultPropertyValue();
	}
	
}
