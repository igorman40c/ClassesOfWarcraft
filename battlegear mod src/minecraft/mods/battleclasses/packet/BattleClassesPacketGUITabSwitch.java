package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesMod;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesPacketGuiTabSwitch  extends AbstractMBPacket {

    public static final String packetName = "BC|GUITabSwitch";

    public BattleClassesPacketGuiTabSwitch(int equipid) {
        this.equipid = equipid;
    }

	public BattleClassesPacketGuiTabSwitch() {
	}

	private int equipid;
	
    @Override
    public void process(ByteBuf in, EntityPlayer player) {
		equipid = -1;
		equipid = in.readInt();
        if(equipid != -1){
            player.openGui(BattleClassesMod.INSTANCE, equipid, player.worldObj, 0, 0, 0);
        }
    }

	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		out.writeInt(equipid);
	}
	
}
