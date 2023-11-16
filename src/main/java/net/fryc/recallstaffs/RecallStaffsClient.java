package net.fryc.recallstaffs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fryc.recallstaffs.blocks.ModBlocks;
import net.fryc.recallstaffs.items.custom.StaffItem;
import net.fryc.recallstaffs.network.ModPackets;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class RecallStaffsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ModelPredicateProviderRegistry.register(new Identifier("recalling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem().getItem() instanceof StaffItem ? 1.0F : 0.0F;
        });

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RECOVERY_ALTAR, RenderLayer.getCutout());

        ModPackets.registerS2CPackets();
    }
}
