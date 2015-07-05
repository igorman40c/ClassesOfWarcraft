package mods.battleclasses.enums;

import java.util.EnumSet;
import java.util.List;

import mods.battleclasses.client.INameProvider;
import mods.battleclasses.client.ITooltipProvider;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public enum EnumBattleClassesPlayerClass implements ITooltipProvider, INameProvider {
	NONE {
		public EnumSet<EnumBattleClassesPlayerRole> getRoles() {
			return EnumSet.noneOf(EnumBattleClassesPlayerRole.class);
		}

		@Override
		public EnumBattleClassesArmorType getArmorType() {
			return EnumBattleClassesArmorType.CLOTH;
		}

		@Override
		public EnumChatFormatting getNameChatColor() {
			return EnumChatFormatting.WHITE;
		}
	},
	
	MAGE {
		@Override
		public EnumSet<EnumBattleClassesPlayerRole> getRoles() {
			return EnumSet.of(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER);
		}

		@Override
		public EnumBattleClassesArmorType getArmorType() {
			return EnumBattleClassesArmorType.CLOTH;
		}

		@Override
		public EnumChatFormatting getNameChatColor() {
			return EnumChatFormatting.AQUA;
		}
	},
	PRIEST {
		@Override
		public EnumSet<EnumBattleClassesPlayerRole> getRoles() {
			return EnumSet.of(EnumBattleClassesPlayerRole.HEALER, EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER);
		}

		@Override
		public EnumBattleClassesArmorType getArmorType() {
			return EnumBattleClassesArmorType.CLOTH;
		}

		@Override
		public EnumChatFormatting getNameChatColor() {
			return EnumChatFormatting.WHITE;
		}
	},
	WARLOCK {
		@Override
		public EnumSet<EnumBattleClassesPlayerRole> getRoles() {
			return EnumSet.of(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER);
		}

		@Override
		public EnumBattleClassesArmorType getArmorType() {
			return EnumBattleClassesArmorType.CLOTH;
		}

		@Override
		public EnumChatFormatting getNameChatColor() {
			return EnumChatFormatting.DARK_PURPLE;
		}
	},
	ROGUE {
		@Override
		public EnumSet<EnumBattleClassesPlayerRole> getRoles() {
			return EnumSet.of(EnumBattleClassesPlayerRole.DAMAGE_DEALER);
		}

		@Override
		public EnumBattleClassesArmorType getArmorType() {
			return EnumBattleClassesArmorType.LEATHER;
		}

		@Override
		public EnumChatFormatting getNameChatColor() {
			return EnumChatFormatting.YELLOW;
		}
	},
	HUNTER {
		@Override
		public EnumSet<EnumBattleClassesPlayerRole> getRoles() {
			return EnumSet.of(EnumBattleClassesPlayerRole.RANGED_DAMAGE_DEALER);
		}

		@Override
		public EnumBattleClassesArmorType getArmorType() {
			return EnumBattleClassesArmorType.MAIL;
		}

		@Override
		public EnumChatFormatting getNameChatColor() {
			return EnumChatFormatting.GREEN;
		}
	},
	PALADIN {
		@Override
		public EnumSet<EnumBattleClassesPlayerRole> getRoles() {
			return EnumSet.of(EnumBattleClassesPlayerRole.HEALER, EnumBattleClassesPlayerRole.TANK, EnumBattleClassesPlayerRole.DAMAGE_DEALER);
		}

		@Override
		public EnumBattleClassesArmorType getArmorType() {
			return EnumBattleClassesArmorType.PLATE;
		}

		@Override
		public EnumChatFormatting getNameChatColor() {
			return EnumChatFormatting.LIGHT_PURPLE;
		}
	},
	WARRIOR {
		@Override
		public EnumSet<EnumBattleClassesPlayerRole> getRoles() {
			return EnumSet.of(EnumBattleClassesPlayerRole.DAMAGE_DEALER, EnumBattleClassesPlayerRole.TANK);
		}

		@Override
		public EnumBattleClassesArmorType getArmorType() {
			return EnumBattleClassesArmorType.PLATE;
		}

		@Override
		public EnumChatFormatting getNameChatColor() {
			return EnumChatFormatting.DARK_RED; 
		}
	}
	;
	
	public ResourceLocation getIconResourceLocation() {
		return new ResourceLocation("battleclasses", "textures/sharedicons/classes/" + this.toString().toLowerCase() + ".png");
	}
		
	public boolean isEligibleForClassAccessSet(EnumSet<EnumBattleClassesPlayerClass> classAccessSet) {
		return classAccessSet.contains(NONE) || classAccessSet.contains(this); 
	}
	
	public abstract EnumSet<EnumBattleClassesPlayerRole> getRoles();
	
	public abstract EnumBattleClassesArmorType getArmorType();
	
	public abstract EnumChatFormatting getNameChatColor();
	
	//----------------------------------------------------------------------------------
	//							SECTION - Class descriptions
	//----------------------------------------------------------------------------------
	
	protected String getUnlocalizedName() {
		return "bcclass." + this.toString().toLowerCase();
	}
	
	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName());
	}
	
	protected String getUnlocalizedIntroduction() {
		return this.getUnlocalizedName() + ".introduction";
	}
	
	public String getTranslatedIntroduction() {
		return StatCollector.translateToLocal(this.getUnlocalizedIntroduction());
	}
	
	public String getTranslatedArmorInfo() {
		return BattleClassesGuiHelper.createListWithTitle(StatCollector.translateToLocal("bcclass.armorinfo"), EnumSet.of(this.getArmorType()));
	}
	
	public String getTranslatedRoleInfo() {
		String unlocalizedRoleString = (this.getRoles().size() > 1) ? "bcclass.rolesinfo" : "bcclass.roleinfo";
		return BattleClassesGuiHelper.createListWithTitle(StatCollector.translateToLocal(unlocalizedRoleString), this.getRoles());
	}

	@Override
	public List<String> getTooltipText() {
		List<String> hoveringText = BattleClassesGuiHelper.createHoveringText();
		BattleClassesGuiHelper.addTitle(hoveringText, this.getTranslatedName(), this.getNameChatColor());
		BattleClassesGuiHelper.addParagraph(hoveringText, this.getTranslatedIntroduction());
		BattleClassesGuiHelper.addEmptyParagraph(hoveringText);
		BattleClassesGuiHelper.addParagraphWithColor(hoveringText, this.getTranslatedRoleInfo(), EnumChatFormatting.GOLD);
		BattleClassesGuiHelper.addParagraphWithColor(hoveringText, this.getTranslatedArmorInfo(), EnumChatFormatting.GOLD);
		hoveringText = BattleClassesGuiHelper.formatHoveringTextWidth(hoveringText);
		return hoveringText;
	}
}
