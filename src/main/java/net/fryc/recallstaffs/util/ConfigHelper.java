package net.fryc.recallstaffs.util;

import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

public class ConfigHelper {

    public static int woodenRecallStaffCooldown = RecallStaffs.config.woodenRecallStaffCooldown;
    public static int copperRecallStaffCooldown = RecallStaffs.config.copperRecallStaffCooldown;
    public static int ironRecallStaffCooldown = RecallStaffs.config.ironRecallStaffCooldown;
    public static int goldenRecallStaffCooldown = RecallStaffs.config.goldenRecallStaffCooldown;
    public static int diamondRecallStaffCooldown = RecallStaffs.config.diamondRecallStaffCooldown;
    public static int netheriteRecallStaffCooldown = RecallStaffs.config.netheriteRecallStaffCooldown;

    public static int woodenRecallCost = RecallStaffs.config.woodenRecallCost;
    public static int copperRecallCost = RecallStaffs.config.copperRecallCost;
    public static int ironRecallCost = RecallStaffs.config.ironRecallCost;
    public static int goldenRecallCost = RecallStaffs.config.goldenRecallCost;
    public static int diamondRecallCost = RecallStaffs.config.diamondRecallCost;
    public static int netheriteRecallCost = RecallStaffs.config.netheriteRecallCost;


    public static int recallStaffCraftCost = RecallStaffs.config.recallStaffCraftCost;

    /**
     *  A - recall cost
     *  B - recall cooldown
     */
    public static Pair<Integer, Integer> getRecallStaffCostAndCooldown(ItemStack staff, @Nullable World world){
        if(staff.isOf(ModItems.WOODEN_RECALL_STAFF)){
            if(world != null){
                if(world.isClient()){
                    return new Pair<>(woodenRecallCost, woodenRecallStaffCooldown);
                }
            }
            return new Pair<>(RecallStaffs.config.woodenRecallCost, RecallStaffs.config.woodenRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.COPPER_RECALL_STAFF)){
            if(world != null){
                if(world.isClient()){
                    return new Pair<>(copperRecallCost, copperRecallStaffCooldown);
                }
            }
            return new Pair<>(RecallStaffs.config.copperRecallCost, RecallStaffs.config.copperRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.IRON_RECALL_STAFF)){
            if(world != null){
                if(world.isClient()){
                    return new Pair<>(ironRecallCost, ironRecallStaffCooldown);
                }
            }
            return new Pair<>(RecallStaffs.config.ironRecallCost, RecallStaffs.config.ironRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.GOLDEN_RECALL_STAFF)){
            if(world != null){
                if(world.isClient()){
                    return new Pair<>(goldenRecallCost, goldenRecallStaffCooldown);
                }
            }
            return new Pair<>(RecallStaffs.config.goldenRecallCost, RecallStaffs.config.goldenRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.DIAMOND_RECALL_STAFF)){
            if(world != null){
                if(world.isClient()){
                    return new Pair<>(diamondRecallCost, diamondRecallStaffCooldown);
                }
            }
            return new Pair<>(RecallStaffs.config.diamondRecallCost, RecallStaffs.config.diamondRecallStaffCooldown);
        }
        else {
            if(world != null){
                if(world.isClient()){
                    return new Pair<>(netheriteRecallCost, netheriteRecallStaffCooldown);
                }
            }
            return new Pair<>(RecallStaffs.config.netheriteRecallCost, RecallStaffs.config.netheriteRecallStaffCooldown);
        }
    }
}
