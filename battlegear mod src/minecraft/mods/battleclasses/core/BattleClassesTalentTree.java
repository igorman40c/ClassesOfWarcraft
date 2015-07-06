package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import mods.battleclasses.ability.talent.BattleClassesAbstractTalent;
import mods.battleclasses.client.ITooltipProvider;
import mods.battleclasses.enums.EnumBattleClassesPlayerEnviroment;
import mods.battleclasses.enums.EnumBattleClassesPlayerRole;
import mods.battleclasses.gui.BattleClassesGuiHelper;

public class BattleClassesTalentTree implements ITooltipProvider {
	
	protected BattleClassesTalentMatrix parentTalentMatrix;
	
	public void setOwnerTalentMatrix(BattleClassesTalentMatrix parTalentMatrix) {
		this.parentTalentMatrix = parTalentMatrix;
		
		for(BattleClassesAbstractTalent talent : talentList) {
			talent.setParentTree(this);
			talent.setPlayerHooks(parTalentMatrix.playerHooks);
		}
	}
	
	public BattleClassesTalentTree() {
		
	}
	
	public BattleClassesTalentTree(String name, EnumBattleClassesPlayerRole role, EnumSet<EnumBattleClassesPlayerEnviroment> enviroments) {
		this.name = name;
		this.setRole(role);
		this.setEnviroments(enviroments);
	}
	
	public String name = "";
	
	public BattleClassesTalentTree setName(String parName) {
		name = parName;
		return this;
	}
	
	public BattleClassesTalentMatrix getParentTalentMatrix() {
		return this.parentTalentMatrix;
	}
	
	public ArrayList<BattleClassesAbstractTalent> talentList = new ArrayList<BattleClassesAbstractTalent>();
	
	/**
	 * Returns the index of the talent tree
	 * @return
	 */
	public int getIndexOfTree() {
		if(this.parentTalentMatrix != null) {
			return this.parentTalentMatrix.talentTrees.indexOf(this);
		}		
		return 0;
	}
	
	public int getPointsOnTree() {
		int n = 0;
		for(BattleClassesAbstractTalent talentAbility : this.talentList ) {
			n += talentAbility.getCurrentState();
    	}
		return n;
	}
	
	public void spendTalentPoints(int points) {
		for(BattleClassesAbstractTalent talentAbility : this.talentList ) {
    		while(!talentAbility.isAlreadyLearned() && points != 0) {
    			this.parentTalentMatrix.learnTalent(talentAbility);
    			points--;
    		}
    	}
	}
	
	/**
	 * Returns the resource path of background image of this talent tree 
	 * @return
	 */
	public String getResoureLocationString() {
		if(this.parentTalentMatrix != null) {
			int i = getIndexOfTree() + 1;
			return "textures/talents/backgrounds/talent_background_"
					+ (this.parentTalentMatrix.playerHooks.playerClass.getPlayerClass().toString()).toLowerCase()
					+ "_" + i + ".png";
		}
		return "";
	}
	
	//----------------------------------------------------------------------------------
	//							SECTION - Tooltip
	//----------------------------------------------------------------------------------
	
	public EnumBattleClassesPlayerRole role = EnumBattleClassesPlayerRole.UNKNOWN;
	
	public EnumBattleClassesPlayerRole getRole() {
		return role;
	}

	public BattleClassesTalentTree setRole(EnumBattleClassesPlayerRole role) {
		this.role = role;
		return this;
	}

	public EnumSet<EnumBattleClassesPlayerEnviroment> enviroments = EnumSet.noneOf(EnumBattleClassesPlayerEnviroment.class);
	
	public EnumSet<EnumBattleClassesPlayerEnviroment> getEnviroments() {
		return enviroments;
	}

	public BattleClassesTalentTree setEnviroments(EnumSet<EnumBattleClassesPlayerEnviroment> enviroments) {
		this.enviroments = enviroments;
		return this;
	}

	protected String getUnlocalizedPrefix() {
		return "bctalenttree";
	}
	
	protected String getUnlocalizedName() {
		return this.getUnlocalizedPrefix() + "." + this.name + ".name";
	}
	
	protected String getUnlocalizedDescription() {
		return this.getUnlocalizedPrefix() + "." + this.name + ".description";
	}
	
	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName());
	}
	
	public String getTranslatedDescription() {
		return StatCollector.translateToLocal(this.getUnlocalizedDescription());
	}
	
	public String getTranslatedRoleInfo() {
		return BattleClassesGuiHelper.createListWithTitle(StatCollector.translateToLocal("bcclass.roleinfo"), EnumSet.of(this.role));
	}
	
	public String getTranslatedEnviromentInfo() {
		return BattleClassesGuiHelper.createListWithTitle(StatCollector.translateToLocal("bcenviroment"), this.getEnviroments());
	}

	@Override
	public List<String> getTooltipText() {
		List<String> hoveringText = BattleClassesGuiHelper.createHoveringText();
		BattleClassesGuiHelper.addTitle(hoveringText, this.getTranslatedName());
		BattleClassesGuiHelper.addParagraph(hoveringText, this.getTranslatedDescription());
		BattleClassesGuiHelper.addEmptyParagraph(hoveringText);
		BattleClassesGuiHelper.addParagraphWithColor(hoveringText, this.getTranslatedRoleInfo(), EnumChatFormatting.GOLD);
		BattleClassesGuiHelper.addParagraphWithColor(hoveringText, this.getTranslatedEnviromentInfo(), EnumChatFormatting.GOLD);
		hoveringText = BattleClassesGuiHelper.formatHoveringTextWidth(hoveringText);
		return hoveringText;
	}

}
