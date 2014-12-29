package mods.battleclasses.core;

import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.battleclasses.ability.BattleClassesWeaponSkill;
import mods.battleclasses.enumhelper.EnumBattleClassesWieldAccess;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.weapons.IBackStabbable;
import mods.battlegear2.api.weapons.IHitTimeModifier;
import mods.battlegear2.api.weapons.IPenetrateWeapon;
import mods.battlegear2.api.weapons.IPotionEffect;
import mods.battlegear2.api.weapons.ISpecialEffect;

public class BattleClassesWeaponHitHandler {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	EnumSet<EnumBattleClassesWieldAccess> accessSet;
	
	public static final int WEAPONSKILL_COOLDOWN_HASHCODE_MAINHAND =  1398;
	public static final int WEAPONSKILL_COOLDOWN_HASHCODE_OFFHAND = 1397;
	public BattleClassesWeaponSkill mainHand;
	public BattleClassesWeaponSkill offHand;
	
	public BattleClassesWeaponHitHandler(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		mainHand = new BattleClassesWeaponSkill(WEAPONSKILL_COOLDOWN_HASHCODE_MAINHAND, parPlayerHooks, true);
		offHand = new BattleClassesWeaponSkill(WEAPONSKILL_COOLDOWN_HASHCODE_OFFHAND, parPlayerHooks, false);
	}
	
	public void initAccessSet() {
		accessSet.clear();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttack(LivingAttackEvent event){
		event.setCanceled(true);
    }
	
}
