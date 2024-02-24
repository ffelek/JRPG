package environments;

import enums.WeatherType;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines a weather
 * @author felek
 */
@Getter
@Setter
public class Weather
{
	private int id;
	private String name;
	private WeatherType wt;
}
