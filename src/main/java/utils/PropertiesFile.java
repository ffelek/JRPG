package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFile
{
	private static final Properties prop = new Properties();

	public static String getProperty(String propertyName)
	{
		String property = prop.getProperty(propertyName);
		return property.trim().isEmpty() ? null : property.trim();
	}

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
