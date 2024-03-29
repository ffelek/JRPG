package config;

import utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads the properties file and stores its content
 * @author felek
 */
public class PropertiesFile
{
	private static final Properties prop = new Properties();

	/**
	 * Retrieves property value by its name
	 * @param propertyName the property name
	 * @return the value for the specified property
	 */
	public static String getProperty(String propertyName)
	{
		String property = prop.getProperty(propertyName);
		return property.trim().isEmpty() ? null : property.trim();
	}

	/**
	 * Reads and loads the properties file into the class
	 */
	public static void loadProperties()
	{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream(Constants.CONFIG_FILE_KEY);
		try
		{
			prop.load(stream);
		}
		catch (IOException err)
		{
			System.out.println(err.getMessage());
			System.exit(0);
		}
	}
}
