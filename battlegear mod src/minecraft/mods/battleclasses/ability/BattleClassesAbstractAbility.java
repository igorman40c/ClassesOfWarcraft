package mods.battleclasses.ability;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.client.IDescriptionProvider;
import mods.battleclasses.core.BattleClassesPlayerAttributes;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.gui.BattleClassesGuiHelper;

public abstract class BattleClassesAbstractAbility implements IDescriptionProvider  {

	/**
	 * Stores all the active and passive abilities those can be displayed on the UI. (For example in a tooltip).
	 * Key : abilityID
	 * Value : Ability Object 
	 */
	public static HashMap<String, BattleClassesAbstractAbility> registredAbilities = new HashMap<String, BattleClassesAbstractAbility>();
	public static void registerAbility(BattleClassesAbstractAbility ability) {
		registredAbilities.put(ability.getAbilityID(), ability);
	}
	public static BattleClassesAbstractAbility getRegisteredAbilityByID(String id) {
		return registredAbilities.get(id);
	}
	
	public static BattleClassesAbstractAbilityActive getRegisteredActiveAbilityByID(String id) {
		BattleClassesAbstractAbility ability = getRegisteredAbilityByID(id);
		if(ability instanceof BattleClassesAbstractAbilityActive) {
			return (BattleClassesAbstractAbilityActive)ability;
		}
		BattleClassesUtils.ErrorLog("Couldn't find active ability with ID: " + id);
		return null;
	}
	
	public String getAbilityID() {
		return this.getUnlocalizedName();
	}
	
	
	private int abilityHashCode = 0;
	public int getAbilityHashCode() {
		return this.abilityHashCode;
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
	
	protected String getUnlocalizedName() {
		return this.unlocalizedName;
	}
	
	protected String getUnlocalizedID() {
		return getUnlocalizedPrefix() + this.unlocalizedName;
	}
	
	protected String getUnlocalizedDisplayName() {
		return getUnlocalizedID() + ".name";
	}
	
	protected String getUnlocalizedDescription() {
		return getUnlocalizedID()  + ".description";
	}
	
	protected String getUnlocalizedIconName() {
		return getUnlocalizedID()  + ".icon";
	}
	
	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedDisplayName());
	}
	
	public String getTranslatedDescription() {
		return StatCollector.translateToLocal(this.getUnlocalizedDescription());
	}
	
	public BattleClassesAbstractAbility() {
		//Init
		this.abilityHashCode = this.getAbilityID().hashCode();
	}
	
	//----------------------------------------------------------------------------------
	//							SECTION - Parent references
	//----------------------------------------------------------------------------------
	
	protected BattleClassesPlayerHooks playerHooks;
	protected BattleClassesPlayerAttributes playerAttributes;
	
	public void setContextReferences(BattleClassesPlayerHooks playerHooks, BattleClassesPlayerAttributes playerAttributes) {
		this.setPlayerAttributes(playerAttributes);
		this.setPlayerHooks(playerHooks);
	}
	
	protected void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
	}
	
	protected void setPlayerAttributes(BattleClassesPlayerAttributes parPlayerAttributes) {
		this.playerAttributes = parPlayerAttributes;
	}
	
	public void onLearn() {
		
	}
	
	public void onUnLearn() {
		
	}
	
	public BattleClassesPlayerHooks getPlayerHooks() throws NullPointerException {
		if(playerHooks == null) {
			throw(new NullPointerException("Ability requires PlayerHooks reference to be set"));
		}
		return this.playerHooks;
	}
	
	public BattleClassesPlayerAttributes getPlayerAttributes() throws NullPointerException {
		if(playerAttributes == null) {
			throw(new NullPointerException("Ability requires PlayerAttributes reference to be set"));
		}
		return this.playerAttributes;
	}
	
	/**
	 * Helper method to get the owner of this ability
	 * @return
	 */
	public EntityPlayer getOwnerPlayer() {
		return this.getPlayerHooks().getOwnerPlayer();
	}
	
	//----------------------------------------------------------------------------------
	//							SECTION - Icon
	//----------------------------------------------------------------------------------
	
	protected IIcon abilityIcon;
	public ResourceLocation abilityIconResourceLocation;
			
    @SideOnly(Side.CLIENT)
    public boolean hasItemIcon() {
    	return false;
    }
    
    @SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		return null;
	}
    
    @SideOnly(Side.CLIENT)    
    public ResourceLocation getIconResourceLocation() {
    	return BattleClassesGuiHelper.getResourceLocationOfTexture("textures/spells/icons/", this.getUnlocalizedIconName() + ".png");
    }
				
}
