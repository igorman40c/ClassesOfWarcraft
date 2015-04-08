package mods.battleclasses.potion;

import java.util.ArrayList;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffectInstantValue;
import mods.battleclasses.core.BattleClassesAttributes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BattleClassesPotionEffectValueOverTime extends PotionEffect {
	
	//Origin references
	protected BattleClassesAbstractAbilityActive parentAbility;
	protected EntityLivingBase effectOwner;
	
	//Variables for the effect
	protected BattleClassesAttributes attributesForParentAbility;
	protected float critChance;
	protected int tickCount;
	protected BattleClassesAbstractAbilityEffectInstantValue effectInstantValue;
	
	//Helpers
	protected int duration;
	protected int numberOfTicksPerAffect;
	protected float partialMultiplier;
	
	
    public BattleClassesPotionEffectValueOverTime(Potion potion, int duration, int amplifier,
    		BattleClassesAttributes attributes, float critChance, int tickCount,
    		BattleClassesAbstractAbilityEffectInstantValue effectInstantValue,
    		BattleClassesAbstractAbilityActive ability, EntityLivingBase effectOwner )
    {
    	super(potion.getId(), duration, amplifier, false);
    	
    	this.attributesForParentAbility = attributes;
    	this.critChance = critChance;
    	this.tickCount = tickCount;
    	this.effectInstantValue = effectInstantValue;
    	this.parentAbility = ability;
    	this.effectOwner = effectOwner;
    	
    	this.duration = duration;
    	this.numberOfTicksPerAffect = duration/this.tickCount;
    	this.partialMultiplier = 1.0F / tickCount;
    }
    
    protected boolean isReady() {
    	return this.duration % numberOfTicksPerAffect == 1;
    }
    
    @Override
    public boolean onUpdate(EntityLivingBase target)
    {
        if (this.getDuration() > 0)
        {
            if (this.isReady())
            {
                this.performEffect(target);
            }

            this.deincrementDuration();
        }

        return this.duration > 0;
    }
    
    @Override
    public void performEffect(EntityLivingBase target)
    {
        if (this.duration > 0)
        {
            this.effectInstantValue.performValueEffect(attributesForParentAbility, this.critChance, this.partialMultiplier, this.effectOwner, target);
        }
    }

    protected int deincrementDuration()
    {
        return --this.duration;
    }
}
