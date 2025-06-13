package net.fryc.recallstaffs.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fryc.recallstaffs.items.custom.StaffItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
abstract class ClientPlayerEntityMixin {

    @WrapOperation(
            method = "updateNausea()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F")
    )
    private static float tickNauseaWhenUsingStaff(float first, float second, float third, Operation<Float> original) {
        if (shouldRenderNauseaEffect()) {
            first += 0.056666667F;
        }

        return original.call(first, second, third);
    }

    private static boolean shouldRenderNauseaEffect(){
        MinecraftClient client = MinecraftClient.getInstance();
        if(client != null){
            ClientPlayerEntity player = client.player;
            if(player != null){
                return player.isUsingItem() && player.getActiveItem().getItem() instanceof StaffItem;
            }
        }
        return false;
    }

}
