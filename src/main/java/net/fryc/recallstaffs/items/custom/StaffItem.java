package net.fryc.recallstaffs.items.custom;

import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.config.ConfigHelper;
import net.fryc.recallstaffs.items.ModItems;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.List;

public class StaffItem extends Item {
    public StaffItem(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(RecallStaffs.config.enableTooltipsForRecallStaffs){
            Pair<Integer, Integer> pair = ConfigHelper.getRecallStaffCostAndCooldown(stack, world);
            tooltip.add(Text.literal("Usage cost: " + pair.getA() + " levels").formatted(Formatting.BLUE));
            tooltip.add(Text.literal("Cooldown: " + pair.getB() + "s").formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 140;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(user instanceof ServerPlayerEntity splayer){
            int cooldown = ((ServerPlayerGetters) splayer).getRecallStaffCooldown();
            if(cooldown < 1){
                user.setCurrentHand(hand);
                int time = setNauseaTime(this);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200 + time, 0));
                return TypedActionResult.pass(itemStack);
            }
            else{
                cooldown /= 20;
                splayer.sendMessage(Text.literal("Time before next recall: " + cooldown + "s"), true);
                splayer.getItemCooldownManager().set(this, 20);
                return TypedActionResult.fail(itemStack);
            }
        }
        return TypedActionResult.fail(itemStack);
    }


    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(remainingUseTicks > 0 && user.hasStatusEffect(StatusEffects.NAUSEA)){
            if(user.getActiveStatusEffects().get(StatusEffects.NAUSEA).getDuration() < 800){
                user.removeStatusEffect(StatusEffects.NAUSEA);
            }
        }
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        if (!world.isClient()) {

            int d = (int) user.getX();
            int e = (int) user.getY();
            int f = (int) user.getZ();

            AbstractHorseEntity horse = null;
            int additionalCost = 0;
            if (user.hasVehicle()) {
                if(user.getVehicle() instanceof AbstractHorseEntity horseEntity){
                    horse = horseEntity;
                    additionalCost = RecallStaffs.config.recallingWithHorseAdditionalCost;
                }
                user.stopRiding();
            }

            if (user instanceof ServerPlayerEntity player) {
                Pair<Integer, Integer> pair = ConfigHelper.getRecallStaffCostAndCooldown(stack, world);
                int recallCost = pair.getA();
                int recallCooldown = pair.getB();
                if(!(player.experienceLevel < recallCost && RecallStaffs.config.checkPlayersLevelBeforeRecall)){
                    player.setInvulnerable(true);

                    Vec3d vec3d = user.getPos();
                    BlockPos spawnPos = ((ServerPlayerGetters) player).getServerPlayerSpawnPosition();
                    if(spawnPos == null) spawnPos = player.getWorld().getSpawnPos();
                    if(player.getServer() != null){
                        if(horse != null){
                            horse.teleport(player.getServer().getWorld(player.getSpawnPointDimension()), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), null, horse.getYaw(), horse.getPitch());
                        }
                        player.teleport(player.getServer().getWorld(player.getSpawnPointDimension()), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), player.getYaw(), player.getPitch());
                        world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));
                    }

                    player.getItemCooldownManager().set(stack.getItem(), 100);
                    player.setExperienceLevel(changePlayerLevel(player.experienceLevel, recallCost, additionalCost));

                    if(RecallStaffs.config.recallingSummonsLightningBolt) EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, new BlockPos(d,e,f), SpawnReason.TRIGGERED);
                    ((ServerPlayerGetters) player).setRecallStaffCooldown(recallCooldown * 20);

                    player.setInvulnerable(false);
                }
                else{
                    player.getItemCooldownManager().set(stack.getItem(), 40);
                    player.sendMessage(Text.literal("Your level is too low to recall!").formatted(Formatting.RED), true);
                    if(user.hasStatusEffect(StatusEffects.NAUSEA)){
                        if(user.getActiveStatusEffects().get(StatusEffects.NAUSEA).getDuration() < 700){
                            user.removeStatusEffect(StatusEffects.NAUSEA);
                        }
                    }
                }
            }
        }

        return itemStack;
    }

    private static int changePlayerLevel(int playerLevel, int cost, int additionalCost){
        playerLevel -= cost + additionalCost;

        if(playerLevel < 0) playerLevel = 0;
        return playerLevel;
    }

    private static int setNauseaTime(Item item){
        int time = 600;
        if(item.getDefaultStack().isOf(ModItems.COPPER_RECALL_STAFF)) time -= 200;
        else if(item.getDefaultStack().isOf(ModItems.IRON_RECALL_STAFF)) time -= 300;
        else if(item.getDefaultStack().isOf(ModItems.GOLDEN_RECALL_STAFF)) time -= 400;
        else if(item.getDefaultStack().isOf(ModItems.DIAMOND_RECALL_STAFF)) time -= 500;
        else if(item.getDefaultStack().isOf(ModItems.NETHERITE_RECALL_STAFF)) time = 0;
        return time;
    }

}
