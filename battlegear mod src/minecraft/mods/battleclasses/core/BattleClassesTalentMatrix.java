package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.active.BattleClassesAbilityTestCasted;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.talent.BattleClassesAbstractTalent;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonTalentNode;
import mods.battleclasses.packet.BattleClassesPacketTalentPointSync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class BattleClassesTalentMatrix {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public BattleClassesTalentMatrix(BattleClassesPlayerHooks parPlayerHooks) {
		playerHooks = parPlayerHooks;
	}
	
	public List<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
	public HashMap<String, BattleClassesAbstractTalent> talentHashMap = new HashMap<String, BattleClassesAbstractTalent>();

	public static final int MAXIMAL_TALENT_POINTS = 3;
	protected int currentTalentPoints = 0;
	protected int unspentTalentPoints = currentTalentPoints;
	
	public void initWithTalentTrees(List<BattleClassesTalentTree> parTalentTrees) {
		//Setting reverse references on trees and talentAbilities
		talentTrees = parTalentTrees;
		for(BattleClassesTalentTree talentTree : talentTrees) {
			talentTree.setOwnerTalentMatrix(this);
		}
		
		//Filling up talentHashMap
		talentHashMap.clear();
		for(BattleClassesTalentTree talentTree : this.talentTrees) {
        	for(BattleClassesAbstractTalent talentAbility : talentTree.talentList ) {
        		this.talentHashMap.put(talentAbility.getAbilityID(), talentAbility);
        	}
        }
	}
	
	public static final float TALENT_CHANGE_COOLDOWN_DURATION = 3.0F;
	
	public void onTalentsChanged() {
		this.playerHooks.playerClass.getCooldownClock().startCooldown(TALENT_CHANGE_COOLDOWN_DURATION, false, EnumBattleClassesCooldownType.CooldownType_TALENT_CHANGE);
		this.playerHooks.onAttributeSourcesChanged();
	}
	
	public BattleClassesPlayerHooks getPlayerHooks() {
		return this.playerHooks;
	}
	
	
	public void setUnspentTalentPoints(int n) {
		if(n <= currentTalentPoints) {
			unspentTalentPoints = n;
		}
	}
	
	public int getUnspentTalentPoints() {
		return unspentTalentPoints;
	}
	
	public boolean hasUnspentPointsToSpend() {
		return unspentTalentPoints > 0;
	}
	
	public boolean hasPointsSpentAlready() {
		return unspentTalentPoints < currentTalentPoints;
	}
	
	public void applyPointsOnTrees(int tree0, int tree1, int tree2) {
		this.talentTrees.get(0).spendTalentPoints(tree0);
		this.talentTrees.get(1).spendTalentPoints(tree1);
		this.talentTrees.get(2).spendTalentPoints(tree2);
	}
	
	public void applyPointsOnTrees(int[] pointsOnTrees) {
		for(int i = 0; i < pointsOnTrees.length; ++i) {
			this.talentTrees.get(i).spendTalentPoints(pointsOnTrees[i]);
        }
	}
	
	public int[] getPointsOnTrees() {
		int[] points = new int[this.talentTrees.size()];
		int i = 0;
		for(BattleClassesTalentTree talentTree : this.talentTrees) {
			points[i] = talentTree.getPointsOnTree();
        	++i;
        }
		return points;
	}
	
	public int getSpentTalentPoints() {
		int n = 0;
		for(BattleClassesTalentTree talentTree : this.talentTrees) {
			n += talentTree.getPointsOnTree();
        }
		return n;
	}
	
	public void learnFullTreeAtIndex(int index) {
		BattleClassesUtils.Log("Trying to learn full talent tree at index: " + index, LogType.CORE);
		this.resetTalentPoints();
		BattleClassesTalentTree talentTree = this.talentTrees.get(index);
		for(BattleClassesAbstractTalent talentAbility : talentTree.talentList ) {
    		while(!talentAbility.isAlreadyLearned() && unspentTalentPoints != 0) {
    			this.learnTalent(talentAbility);
    		}
    	}
	}
	
	public void resetTalentPoints() {
		for(BattleClassesTalentTree talentTree : this.talentTrees) {
        	for(BattleClassesAbstractTalent talentAbility : talentTree.talentList ) {
        		talentAbility.resetState();
        	}
        }
		unspentTalentPoints = currentTalentPoints;
	}
	
	public void learnTalent(BattleClassesAbstractTalent talentAbility) {
		if(talentAbility.isAvailableToLearn()) {
    		talentAbility.incrementState();
    		--unspentTalentPoints;
    	}
	}
	
	//Gaining talent points
	
	protected static final int experienceLevelCostPerTalentPoint = 10;
	
	public int getCurrentTalentPoints() {
		return this.currentTalentPoints;
	}
	
	public int getNextTalentPoints() {
		return this.getCurrentTalentPoints() + 1;
	}
	
	public void setCurrentTalentPoints(int points) {
		this.currentTalentPoints = points;
	}
	
	public int getGainTalentPointExperienceCost() {
		int experienceLevelCost = this.getNextTalentPoints() * experienceLevelCostPerTalentPoint;
		return experienceLevelCost;
	}
	
	public boolean hasExperienceToGainTalentPoint(EntityPlayer entityPlayer) {
		return entityPlayer.experienceLevel >= this.getGainTalentPointExperienceCost();
	}
	
	public boolean canGainAdditionalTalentPoints() {
		return this.getCurrentTalentPoints() < MAXIMAL_TALENT_POINTS;
	}
	
	public void gainTalentPoint(EntityPlayer entityPlayer) {
		if(this.canGainAdditionalTalentPoints() && this.hasExperienceToGainTalentPoint(entityPlayer)) {
			int nextTalentPoints = this.getNextTalentPoints();
			entityPlayer.addExperienceLevel((-1) * getGainTalentPointExperienceCost());
			this.setCurrentTalentPoints(nextTalentPoints);
			if(!entityPlayer.worldObj.isRemote) {
				entityPlayer.worldObj.playSoundAtEntity(entityPlayer, BattleClassesMod.MODID + ":" + "gui.talent", 1F, 1F);				
			}
			if(entityPlayer instanceof EntityPlayerMP) {
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entityPlayer;
				FMLProxyPacket p = new BattleClassesPacketTalentPointSync(this.getCurrentTalentPoints()).generatePacket();
				BattleClassesMod.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
			}
		}
	}
	
	//Helper
	public BattleClassesTalentTree getTreeAtIndex(int index) {
		if(this.talentTrees.size() > 0 && index < this.talentTrees.size()) {
			return this.talentTrees.get(index);
		}
		return null;
	}
	
}
