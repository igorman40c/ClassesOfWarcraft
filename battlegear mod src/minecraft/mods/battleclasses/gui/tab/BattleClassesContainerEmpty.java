package mods.battleclasses.gui.tab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.gui.ArmorSlot;
import mods.battlegear2.gui.ContainerLocalPlayer;
import mods.battlegear2.gui.WeaponSlot;

public class BattleClassesContainerEmpty extends ContainerLocalPlayer {

	public BattleClassesContainerEmpty(InventoryPlayer inventoryPlayer, boolean local, EntityPlayer player) {
        super(local, player);
    }
	
}
