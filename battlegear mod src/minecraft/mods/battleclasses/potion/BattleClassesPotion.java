package mods.battleclasses.potion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;

public class BattleClassesPotion extends Potion {
	
	protected BattleClassesPotion(int id, boolean badEffect, int liquidColorCode) {
		super(id, badEffect, liquidColorCode);
	}

	public static final int POTION_TYPES_CAPACITY = 256;
	public static void increasePotionTypesCapacity() {
		if(Potion.potionTypes.length >= POTION_TYPES_CAPACITY) {
			System.out.println("Battle Classes mod trying to increase size of Potion.potionTypes array, but it's already modified. Current lenght:" + potionTypes.length);
		}
		for (Field f : Potion.class.getDeclaredFields()) {
	        f.setAccessible(true);
	        try {
	            if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
	                Field modfield = Field.class.getDeclaredField("modifiers");
	                modfield.setAccessible(true);
	                modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

	                //Potion.potionTypes = (Potion[])f.get(null);
	                final Potion[] newPotionTypes = new Potion[POTION_TYPES_CAPACITY];
	                System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
	                f.set(null, newPotionTypes);
	            }
	        } catch (Exception e) {
	            System.err.println("Severe error, please report this to the mod author:");
	            System.err.println(e);
	        }
	    }
	}
}
