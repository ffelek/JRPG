package equipments;

import lombok.Getter;
import lombok.Setter;
import enums.EquipmentType;

/**
 * Defines an equipment
 * @author felek
 */
@Getter
@Setter
public class Equipment
{
	private int id;
	private String name;
	private int defense;
	private int durability;
	private EquipmentType equipmentType;

	/**
	 * Constructor for an equipment
	 * @param id id
	 * @param name name
	 * @param defense defense value (int)
	 * @param durability durability value (int)
	 * @param equipmentType equipment type (cf enum)
	 */
	public Equipment(int id, String name, int defense, int durability, EquipmentType equipmentType)
	{
		this.setId(id);
		this.setName(name);
		this.setDefense(defense);
		this.setDurability(durability);
		this.setEquipmentType(equipmentType);
	}
}
