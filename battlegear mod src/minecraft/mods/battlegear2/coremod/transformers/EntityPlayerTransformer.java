package mods.battlegear2.coremod.transformers;

import mods.battlegear2.api.core.BattlegearTranslator;
import mods.battlegear2.api.core.IBattlePlayer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.ListIterator;

public final class EntityPlayerTransformer extends TransformerBase {
    public static final int DATAWATCHER_SHIELD = 25;
    public EntityPlayerTransformer() {
		super("net.minecraft.entity.player.EntityPlayer");
	}

	private String entityPlayerClassName;
    private String inventoryClassName;
    private String itemStackClassName;
    private String entityClassName;
    private String entityLivingClassName;
    private String dataWatcherClassName;


    private String playerInventoryFieldName;
    private String playerDataWatcherField;
    private String playerItemInUseField;

    private String onItemFinishMethodName;
    private String onItemFinishMethodDesc;
    private String setCurrentItemArmourMethodName;
    private String setCurrentItemArmourMethodDesc;
    private String onUpdateMethodName;
    private String onUpdateMethodDesc;
    private String playerUpdateArmSwingMethodName;
    private String getArmSwingEndMethodName;
    private String dataWatcherAddObjectMethodName;
    private String dataWatcherAddObjectMethodDesc;
    private String playerInitMethodName;
    private String playerInitMethodDesc;
    private String dataWatcherUpdateObjectMethodName;
    private String dataWatcherUpdateObjectMethodDesc;
    
    @Override
    void addInterface(List<String> interfaces) {
        interfaces.add(Type.getInternalName(IBattlePlayer.class));
    }

    @Override
    boolean processFields(List<FieldNode> fields) {
        logger.log(Level.INFO, "\tAdding new fields to EntityPlayer");
        fields.add(fields.size(), new FieldNode(ACC_PUBLIC, "offHandSwingProgress", "F", null, 0F));
        fields.add(fields.size(), new FieldNode(ACC_PUBLIC, "prevOffHandSwingProgress", "F", null, 0F));
        fields.add(fields.size(), new FieldNode(ACC_PUBLIC, "offHandSwingProgressInt", "I", null, 0));
        fields.add(fields.size(), new FieldNode(ACC_PUBLIC, "isOffHandSwingInProgress", "Z", null, false));
        fields.add(fields.size(), new FieldNode(ACC_PUBLIC, "specialActionTimer", "I", null, 0));
        return true;
    }

    @Override
	boolean processMethods(List<MethodNode> methods) {
        int found = 0;
    	for (MethodNode mn : methods) {
            if (mn.name.equals("<init>")) {
                logger.log(Level.INFO, "\tPatching constructor in EntityPlayer");
                ListIterator<AbstractInsnNode> it = mn.instructions.iterator();

                while (it.hasNext()) {
                    AbstractInsnNode insn = it.next();
                    if (insn instanceof TypeInsnNode) {
                        if (((TypeInsnNode) insn).desc.equals(inventoryClassName)) {
                            ((TypeInsnNode) insn).desc = "mods/battlegear2/api/core/InventoryPlayerBattle";
                        }

                    } else if (insn instanceof MethodInsnNode) {
                        if (((MethodInsnNode) insn).owner.equals(inventoryClassName)) {
                            ((MethodInsnNode) insn).owner = "mods/battlegear2/api/core/InventoryPlayerBattle";
                        }
                    }
                }
                found++;
            } else if (mn.name.equals(onItemFinishMethodName) && mn.desc.equals(onItemFinishMethodDesc)) {
                sendPatchLog("onItemUseFinish");
                InsnList newList = new InsnList();
                ListIterator<AbstractInsnNode> it = mn.instructions.iterator();
                while (it.hasNext()) {
                    AbstractInsnNode next = it.next();
                    if (next instanceof MethodInsnNode && ((MethodInsnNode) next).owner.equals("net/minecraftforge/event/ForgeEventFactory")) {
                        found++;
                        int index = ((MethodInsnNode) next).desc.indexOf(")");
                        String newDesc = ((MethodInsnNode) next).desc.substring(0, index) + "I" + ((MethodInsnNode) next).desc.substring(index);
                        newList.add(new VarInsnNode(ILOAD, 1));
                        newList.add(new MethodInsnNode(INVOKESTATIC, "mods/battlegear2/api/core/BattlegearUtils", "beforeFinishUseEvent", newDesc));
                    } else {
                        newList.add(next);
                    }
                }
                mn.instructions = newList;
            } else if(mn.name.equals(onUpdateMethodName) && mn.desc.equals(onUpdateMethodDesc)){
                sendPatchLog("onUpdate");
                InsnList newList = new InsnList();
                ListIterator<AbstractInsnNode> it = mn.instructions.iterator();
                while (it.hasNext()) {
                    AbstractInsnNode next = it.next();
                    if(next instanceof FieldInsnNode && ((FieldInsnNode) next).owner.equals(entityPlayerClassName) && ((FieldInsnNode) next).name.equals(playerInventoryFieldName)){
                        found++;
                        newList.add(new VarInsnNode(ALOAD, 0));
                        newList.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, playerItemInUseField, "L"+itemStackClassName+";"));
                        newList.add(new MethodInsnNode(INVOKESTATIC, "mods/battlegear2/api/core/BattlegearUtils", "getCurrentItemOnUpdate", "(L"+entityPlayerClassName+";L"+itemStackClassName+";)L"+itemStackClassName+";"));
                        next = it.next();
                    }else{
                        newList.add(next);
                    }
                }
                mn.instructions = newList;
            } else if (mn.name.equals(setCurrentItemArmourMethodName) && mn.desc.equals(setCurrentItemArmourMethodDesc)) {

                sendPatchLog("setCurrentItemOrArmor");
                replaceInventoryArrayAccess(mn, entityPlayerClassName, playerInventoryFieldName, mn.maxStack, mn.maxLocals);
                found++;
            } else if(mn.name.equals(playerInitMethodName) && mn.desc.equals(playerInitMethodDesc)) {

            	sendPatchLog("entityInit");

                InsnList newList = new InsnList();
                ListIterator<AbstractInsnNode> it = mn.instructions.iterator();
                while(it.hasNext()){
                    AbstractInsnNode next = it.next();

                    if(next instanceof InsnNode && next.getOpcode() == RETURN){
                        found++;
                        newList.add(new VarInsnNode(ALOAD, 0));
                        newList.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, playerDataWatcherField, "L"+dataWatcherClassName+";"));
                        newList.add(new VarInsnNode(BIPUSH, DATAWATCHER_SHIELD));
                        newList.add(new InsnNode(ICONST_0));
                        newList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;"));
                        newList.add(new MethodInsnNode(INVOKEVIRTUAL, dataWatcherClassName, dataWatcherAddObjectMethodName, dataWatcherAddObjectMethodDesc));
                    }

                    newList.add(next);
                }

                mn.instructions = newList;
            }
        }

        logger.log(Level.INFO, "\tCreating new methods in EntityPlayer");
        methods.add(methods.size(), generateAttackOffhandMethod());
        methods.add(methods.size(), generateSwingOffhand());
        methods.add(methods.size(), generateGetOffSwingMethod());
        methods.add(methods.size(), generateUpdateSwingArm());
        methods.add(methods.size(), generateIsBattleMode());
        methods.add(methods.size(), generateIsBlockingWithShield());
        methods.add(methods.size(), generateSetBlockingWithShield());
        methods.add(methods.size(), generateGetter(entityPlayerClassName, "getSpecialActionTimer", "specialActionTimer", "I"));
        methods.add(methods.size(), generateSetter(entityPlayerClassName, "setSpecialActionTimer", "specialActionTimer", "I"));
        return found == 5;
    }

    private MethodNode generateIsBlockingWithShield() {
        MethodNode mn = new MethodNode(ASM4, ACC_PUBLIC, "isBlockingWithShield", "()Z", null, null);

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new MethodInsnNode(INVOKESTATIC, "mods/battlegear2/api/core/BattlegearUtils", "isBlockingWithShield", "(L"+entityPlayerClassName+";)Z"));
        mn.instructions.add(new InsnNode(IRETURN));

        mn.maxStack = 1;
        mn.maxLocals = 1;

        return mn;
    }


    private MethodNode generateSetBlockingWithShield() {
        MethodNode mn = new MethodNode(ASM4, ACC_PUBLIC, "setBlockingWithShield", "(Z)V", null, null);

        LabelNode L1 = new LabelNode();
        LabelNode L3 = new LabelNode();

        mn.instructions.add(new VarInsnNode(ILOAD, 1));
        mn.instructions.add(new JumpInsnNode(IFEQ, L1));

        //if( BattlegearUtils.canBlockWithShield(this))
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new MethodInsnNode(INVOKESTATIC, "mods/battlegear2/api/core/BattlegearUtils", "canBlockWithShield", "(L"+entityPlayerClassName+";)Z"));
        mn.instructions.add(new JumpInsnNode(IFEQ, L1));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, playerDataWatcherField, "L"+dataWatcherClassName+";"));
        mn.instructions.add(new VarInsnNode(BIPUSH, DATAWATCHER_SHIELD));
        mn.instructions.add(new InsnNode(ICONST_1));
        mn.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;"));

        mn.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, dataWatcherClassName, dataWatcherUpdateObjectMethodName, dataWatcherUpdateObjectMethodDesc));
        mn.instructions.add(new JumpInsnNode(GOTO, L3));

        mn.instructions.add(L1);
        mn.instructions.add(new FrameNode(F_SAME, 0, null, 0, null));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, playerDataWatcherField, "L"+dataWatcherClassName+";"));
        mn.instructions.add(new VarInsnNode(BIPUSH, DATAWATCHER_SHIELD));
        mn.instructions.add(new InsnNode(ICONST_0));
        mn.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;"));
        mn.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, dataWatcherClassName, dataWatcherUpdateObjectMethodName, dataWatcherUpdateObjectMethodDesc));

        mn.instructions.add(L3);
        mn.instructions.add(new FrameNode(F_SAME, 0, null, 0, null));
        mn.instructions.add(new InsnNode(RETURN));


        mn.maxStack = 3;
        mn.maxLocals = 2;

        return mn;
    }

    private MethodNode generateAttackOffhandMethod() {

        MethodNode mn = new MethodNode(ASM4, ACC_PUBLIC, "attackTargetEntityWithCurrentOffItem", "(L" + entityClassName + ";)V", null, null);

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new VarInsnNode(ALOAD, 1));
        mn.instructions.add(new MethodInsnNode(INVOKESTATIC, "mods/battlegear2/api/core/BattlegearUtils",
                "attackTargetEntityWithCurrentOffItem", "(L" + entityPlayerClassName + ";L" + entityClassName + ";)V"));
        mn.instructions.add(new InsnNode(RETURN));

        mn.maxStack = 2;
        mn.maxLocals = 2;

        return mn;
    }

    private MethodNode generateSwingOffhand() {
        MethodNode mn = new MethodNode(ASM4, ACC_PUBLIC, "swingOffItem", "()V", null, null);

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "isOffHandSwingInProgress", "Z"));
        LabelNode l0 = new LabelNode();
        mn.instructions.add(new JumpInsnNode(IFEQ, l0));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new MethodInsnNode(INVOKESPECIAL, entityPlayerClassName, getArmSwingEndMethodName, "()I"));
        mn.instructions.add(new InsnNode(ICONST_2));
        mn.instructions.add(new InsnNode(IDIV));
        mn.instructions.add(new JumpInsnNode(IF_ICMPGE, l0));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));
        LabelNode l1 = new LabelNode();
        mn.instructions.add(new JumpInsnNode(IFGE, l1));
        mn.instructions.add(l0);
        mn.instructions.add(new FrameNode(F_SAME, 0, null, 0, null));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new InsnNode(ICONST_M1));
        mn.instructions.add(new FieldInsnNode(PUTFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new InsnNode(ICONST_1));
        mn.instructions.add(new FieldInsnNode(PUTFIELD, entityPlayerClassName, "isOffHandSwingInProgress", "Z"));
        mn.instructions.add(l1);
        mn.instructions.add(new FrameNode(F_SAME, 0, null, 0, null));
        mn.instructions.add(new InsnNode(RETURN));

        mn.maxStack = 3;
        mn.maxLocals = 1;

        return mn;
    }

    private MethodNode generateGetOffSwingMethod() {

        MethodNode mn = new MethodNode(ASM4, ACC_PUBLIC, "getOffSwingProgress", "(F)F", null, null);

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "offHandSwingProgress", "F"));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "prevOffHandSwingProgress", "F"));
        mn.instructions.add(new InsnNode(FSUB));
        mn.instructions.add(new VarInsnNode(FSTORE, 2));

        mn.instructions.add(new VarInsnNode(FLOAD, 2));
        mn.instructions.add(new InsnNode(FCONST_0));
        mn.instructions.add(new InsnNode(FCMPG));
        LabelNode l0 = new LabelNode();
        mn.instructions.add(new JumpInsnNode(IFGE, l0));

        mn.instructions.add(new VarInsnNode(FLOAD, 2));
        mn.instructions.add(new InsnNode(FCONST_1));
        mn.instructions.add(new InsnNode(FADD));
        mn.instructions.add(new VarInsnNode(FSTORE, 2));

        mn.instructions.add(l0);
        mn.instructions.add(new FrameNode(F_APPEND, 1, new Object[]{FLOAT}, 0, null));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "prevOffHandSwingProgress", "F"));

        mn.instructions.add(new VarInsnNode(FLOAD, 2));
        mn.instructions.add(new VarInsnNode(FLOAD, 1));
        mn.instructions.add(new InsnNode(FMUL));
        mn.instructions.add(new InsnNode(FADD));
        mn.instructions.add(new InsnNode(FRETURN));

        mn.maxLocals = 3;
        mn.maxStack = 3;

        return mn;
    }

    private MethodNode generateUpdateSwingArm() {

        MethodNode mn = new MethodNode(ASM4, ACC_PROTECTED, playerUpdateArmSwingMethodName, "()V", null, null);

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new MethodInsnNode(INVOKESPECIAL, entityLivingClassName, playerUpdateArmSwingMethodName, "()V"));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));

        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "offHandSwingProgress", "F"));
        mn.instructions.add(new FieldInsnNode(PUTFIELD, entityPlayerClassName, "prevOffHandSwingProgress", "F"));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));

        mn.instructions.add(new MethodInsnNode(INVOKESPECIAL, entityPlayerClassName, getArmSwingEndMethodName, "()I"));
        mn.instructions.add(new VarInsnNode(ISTORE, 1));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));


        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "isOffHandSwingInProgress", "Z"));
        LabelNode l0 = new LabelNode();
        mn.instructions.add(new JumpInsnNode(IFEQ, l0));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new InsnNode(DUP));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));
        mn.instructions.add(new InsnNode(ICONST_1));
        mn.instructions.add(new InsnNode(IADD));
        mn.instructions.add(new FieldInsnNode(PUTFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));
        mn.instructions.add(new VarInsnNode(ILOAD, 1));
        LabelNode l1 = new LabelNode();
        mn.instructions.add(new JumpInsnNode(IF_ICMPLT, l1));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new InsnNode(ICONST_0));
        mn.instructions.add(new FieldInsnNode(PUTFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new InsnNode(ICONST_0));
        mn.instructions.add(new FieldInsnNode(PUTFIELD, entityPlayerClassName, "isOffHandSwingInProgress", "Z"));
        mn.instructions.add(new JumpInsnNode(GOTO, l1));

        mn.instructions.add(l0);
        mn.instructions.add(new FrameNode(F_APPEND, 1, new Object[]{INTEGER}, 0, null));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new InsnNode(ICONST_0));
        mn.instructions.add(new FieldInsnNode(PUTFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));

        mn.instructions.add(l1);
        mn.instructions.add(new FrameNode(F_SAME, 0, null, 0, null));

        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new FieldInsnNode(GETFIELD, entityPlayerClassName, "offHandSwingProgressInt", "I"));
        mn.instructions.add(new InsnNode(I2F));
        mn.instructions.add(new VarInsnNode(ILOAD, 1));
        mn.instructions.add(new InsnNode(I2F));
        mn.instructions.add(new InsnNode(FDIV));
        mn.instructions.add(new FieldInsnNode(PUTFIELD, entityPlayerClassName, "offHandSwingProgress", "F"));
        mn.instructions.add(new InsnNode(RETURN));

        mn.maxStack = 3;
        mn.maxLocals = 2;


        return mn;
    }


    private MethodNode generateIsBattleMode() {
        MethodNode mn = new MethodNode(ASM4, ACC_PUBLIC, "isBattlemode", "()Z", null, null);
        mn.instructions.add(new VarInsnNode(ALOAD, 0));
        mn.instructions.add(new MethodInsnNode(INVOKESTATIC, "mods/battlegear2/api/core/BattlegearUtils", "isPlayerInBattlemode", "(L"+entityPlayerClassName+";)Z"));
        mn.instructions.add(new InsnNode(IRETURN));
        mn.maxStack = 1;
        mn.maxLocals = 1;

        return mn;
    }

	@Override
	void setupMappings() {
		entityPlayerClassName = BattlegearTranslator.getMapedClassName("entity.player.EntityPlayer");
        inventoryClassName = BattlegearTranslator.getMapedClassName("entity.player.InventoryPlayer");
        itemStackClassName = BattlegearTranslator.getMapedClassName("item.ItemStack");
        entityClassName = BattlegearTranslator.getMapedClassName("entity.Entity");
        entityLivingClassName = BattlegearTranslator.getMapedClassName("entity.EntityLivingBase");
        dataWatcherClassName = BattlegearTranslator.getMapedClassName("entity.DataWatcher");

        playerInventoryFieldName =
                BattlegearTranslator.getMapedFieldName("EntityPlayer", "field_71071_by", "inventory");
        playerDataWatcherField =
                BattlegearTranslator.getMapedFieldName("Entity", "field_70180_af", "dataWatcher");
        playerItemInUseField = BattlegearTranslator.getMapedFieldName("EntityPlayer", "field_71074_e", "itemInUse");

        onItemFinishMethodName =
                BattlegearTranslator.getMapedMethodName("EntityPlayer", "func_71036_o", "onItemUseFinish");
        onItemFinishMethodDesc =
                BattlegearTranslator.getMapedMethodDesc("EntityPlayer", "func_71036_o", "()V");
        setCurrentItemArmourMethodName =
                BattlegearTranslator.getMapedMethodName("EntityPlayer", "func_70062_b", "setCurrentItemOrArmor");
        setCurrentItemArmourMethodDesc =
                BattlegearTranslator.getMapedMethodDesc("EntityPlayer", "func_70062_b", "(IL"+itemStackClassName+";)V");
        onUpdateMethodName = BattlegearTranslator.getMapedMethodName("EntityPlayer", "func_70071_h_", "onUpdate");
        onUpdateMethodDesc = BattlegearTranslator.getMapedMethodDesc("EntityPlayer", "func_70071_h_", "()V");
        playerUpdateArmSwingMethodName =
                BattlegearTranslator.getMapedMethodName("EntityLivingBase", "func_82168_bl", "updateArmSwingProgress");
        getArmSwingEndMethodName =
                BattlegearTranslator.getMapedMethodName("EntityLivingBase", "func_82166_i", "getArmSwingAnimationEnd");
        dataWatcherAddObjectMethodName =
                BattlegearTranslator.getMapedMethodName("DataWatcher", "func_75682_a", "addObject");
        dataWatcherAddObjectMethodDesc =
                BattlegearTranslator.getMapedMethodDesc("DataWatcher", "func_75682_a", "(ILjava/lang/Object;)V");
        playerInitMethodName =
                BattlegearTranslator.getMapedMethodName("EntityPlayer", "func_70088_a", "entityInit");
        playerInitMethodDesc =
                BattlegearTranslator.getMapedMethodDesc("EntityPlayer", "func_70088_a", "()V");
        dataWatcherUpdateObjectMethodName =
                BattlegearTranslator.getMapedMethodName("DataWatcher", "func_75692_b", "updateObject");
        dataWatcherUpdateObjectMethodDesc =
                BattlegearTranslator.getMapedMethodDesc("DataWatcher", "func_75692_b", "(ILjava/lang/Object;)V");
	}


}
