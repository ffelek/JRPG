package characters;

import lombok.Getter;
import lombok.Setter;
import equipments.Equipment;
import java.util.List;

@Getter
@Setter
public final class Character extends ACharacterAttributes
{
	private String name;
	private int weight;
	private List<Equipment> equipments;

	public Character(int id, String name, int hp, int mp, int stamina, int weight)
	{
		this.setId(id);
		this.setName(name);
		this.setHp(hp);
		this.setInitialHp(hp);
		this.setMp(mp);
		this.setInitialMp(mp);
		this.setStamina(stamina);
		this.setInitialStamina(stamina);
		this.setWeight(weight);
	}
}
