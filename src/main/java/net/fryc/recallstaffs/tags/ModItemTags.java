package net.fryc.recallstaffs.tags;

import net.fryc.recallstaffs.RecallStaffs;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {

    public static final TagKey<Item> RECALL_STAFFS = ModItemTags.register("recall_staffs");
    public static final TagKey<Item> RECALL_STAFFS_REQUIRE_LEVEL = ModItemTags.register("recall_staffs_require_level");


    private ModItemTags() {
    }

    private static TagKey<Item> register(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(RecallStaffs.MOD_ID, id));
    }
}
