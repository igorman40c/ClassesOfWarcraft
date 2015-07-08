package mods.battleclasses.client;

import java.util.HashMap;

public class BattleClassesClientAbilityActivityRegistry {
	
	public static class AbilityActivity {
		public String abilityID;
		public int duration;
		
		AbilityActivity(String abilityID, int duration) {
			this.abilityID = abilityID;
	    	this.duration = duration;
		}
		
		public void closeActivity() {
			//Stub method, stop casting sound here, etc....
		}
	}
	
	/**
	 * Stores the active abilities being casted at the moment.
	 * Key - Player Name
	 * Value - AbilityActivity < String abilityID & int duration >
	 */
	public HashMap<String,AbilityActivity> activityMap = new HashMap<String,AbilityActivity>();
	
	public void addActiveAbility(String playerName, String abilityID, int duration) {
		 
		this.activityMap.put(playerName, new AbilityActivity(abilityID, duration));
		 
	}
	 
	public void removeActiveAbility(String playerName) {
		AbilityActivity activity = this.activityMap.get(playerName);
		if(activity != null) {
			activity.closeActivity();
			this.activityMap.remove(playerName);
		}
	}
	
}
