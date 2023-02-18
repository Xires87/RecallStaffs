package net.fryc.recallstaffs.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.items.custom.StaffItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item WOODEN_RECALL_STAFF = registerItem("wooden_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS)));

    public static final Item COPPER_RECALL_STAFF = registerItem("copper_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS)));

    public static final Item IRON_RECALL_STAFF = registerItem("iron_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS)));

    public static final Item GOLDEN_RECALL_STAFF = registerItem("golden_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS)));

    public static final Item DIAMOND_RECALL_STAFF = registerItem("diamond_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1).group(ItemGroup.TOOLS)));

    public static final Item NETHERITE_RECALL_STAFF = registerItem("netherite_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1).fireproof().group(ItemGroup.TOOLS)));



    //registers------------------------------------------------------------------------------------------
    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(RecallStaffs.MOD_ID, name), item);
    }
    public static void registerModItems(){

    }
}
