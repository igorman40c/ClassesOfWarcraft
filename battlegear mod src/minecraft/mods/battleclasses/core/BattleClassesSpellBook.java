package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbilityTestCasted;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.passive.BattleClassesAbstractAbilityPassive;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.BattleClassesGuiHUDOverlay;
import mods.battleclasses.items.BattleClassesItemWeapon;
import mods.battleclasses.items.IBattleClassesWeapon;
import mods.battleclasses.packet.BattleClassesPacketChosenAbilityIDSync;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.core.IBattlePlayer;

public class BattleClassesSpellBook {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public static final float GLOBAL_COOLDOWN_DURATION = 1.0F;
	
	public ArrayList<BattleClassesAbstractAbilityActive> actionbarAbilities = new ArrayList<BattleClassesAbstractAbilityActive>();
	public LinkedHashMap<String, BattleClassesAbstractAbilityActive> activeAbilities = new LinkedHashMap<String, BattleClassesAbstractAbilityActive>();
	public LinkedHashMap<String, BattleClassesAbstractAbilityPassive> passiveAbilities = new LinkedHashMap<String, BattleClassesAbstractAbilityPassive>();
	
	protected int chosenAbilityIndex = 0;
	protected String chosenAbilityID = "";
	
	public BattleClassesSpellBook(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		//protoSpell = new BattleClassesAbilityTestCasted(100);
	}
	
	//public BattleClassesAbstractAbilityActive protoSpell;
	
	public BattleClassesAbstractAbilityActive getChosenAbility() {
		//TODO
		return activeAbilities.get(chosenAbilityID);
	}
	
	public void UseStartOnChosenAbility(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!this.isAvailable(itemStack, entityPlayer) || this.getChosenAbility() == null) {
			return;
		}
		this.getChosenAbility().startUse(itemStack, world, entityPlayer);
	}
	
	public void CastTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(!this.isAvailable(itemStack, entityPlayer) || this.getChosenAbility() == null) {
			return;
		}
		this.getChosenAbility().tickUse(itemStack, entityPlayer, tickCount);
	}
	
	public void CastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(!this.isAvailable(itemStack, entityPlayer) || this.getChosenAbility() == null) {
			return;
		}
		this.getChosenAbility().releaseUse(itemStack, entityPlayer, tickCount);
	}
	
	public boolean isAvailable(ItemStack itemStack, EntityPlayer entityPlayer) {
		boolean battleMode = ((IBattlePlayer) entityPlayer).isBattlemode();
		if(!battleMode) {
			BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_BATTLEMODE_REQUIRED);
			return false;
		}
		boolean hasClass = playerHooks.playerClass.getPlayerClass() != EnumBattleClassesPlayerClass.NONE;
		if(!hasClass) {
			BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_CLASS_REQUIRED);
			return false;
		}
		boolean cooldownFreeClass = !playerHooks.playerClass.getCooldownClock().isOnCooldown();
		if(!cooldownFreeClass) {
			BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_ON_CLASS_COOLDOWN);
			return false;
		}
		BattleClassesAbstractAbilityActive chosenAbility = getChosenAbility();
		if(chosenAbility != null) {
			boolean combatDisabled = !this.getChosenAbility().isIgnoringSilence() && BattleClassesCombatHooks.isPlayerSilenced(entityPlayer);
			if(combatDisabled) {
				BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_COMBAT_DISABLED);
				return false;
			}
			boolean silenced = !this.getChosenAbility().isIgnoringSilence() && BattleClassesCombatHooks.isPlayerSilenced(entityPlayer);
			if(silenced) {
				BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_SILENCED);
				return false;
			}
		}
		else {
			return false;
		}
		
		return true;
	}
	
	public void learnAbility(BattleClassesAbstractAbility ability) {
		ability.setContextReferences(this.playerHooks, this.playerHooks.playerAttributes);
		if(ability instanceof BattleClassesAbstractAbilityActive) {
			BattleClassesAbstractAbilityActive activeAbility = (BattleClassesAbstractAbilityActive) ability;
			if(!this.activeAbilities.containsValue(activeAbility)) {
				this.activeAbilities.put(activeAbility.getAbilityID(), activeAbility);
				this.actionbarAbilities.add(activeAbility);
				activeAbility.onLearn();
			}
		}
		else if(ability instanceof BattleClassesAbstractAbilityPassive) {
			BattleClassesAbstractAbilityPassive passiveAbility = (BattleClassesAbstractAbilityPassive) ability;
			if(hasAbility(passiveAbility)) {
				this.passiveAbilities.put(passiveAbility.getAbilityID(), passiveAbility);
			}
		}
	}
	
	public void unLearnAbility(BattleClassesAbstractAbility ability) {
		if(ability instanceof BattleClassesAbstractAbilityActive) {
			BattleClassesAbstractAbilityActive activeAbility = (BattleClassesAbstractAbilityActive) ability;
			if(hasAbility(activeAbility)) {
				BattleClassesUtils.Log("Unlearning ability (ID: " + activeAbility.getAbilityID() + ")", LogType.ABILITY);
				if(this.actionbarAbilities.contains(activeAbility)) {
					this.actionbarAbilities.remove(activeAbility);
				}
				this.activeAbilities.remove(activeAbility.getAbilityID());
				activeAbility.onUnLearn();
			}
		}
		else if(ability instanceof BattleClassesAbstractAbilityPassive) {
			BattleClassesAbstractAbilityPassive passiveAbility = (BattleClassesAbstractAbilityPassive) ability;
			if(hasAbility(passiveAbility)) {
				BattleClassesUtils.Log("Unlearning ability (ID: " + passiveAbility.getAbilityID() + ")", LogType.ABILITY);
				this.passiveAbilities.remove(passiveAbility.getAbilityID());
			}
		}
		
		if(this.getChosenAbility() == null) {
			this.setChosenAbilityIndex(0);
		}
	}
	
	public boolean hasAbility(BattleClassesAbstractAbility ability) {
		if(ability instanceof BattleClassesAbstractAbilityActive) {
			BattleClassesAbstractAbilityActive activeAbility = (BattleClassesAbstractAbilityActive) ability;
			return this.activeAbilities.containsKey(activeAbility.getAbilityID());
		}
		else if(ability instanceof BattleClassesAbstractAbilityPassive) {
			BattleClassesAbstractAbilityPassive passiveAbility = (BattleClassesAbstractAbilityPassive) ability;
			return this.passiveAbilities.containsKey(passiveAbility.getAbilityID());
		}
		return false;
	}
	
	public boolean hasAbilityByID(String abilityID) {
		boolean hasActiveAbility = this.activeAbilities.containsKey(abilityID);
		boolean hasPassiveAbility = this.passiveAbilities.containsKey(abilityID);
		return hasActiveAbility || hasPassiveAbility;
	}
	
	public void clearSpellBook() {
		for(BattleClassesAbstractAbilityActive ability : getActiveAbilitiesInArray()) {
			this.unLearnAbility(ability);
		}
		this.activeAbilities.clear();
		this.actionbarAbilities.clear();
	}
	
	public void initWithAbilities(List<BattleClassesAbstractAbilityActive> parAbilities) {
		clearSpellBook();
		for(BattleClassesAbstractAbilityActive ability : parAbilities){
		    this.learnAbility(ability);
		}
		updateChosenAbilityID();
	}
		
	@SideOnly(Side.CLIENT)
	public int getChosenAbilityIndex() {
		return this.chosenAbilityIndex;
	}
	
	@SideOnly(Side.CLIENT)
	public void setChosenAbilityIndex(int i) {
		if(i >= 0 && i < this.getActionbarAbilities().size()) {
			this.chosenAbilityIndex = i;
		}
		else if(this.getActionbarAbilities().size() > 0 && i >= this.getActionbarAbilities().size()) {
			this.chosenAbilityIndex = this.getActionbarAbilities().size()-1;
		}
		updateChosenAbilityID();
	}
	
	@SideOnly(Side.CLIENT)
	public void incrementChosenAbilityIndex() {
		++this.chosenAbilityIndex;
		if(this.chosenAbilityIndex >= this.getActionbarAbilities().size()) {
			this.chosenAbilityIndex = 0;
		}
		updateChosenAbilityID();
	}
	
	@SideOnly(Side.CLIENT)
	public void decrementChosenAbilityIndex() {
		--this.chosenAbilityIndex;
		if(this.chosenAbilityIndex < 0) {
			int n = this.getActionbarAbilities().size();
			this.chosenAbilityIndex = (n > 0) ?  n - 1 : 0;
		}
		updateChosenAbilityID();
	}
	
	@SideOnly(Side.CLIENT)
	public void updateChosenAbilityID() {
		if(chosenAbilityIndex < getActionbarAbilities().size()) {
			setChosenAbilityID(getActionbarAbilities().get(chosenAbilityIndex).getAbilityID());
			cancelCasting();
			FMLProxyPacket p = new BattleClassesPacketChosenAbilityIDSync(playerHooks.getOwnerPlayer(), this.chosenAbilityID).generatePacket();
			//Should be sidesafe
			BattleClassesMod.packetHandler.sendPacketToServerWithSideCheck(p);
			
			BattleClassesGuiHUDOverlay.displayChosenAbilityName(this.getChosenAbility().getTranslatedName());
		}
	}
	
	public void setChosenAbilityID(String parID) {
		this.chosenAbilityID = parID;
		cancelCasting();
	}
	
	public void setGlobalCooldownForCasting(float castingDuration) {
		float globalCooldownDuration = (GLOBAL_COOLDOWN_DURATION < castingDuration) ? GLOBAL_COOLDOWN_DURATION : castingDuration; 
		for(BattleClassesAbstractAbilityActive ability : getActiveAbilitiesInArray()) {
			if(!ability.ignoresGlobalCooldown && !ability.getCooldownClock().isOnCooldown()) {
				ability.getCooldownClock().startCooldown(globalCooldownDuration, false, EnumBattleClassesCooldownType.CooldownType_GLOBAL);
			}
		}
	}
		
	public void setGlobalCooldown() {
		this.setGlobalCooldownForCasting(GLOBAL_COOLDOWN_DURATION);
	}
	
	public void cancelGlobalCooldown() {
		for(BattleClassesAbstractAbilityActive ability : getActiveAbilitiesInArray()) {
			if(!ability.ignoresGlobalCooldown && ability.getCooldownClock().isOnCooldown() && ability.getCooldownClock().getLastUsedType() == EnumBattleClassesCooldownType.CooldownType_GLOBAL) {
				ability.getCooldownClock().cancelCooldown();
			}
		}
	}
	
	
    /**
     * Helper method to collect abilities should be drawn on the actionbar
     * @return
     */
	@SideOnly(Side.CLIENT)
    public ArrayList<BattleClassesAbstractAbilityActive> getActionbarAbilities() {
    	return this.actionbarAbilities;
    }
	
	@SideOnly(Side.CLIENT)
	public boolean isAbilityOnActionbar(BattleClassesAbstractAbilityActive ability) {
		return this.actionbarAbilities.contains(ability);
	}

	
	@SideOnly(Side.CLIENT)
	public void repleaceAbilitiesOnActionbar(BattleClassesAbstractAbilityActive ability1, BattleClassesAbstractAbilityActive ability2) {
		if(this.actionbarAbilities.contains(ability1) && this.actionbarAbilities.contains(ability2)) {
			int index1 = this.actionbarAbilities.indexOf(ability1);
			int index2 = this.actionbarAbilities.indexOf(ability2);
			this.actionbarAbilities.set(index2, ability1);
			this.actionbarAbilities.set(index1, ability2);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void removeAbilityFromActionbar(BattleClassesAbstractAbilityActive ability) {
		if(this.actionbarAbilities.contains(ability)) {
			this.actionbarAbilities.remove(ability);
			if(this.chosenAbilityIndex >= this.actionbarAbilities.size() && this.actionbarAbilities.size() > 0) {
				this.setChosenAbilityIndex(0);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void addAbilityToActionbar(BattleClassesAbstractAbilityActive ability) {
		if(!this.actionbarAbilities.contains(ability)) {
			this.actionbarAbilities.add(ability);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void insertAbilityToActionbarAtIndex(BattleClassesAbstractAbilityActive ability, int index) {
		if(!this.actionbarAbilities.contains(ability)) {
			this.actionbarAbilities.add(index, ability);
		}
	}
	
	public EnumAction getCurrentEnumAction() {
		BattleClassesAbstractAbilityActive chosenAbility = this.getChosenAbility();
		if(chosenAbility != null) {
			return chosenAbility.getEnumActionForCasting();
		}
		return EnumAction.none;
	}

	
	//Helper
    public BattleClassesAbstractAbilityActive getActiveAbilityByID(String id) {
    	return this.activeAbilities.get(id);
    }
    
    public BattleClassesAbstractAbilityPassive getPassiveAbilityByID(String id) {
    	return this.passiveAbilities.get(id);
    }
	
	public ArrayList<BattleClassesAbstractAbilityActive> getActiveAbilitiesInArray() {
		return new ArrayList<BattleClassesAbstractAbilityActive>(this.activeAbilities.values());
	}
	
	public ArrayList<BattleClassesAbstractAbilityPassive> getPassiveAbilitiesInArray() {
		return new ArrayList<BattleClassesAbstractAbilityPassive>(this.passiveAbilities.values());
	}
	
	public void cancelCasting() {
		this.playerHooks.ownerPlayer.clearItemInUse();
	}
	
	public boolean isCastingInProgress() {
		return ((IBattlePlayer)this.playerHooks.ownerPlayer).isBattlemode() && 
				(this.playerHooks.ownerPlayer.isUsingItem()) && 
				(this.playerHooks.ownerPlayer.getItemInUse().getItem() instanceof IBattleClassesWeapon) && 
				(this.getChosenAbility() != null);
	}
}
