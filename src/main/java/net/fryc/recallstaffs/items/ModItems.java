package net.fryc.recallstaffs.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.items.custom.StaffItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item WOODEN_RECALL_STAFF = registerItem("wooden_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1)));

    public static final Item COPPER_RECALL_STAFF = registerItem("copper_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1)));

    public static final Item IRON_RECALL_STAFF = registerItem("iron_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1)));

    public static final Item GOLDEN_RECALL_STAFF = registerItem("golden_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1)));

    public static final Item DIAMOND_RECALL_STAFF = registerItem("diamond_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1)));

    public static final Item NETHERITE_RECALL_STAFF = registerItem("netherite_recall_staff" ,
            new StaffItem(new FabricItemSettings().maxCount(1).fireproof()));



    //registers------------------------------------------------------------------------------------------
    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(RecallStaffs.MOD_ID, name), item);
    }
    public static void registerModItems(){
        //creative
        ItemGroup RECALLSTAFFS = FabricItemGroup.builder(new Identifier(RecallStaffs.MOD_ID, "recallstaffs_item_group"))
                .displayName(Text.literal("Recall Staffs"))
                .icon(() -> new ItemStack(ModItems.NETHERITE_RECALL_STAFF))
                .entries((enabledFeatures, entries, operatorEnabled) -> {

                    entries.add(ModItems.WOODEN_RECALL_STAFF);
                    entries.add(ModItems.COPPER_RECALL_STAFF);
                    entries.add(ModItems.IRON_RECALL_STAFF);
                    entries.add(ModItems.GOLDEN_RECALL_STAFF);
                    entries.add(ModItems.DIAMOND_RECALL_STAFF);
                    entries.add(ModItems.NETHERITE_RECALL_STAFF);

                })
                .build();
    }
}
