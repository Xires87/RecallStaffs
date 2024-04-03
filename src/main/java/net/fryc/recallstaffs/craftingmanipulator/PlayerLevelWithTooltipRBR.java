package net.fryc.recallstaffs.craftingmanipulator;

import net.fryc.craftingmanipulator.rules.recipeblocking.PlayerLevelRBR;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.tags.ModItemTags;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class PlayerLevelWithTooltipRBR extends PlayerLevelRBR {

    public PlayerLevelWithTooltipRBR(@Nullable TagKey<Item> blockedItems, int playerLevel) {
        super(blockedItems, playerLevel);
    }

    public PlayerLevelWithTooltipRBR(int playerLevel) {
        super(playerLevel);
    }

    public void drawRedCrossWhenNeeded(ItemStack craftedItem, ServerPlayerEntity player, ScreenHandler handler){
        if(craftedItem.isEmpty()){
            if(handler instanceof PlayerScreenHandler){
                this.informAboutItemModification(player, "inventory_red_x");
                this.drawMouseOverTooltip(player, Text.translatable("text.recallstaffs.higher_level_required"), 134, 28, 18, 15);
            }
            else {
                this.informAboutItemModification(player, "crafting_red_x");
                this.drawMouseOverTooltip(player, Text.translatable("text.recallstaffs.higher_level_required"), 87, 32, 28, 21);
            }
        }
    }

    public void drawExperienceOrbWhenNeeded(ItemStack craftedItem, ServerPlayerEntity player, ScreenHandler handler){
        if(!craftedItem.isEmpty()){
            if(craftedItem.isIn(ModItemTags.RECALL_STAFFS_REQUIRE_LEVEL) && RecallStaffs.config.recallStaffCraftCost > 0){
                if(handler instanceof PlayerScreenHandler){
                    this.informAboutItemModification(player, "inventory_xp_orb");
                    this.drawMouseOverTooltip(player, Text.translatable("text.recallstaffs.consumes_level"), 137, 16, 11, 11);
                }
                else {
                    this.informAboutItemModification(player, "crafting_xp_orb");
                    this.drawMouseOverTooltip(player, Text.translatable("text.recallstaffs.consumes_level"), 95, 21, 11, 11);
                }
            }
        }
    }

    @Override
    public ItemStack modifyCraftedItem(ItemStack craftedItem, ServerPlayerEntity player, ServerWorld world, ScreenHandler handler, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory) {
        craftedItem = super.modifyCraftedItem(craftedItem, player, world, handler, craftingInventory, resultInventory);

        this.drawExperienceOrbWhenNeeded(craftedItem, player, handler);

        return craftedItem;
    }

}
