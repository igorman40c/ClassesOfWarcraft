package mods.battleclasses.enums;

public enum EnumBattleClassesAbilityDirectTargetRequirement {
	REQUIRED {
		@Override
		public boolean requiresRayTracingForTarget() {
			return false;
		}
	},
	OPTIONAL {
		@Override
		public boolean requiresRayTracingForTarget() {
			return true;
		}
	},
	NEEDLESS {
		@Override
		public boolean requiresRayTracingForTarget() {
			return true;
		}
	};
	
	public abstract boolean requiresRayTracingForTarget();
}
