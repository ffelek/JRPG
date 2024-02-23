package bootstrap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import utils.Constants;
import config.PropertiesFile;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Class loading the JSON source file and parsing it
 * @author felek
 */
public class Json
{
	public static JsonObject data = new JsonObject();

	/**
	 * Retrieves path from properties file and read the file to parse its content into JsonObject
	 * @throws FileNotFoundException if file is not found
	 */
	public static void loadData() throws FileNotFoundException
	{
		String s = PropertiesFile.getProperty(Constants.CONFIG_FILE_ACCESS_KEY);
		if (s != null && !s.isEmpty())
		{
			JsonReader reader = new JsonReader(new FileReader(s));
			Gson gson = new Gson();
			data = gson.fromJson(reader, JsonObject.class);
		}
	}
}