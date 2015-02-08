package mods.battleclasses.ability;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.core.BattleClassesPlayerHooks;

public abstract class BattleClassesAbstractAbility {

	protected int abilityID;
	protected BattleClassesPlayerHooks playerHooks;
	protected String name = "";
	
	public int getAbilityID() {
		return abilityID;
	}
	
	public BattleClassesAbstractAbility setName(String parName) {
		this.name = parName;
		return this;
	}
	
	public static final String UNLOCALIZED_PREFIX = "ability.";
	public String getUnlocalizedPrefix() {
		return "ability.";
	}
	
	public String getUnlocalizedID() {
		return getUnlocalizedPrefix() + this.name;
	}
	
	public String getUnlocalizedName() {
		return getUnlocalizedPrefix() + this.name + ".name";
	}
	
	public String getUnlocalizedDescription() {
		return getUnlocalizedPrefix() + this.name + ".description";
	}
	
	public String getUnlocalizedIconName() {
		return getUnlocalizedPrefix() + this.name + ".icon";
	}
	
	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName());
	}
	
	public String getTranslatedDescription() {
		return StatCollector.translateToLocal(this.getUnlocalizedDescription());
	}
	
	public BattleClassesAbstractAbility(int parAbilityID) {
		this.abilityID = parAbilityID;
	}
	
	public void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
	}
	
	public void onLearn() {
		
	}
	
	public void onUnLearn() {
		
	}
	
	public BattleClassesPlayerHooks getPlayerHooks() throws NullPointerException {
		if(playerHooks == null) {
			throw(new NullPointerException("Ability requires playerHooks to be set"));
		}
		return this.playerHooks;
	}
			
	@SideOnly(Side.CLIENT)
	public String getAbilityIconName() {
		return "unknown";
	}
	
}
