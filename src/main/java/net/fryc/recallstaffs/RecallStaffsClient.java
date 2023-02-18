package net.fryc.recallstaffs;

import net.fabricmc.api.ClientModInitializer;
import net.fryc.recallstaffs.items.custom.StaffItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class RecallStaffsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ModelPredicateProviderRegistry.register(new Identifier("recalling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem().getItem() instanceof StaffItem ? 1.0F : 0.0F;
        });
    }
}
