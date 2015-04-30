package mods.battleclasses.attributes;

import java.util.List;

public interface ICWAttributeModifierOwner {
	public List<ICWAttributeModifier> getAttributeModifiers();
	public void setAttributeModifiers(List<ICWAttributeModifier> attributeModifiers);
}
