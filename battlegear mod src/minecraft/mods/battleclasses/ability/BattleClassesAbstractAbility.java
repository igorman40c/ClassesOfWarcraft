package mods.battleclasses.ability;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.core.BattleClassesPlayerHooks;

public abstract class BattleClassesAbstractAbility {

	protected int abilityID;
	
	
	//Mandtory Helper References
	protected BattleClassesPlayerHooks playerHooks;
	
	public int getAbilityID() {
		return abilityID;
	}
	
	
	
	public static final String UNLOCALIZED_PREFIX_ABILITY = "ability.";
	public String getUnlocalizedPrefix() {
		return UNLOCALIZED_PREFIX_ABILITY;
	}
	
	protected String unlocalizedName = "";
	
	public BattleClassesAbstractAbility setUnlocalizedName(String parName) {
		this.unlocalizedName = parName;
		return this;
	}
	
	public String getUnlocalizedID() {
		return getUnlocalizedPrefix() + this.unlocalizedName;
	}
	
	public String getUnlocalizedDisplayName() {
		return getUnlocalizedID() + ".name";
	}
	
	public String getUnlocalizedDescription() {
		return getUnlocalizedID()  + ".description";
	}
	
	public String getUnlocalizedIconName() {
		return getUnlocalizedID()  + ".icon";
	}
	
	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedDisplayName());
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
