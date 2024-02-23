package bootstrap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import utils.Constants;
import config.PropertiesFile;
import java.io.FileReader;
import java.io.IOException;

public class Json
{
	public static JsonObject data = new JsonObject();

	public static void loadData() throws IOException
	{
		String s = PropertiesFile.getProperty(Constants.CONFIG_FILE_ACCESS_KEY);
		if (s != null && !s.isEmpty())
		{
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(s));
			data = gson.fromJson(reader, JsonObject.class);
		}
	}
}