package net.fryc.recallstaffs.util;

import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.items.ModItems;
import net.fryc.recallstaffs.items.custom.StaffItem;
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

    public static int calibratedStaffAdditionalCost = RecallStaffs.config.calibratedStaffAdditionalCost;

    public static int recallStaffCraftCost = RecallStaffs.config.recallStaffCraftCost;

    /**
     *  A - recall cost
     *  B - recall cooldown
     */
    public static Pair<Integer, Integer> getRecallStaffCostAndCooldown(ItemStack staff, @Nullable World world){
        if(world != null){
            if(world.isClient()){
                return getClientRecallStaffCostAndCooldown(staff);
            }
        }

        int cost = StaffItem.isCalibrated(staff) ? RecallStaffs.config.calibratedStaffAdditionalCost : 0;
        if(staff.isOf(ModItems.WOODEN_RECALL_STAFF)){
            return new Pair<>(RecallStaffs.config.woodenRecallCost + cost, RecallStaffs.config.woodenRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.COPPER_RECALL_STAFF)){
            return new Pair<>(RecallStaffs.config.copperRecallCost + cost, RecallStaffs.config.copperRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.IRON_RECALL_STAFF)){
            return new Pair<>(RecallStaffs.config.ironRecallCost + cost, RecallStaffs.config.ironRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.GOLDEN_RECALL_STAFF)){
            return new Pair<>(RecallStaffs.config.goldenRecallCost + cost, RecallStaffs.config.goldenRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.DIAMOND_RECALL_STAFF)){
            return new Pair<>(RecallStaffs.config.diamondRecallCost + cost, RecallStaffs.config.diamondRecallStaffCooldown);
        }
        else {
            return new Pair<>(RecallStaffs.config.netheriteRecallCost + cost, RecallStaffs.config.netheriteRecallStaffCooldown);
        }
    }

    public static Pair<Integer, Integer> getClientRecallStaffCostAndCooldown(ItemStack staff){
        int cost = StaffItem.isCalibrated(staff) ? calibratedStaffAdditionalCost : 0;

        if(staff.isOf(ModItems.WOODEN_RECALL_STAFF)){
            return new Pair<>(woodenRecallCost + cost, woodenRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.COPPER_RECALL_STAFF)){
            return new Pair<>(copperRecallCost + cost, copperRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.IRON_RECALL_STAFF)){
            return new Pair<>(ironRecallCost + cost, ironRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.GOLDEN_RECALL_STAFF)){
            return new Pair<>(goldenRecallCost + cost, goldenRecallStaffCooldown);
        }
        else if(staff.isOf(ModItems.DIAMOND_RECALL_STAFF)){
            return new Pair<>(diamondRecallCost + cost, diamondRecallStaffCooldown);
        }
        else {
            return new Pair<>(netheriteRecallCost + cost, netheriteRecallStaffCooldown);
        }
    }
}
