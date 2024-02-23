package bootstrap;

import characters.Character;
import characters.Boss;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import enums.EquipmentType;
import enums.WeaponType;
import equipments.Equipment;
import equipments.Weapon;
import utils.Constants;
import utils.Randomizer;

import java.io.IOException;
import java.util.*;

public class Loader
{
	public static final List<Character> playableClasses = new ArrayList<>();
	public static final List<Boss> bosses = new ArrayList<>();
	public static final List<Weapon> weapons = new ArrayList<>();
	public static final List<Equipment> equipments = new ArrayList<>();
	public static JsonObject texts = new JsonObject();
	public static JsonObject stories = new JsonObject();
	private static final List<WeaponType> WEAPON_TYPES_VALUES = List.of(WeaponType.values());
	private static final int WEAPON_TYPES_SIZE = WEAPON_TYPES_VALUES.size() - 1;
	private static final List<EquipmentType> EQUIPMENT_TYPES_VALUES = List.of(EquipmentType.values());
	private static final int EQUIPMENT_TYPES_SIZE = EQUIPMENT_TYPES_VALUES.size() - 1;

	public static void init()
	{
		try
		{
			Json.loadData();
			Loader.loadStories();
			Loader.loadPlayableClasses();
			Loader.loadBosses();
			Loader.loadWeapons();
			Loader.loadEquipments();
		}
		catch (IOException err)
		{
			System.out.println(err.getLocalizedMessage() + "\nThe game will now stop.");
			System.exit(0);
		}
	}

	private static void loadPlayableClasses()
	{
		JsonArray playableClasses = (JsonArray) Json.data.get("classes");
		for (JsonElement playableClass : playableClasses)
		{
			JsonObject j = playableClass.getAsJsonObject();
			String className = j.get("name").getAsString();
			int id = Integer.parseInt(j.get("id").getAsString());
			int hp = Integer.parseInt(j.get("hp").getAsString());
			int mp = Integer.parseInt(j.get("mp").getAsString());
			int stamina = Integer.parseInt(j.get("stamina").getAsString());
			int weight = Integer.parseInt(j.get("weight").getAsString());
			className = className.substring(0, 1).toUpperCase() + className.substring(1);
			Character pc = new Character(id, className, hp, mp, stamina, weight);
			Loader.playableClasses.add(pc);
		}
	}

	private static void loadBosses()
	{
		JsonArray bosses = (JsonArray) Json.data.get("boss");
		for (JsonElement boss : bosses)
		{
			JsonObject j = boss.getAsJsonObject();
			String bossName = j.get("name").getAsString();
			String nickname = j.get("nickname").getAsString();
			int level = Integer.parseInt(j.get("level").getAsString());
			int id = Integer.parseInt(j.get("id").getAsString());
			int hp = Integer.parseInt(j.get("hp").getAsString());
			int mp = Integer.parseInt(j.get("mp").getAsString());
			int stamina = Integer.parseInt(j.get("stamina").getAsString());
			bossName = bossName.substring(0, 1).toUpperCase() + bossName.substring(1);
			nickname = nickname.substring(0, 1).toUpperCase() + nickname.substring(1);
			Boss b = new Boss(id, level, bossName, nickname, hp, mp, stamina);
			Loader.bosses.add(b);
		}
	}

	private static void loadWeapons()
	{
		JsonArray weapons = (JsonArray) Json.data.get("weapons");
		for (JsonElement weapon : weapons)
		{
			JsonObject j = weapon.getAsJsonObject();
			String weaponName = j.get("name").getAsString();
			String damage = j.get("damage").getAsString();
			int id = Integer.parseInt(j.get("id").getAsString());
			int durability = Integer.parseInt(j.get("durability").getAsString());
			int cost = Integer.parseInt(j.get("cost").getAsString());
			weaponName = weaponName.substring(0, 1).toUpperCase() + weaponName.substring(1);
			Weapon w = new Weapon(id, weaponName, damage, durability, cost, Loader.randomWeaponType());
			Loader.weapons.add(w);
		}
	}

	private static void loadEquipments()
	{
		JsonArray equipments = (JsonArray) Json.data.get("equipments");
		for (JsonElement equipment : equipments)
		{
			JsonObject j = equipment.getAsJsonObject();
			String equipmentName = j.get("name").getAsString();
			int id = Integer.parseInt(j.get("id").getAsString());
			int defense = Integer.parseInt(j.get("defense").getAsString());
			int durability = Integer.parseInt(j.get("durability").getAsString());
			equipmentName = equipmentName.substring(0, 1).toUpperCase() + equipmentName.substring(1);
			Equipment e = new Equipment(id, equipmentName, defense, durability, Loader.randomEquipmentType());
			Loader.equipments.add(e);
		}
	}

	private static void loadStories()
	{
		Loader.texts = (JsonObject) Json.data.get(Constants.TEXT_KEY);
		Loader.stories = (JsonObject) Loader.texts.get(Constants.STORIES_KEY);
	}

	public static WeaponType randomWeaponType()
	{
		return WEAPON_TYPES_VALUES.get(Randomizer.randomInRange(WEAPON_TYPES_SIZE, 1));
	}

	public static EquipmentType randomEquipmentType()
	{
		return EQUIPMENT_TYPES_VALUES.get(Randomizer.randomInRange(EQUIPMENT_TYPES_SIZE, 1));
	}

}
