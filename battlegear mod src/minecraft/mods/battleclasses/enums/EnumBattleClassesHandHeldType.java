package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;

public enum EnumBattleClassesHandHeldType {
	ONE_HANDED {
		@Override
		public boolean isOffhandEligible() {
			return true;
		}
	},
	MAIN_HANDED {
		@Override
		public boolean isOffhandEligible() {
			// TODO Auto-generated method stub
			return false;
		}
	},
	TWO_HANDED {
		@Override
		public boolean isOffhandEligible() {
			// TODO Auto-generated method stub
			return false;
		}
	},
	OFF_HANDED {
		@Override
		public boolean isOffhandEligible() {
			// TODO Auto-generated method stub
			return true;
		}
	}
	;
	
	public abstract boolean isOffhandEligible();
	
	protected String unlocalizedPrefix = "bchandheld.";
	public String getTranslatedName() {
		return StatCollector.translateToLocal(unlocalizedPrefix + this.toString().toLowerCase());
	}
}
