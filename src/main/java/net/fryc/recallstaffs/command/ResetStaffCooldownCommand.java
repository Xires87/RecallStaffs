package net.fryc.recallstaffs.command;


import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Iterator;

public class ResetStaffCooldownCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("resetRecallCooldown").requires((source) -> {
            return source.hasPermissionLevel(2);
        })).executes((context) -> {
            return execute((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow()));
        })).then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> {
            return execute((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"));
        })));
    }

    private static int execute(ServerCommandSource source, Collection<? extends Entity> targets) {
        Iterator var2 = targets.iterator();

        while(var2.hasNext()) {
            Entity entity = (Entity)var2.next();
            if(entity instanceof ServerPlayerEntity player){
                ((ServerPlayerGetters) player).setRecallStaffCooldown(0);
            }
        }


        source.sendFeedback(Text.literal("Recall staff cooldown has been set to 0"), true);

        return targets.size();
    }

}
