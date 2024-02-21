package environments;

import enums.WeatherType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather
{
	private int id;
	private String name;
	private WeatherType wt;
}
