package equipments;

import lombok.Getter;
import lombok.Setter;
import enums.EquipmentType;

@Getter
@Setter
public class Equipment
{
	private int id;
	private String name;
	private int defense;
	private int durability;
	private EquipmentType equipmentType;

	public Equipment(int id, String name, int defense, int durability, EquipmentType equipmentType)
	{
		this.setId(id);
		this.setName(name);
		this.setDefense(defense);
		this.setDurability(durability);
		this.setEquipmentType(equipmentType);
	}
}
