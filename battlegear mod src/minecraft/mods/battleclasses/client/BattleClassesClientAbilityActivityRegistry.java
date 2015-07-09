package mods.battleclasses.client;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;

/**
 * Registry for currently ongoing spellcasts to play casting FX and release/launch FX.
 * This maintains the sync of the ability related FX (casting and release sounds and animations) between clients.
 * Stored activity data can be used for render effects also.
 * @author Zsolt Molnár
 */
public class BattleClassesClientAbilityActivityRegistry {
	
	/**
	 * Struct-like class representing an ongoing spellcast. 
	 * @author Zsolt Molnár
	 */
	class CastingActivity {
		
		public String abilityID;
		public String getAbilityID() {
			return abilityID;
		}
		public float castingDuration;
		public float getCastingDuration() {
			return castingDuration;
		}

		CastingActivity(String abilityID, float duration) {
			this.abilityID = abilityID;
	    	this.castingDuration = duration;
		}
		
		public void beginFX() {
			System.out.println("Should Begin Casting FX");
			//Stub method, start casting sound here, etc....
		}
		
		public void endFX() {
			System.out.println("Should End Casting FX");
			//Stub method, stop casting sound here, etc....
		}
	}
	
	/**
	 * Stores the active abilities being casted at the moment.
	 * Key - Player Name
	 * Value - AbilityActivity < String abilityID & float duration >
	 */
	public HashMap<String,CastingActivity> activityMap = new HashMap<String,CastingActivity>();
	private Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Saves the player casting the given ability for the given duration, also starts playing the related casting FX.
	 * @param playerName
	 * @param abilityID
	 * @param duration
	 */
	public void addActivity(String playerName, String abilityID, float duration) {
		CastingActivity activity = new CastingActivity(abilityID, duration);
		activity.beginFX();
		this.activityMap.put(playerName, new CastingActivity(abilityID, duration));
	}
	
	/**
	 * Removes the player casting, also stops playing the related casting FX.
	 * @param playerName
	 */
	public void removeActivity(String playerName) {
		CastingActivity activity = this.activityMap.get(playerName);
		if(activity != null) {
			activity.endFX();
			this.activityMap.remove(playerName);
		}
	}
	
	/**
	 * Plays the launch/release effect of the given ability at the given player.
	 * @param playerName
	 * @param abilityID
	 */
	public void playAbilityLaunchFX(String playerName, String abilityID) {
		EntityPlayer entityPlayer = mc.thePlayer.worldObj.getPlayerEntityByName(playerName);
		if(mc.thePlayer.worldObj.getPlayerEntityByName(playerName) != null) {
			System.out.println("Should Play Release FX");
			//Stub method, play release/launch animation and sound of ability here
		}
	}
}