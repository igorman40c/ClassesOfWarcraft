package mods.battleclasses;

import java.util.Random;

import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesTalentMatrix;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battlegear2.Battlegear;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class BattleClassesCommand extends CommandBase {
	
	public static void registerCommands() {
		 MinecraftServer server = MinecraftServer.getServer();
	     ICommandManager command = server.getCommandManager();
	     ServerCommandManager manager = (ServerCommandManager) command;
	     manager.registerCommand(new BattleClassesCommand());
	     BattleClassesUtils.Log("Commands registered!", LogType.COMMAND);
	}
	
	@Override
	public String getCommandName() {
		return "battleclasses";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		String helpDescription = new String("Battle Classes commands: /battleclasses ");
		helpDescription = new String(helpDescription
							+ " - " + new String("version")
							+ " - " + new String("classinfo"));
		return helpDescription;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if(var2.length < 1) {
			return;
		}
		if(var2[0].equals("version") ) {
			String message = new String("Running " + BattleClassesMod.MODID + " version: " + BattleClassesMod.VERSION);
			if(var1 instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) var1;
				player.addChatMessage(new ChatComponentText(message));
			}
			else {
				BattleClassesUtils.Log("Error! CommandSender isn't player", LogType.COMMAND);
			}
			System.out.println("Potion types capacity: " + Potion.potionTypes.length);
			return;
		}
		
		if(var2[0].equals("classinfo") ) {
			if(var1 instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) var1;
				String message = new String("Player Class: " + BattleClassesUtils.getPlayerClassEnum(player));
				BattleClassesTalentMatrix talentMatrix = BattleClassesUtils.getPlayerTalentMatrix(player);
				if(talentMatrix != null) {
					message += " Talents: ";
					int[] treeStates = talentMatrix.getPointsOnTrees(); 
					for(int i = 0; i<treeStates.length; ++i) {
						message += "|" + treeStates[i] + "|";
					}
					message += " Unspent points: " + talentMatrix.getTalentPoints();
				}
				System.out.println("Player[" + player.getCommandSenderName() + "] classinfo: " + message);
				player.addChatMessage(new ChatComponentText(message));
			}
			else {
				BattleClassesUtils.Log("Error! CommandSender isn't player", LogType.COMMAND);
			}
			return;
		}
	}

}
