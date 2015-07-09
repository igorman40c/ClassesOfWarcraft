package mods.battleclasses.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.sound.CastingSound;

/**
 * Registry for currently ongoing spellcasts to play casting FX and release/launch FX.
 * This maintains the sync of the ability related FX (casting and release sounds and animations) between clients.
 * Stored activity data can be used for render effects and overlays also.
 * 
 * @author Zsolt Molnár
 */
public class BattleClassesClientAbilityActivityRegistry {
	
	/**
	 * Struct-like class representing an ongoing spellcast. 
	 * 
	 * @author Zsolt Molnár
	 */
	class CastingActivity {
		public String playerCastingName;
		public String abilityID;
		public String getAbilityID() {
			return abilityID;
		}
		public float castingDuration;
		public float getCastingDuration() {
			return castingDuration;
		}

		CastingActivity(String playerCastingName, String abilityID, float duration) {
			this.playerCastingName = playerCastingName;
			this.abilityID = abilityID;
	    	this.castingDuration = duration;
		}
		
		public void beginFX() {
			System.out.println("Should Begin Casting FX");
			
			//Start playing casting sound
			String resourceName = BattleClassesAbstractAbility.getRegisteredActiveAbilityByID(this.abilityID).getCastingSoundResourceName();
			CastingSound.startCastingSound(playerCastingName, resourceName);
			
			//Stub method, start casting sound here, etc....
		}
		
		public void endFX() {
			System.out.println("Should End Casting FX");
			
			//Start playing casting sound
			CastingSound.stopCastingSound(playerCastingName);
			
			//Stub method, stop casting sound here, etc....
		}
		
		public boolean markedAsJunk = false;
		
		public boolean looksLikeJunk() {
			boolean junk = false;
			EntityPlayer entityPlayerCasting = mc.theWorld.getPlayerEntityByName(playerCastingName);
			if(entityPlayerCasting != null) {
				if(entityPlayerCasting.getItemInUse() != null) {
					junk = false;
				}
				else {
					junk = true;
				}
			}
			else {
				junk = true;
			}
			return junk;
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
		EntityPlayer entityPlayerCasting = mc.thePlayer.worldObj.getPlayerEntityByName(playerName);
		if(entityPlayerCasting != null) {
			CastingActivity activity = new CastingActivity(playerName, abilityID, duration);
			activity.beginFX();
			this.activityMap.put(playerName, activity);
		}
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
		if(entityPlayer != null) {
			System.out.println("Should Play Release FX");
			//Stub method, play release/launch animation and sound of ability here
		}
	}
	
	/**
	 * Enables or disables to whole junk checking process
	 */
	private boolean JUNK_CHECKING = true;
	/**
	 * True - remove junk on first check tick (if found to be invalid)
	 * False - remove junk on second check tick (if found to be invalid)
	 */
	private boolean JUNK_MARKING = false;
	/**
	 * Checks for stuck ongoing effects *junks*. Called on ClientTickEvent.END from ClientEvents.
	 */
	public void checkForJunk() {
		if(JUNK_CHECKING) {
			Iterator it = this.activityMap.entrySet().iterator();
			while (it.hasNext()) {
		        Map.Entry<String,CastingActivity> pair = (Map.Entry)it.next();
		        String playerName = pair.getKey();
			    CastingActivity castingActivity = pair.getValue();
			    if(castingActivity.looksLikeJunk()) {
			    	if(JUNK_MARKING) {
			    		if(!castingActivity.markedAsJunk) {
			    			castingActivity.markedAsJunk = true;
			    		}
			    		else {
			    			this.removeActivity(playerName);
			    		}
			    	}
			    	else {
			    		this.removeActivity(playerName);
			    	}
			    }
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		}
	}
}