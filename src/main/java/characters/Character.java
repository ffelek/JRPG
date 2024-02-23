package characters;

import lombok.Getter;
import lombok.Setter;
import equipments.Equipment;
import java.util.List;

@Getter
@Setter
public final class Character extends ACharacterAttributes
{
	private int weight;
	private List<Equipment> equipments;

	public Character(int id, String name, int hp, int mp, int stamina, int weight)
	{
		super(id, name, "", hp, mp, stamina);
		this.setWeight(weight);
	}
}
