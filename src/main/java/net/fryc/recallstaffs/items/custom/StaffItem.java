package net.fryc.recallstaffs.items.custom;

import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.items.ModItems;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

import java.util.List;

public class StaffItem extends Item {
    public StaffItem(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(RecallStaffs.config.enableTooltipsForRecallStaffs){
            tooltip.add(Text.literal("Usage cost: " + getUsageCost(this) + " levels").formatted(Formatting.BLUE));
            tooltip.add(Text.literal("Cooldown: " + getStaffCooldown(this) + "s").formatted(Formatting.GRAY));
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
                splayer.sendMessage(Text.literal("Time before next recall: " + cooldown + "s"));
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
        if (!world.isClient) {

            double d = user.getX();
            double e = user.getY();
            double f = user.getZ();

            if (user.hasVehicle()) {
                user.stopRiding();
            }

            if (user instanceof ServerPlayerEntity player) {
                if(!(player.experienceLevel < getUsageCost(this) && RecallStaffs.config.checkPlayersLevelBeforeRecall)){
                    player.setInvulnerable(true);

                    Vec3d vec3d = user.getPos();
                    BlockPos spawnPos = ((ServerPlayerGetters) player).getServerPlayerSpawnPosition();
                    if(spawnPos == null) spawnPos = player.getWorld().getSpawnPos();
                    if(!player.getWorld().getDimension().bedWorks() && player.getSpawnPointDimension() == World.OVERWORLD){
                        player.moveToWorld(((ServerWorld) world).getServer().getWorld(World.OVERWORLD));
                    }
                    player.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));

                    player.getItemCooldownManager().set(this, 100);
                    player.setExperienceLevel(changePlayerLevel(this, player.experienceLevel));

                    if(RecallStaffs.config.recallingSummonsLightningBolt) EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, new BlockPos(d,e,f), SpawnReason.TRIGGERED);
                    ((ServerPlayerGetters) player).setRecallStaffCooldown(setStaffCooldown(this));

                    player.setInvulnerable(false);
                }
                else{
                    player.getItemCooldownManager().set(this, 40);
                    player.sendMessage(Text.literal("Your level is too low to recall!").formatted(Formatting.RED));
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

    private static int getUsageCost(Item item){
        if(item.getDefaultStack().isOf(ModItems.WOODEN_RECALL_STAFF)) return RecallStaffs.config.woodenRecallCost;
        else if(item.getDefaultStack().isOf(ModItems.COPPER_RECALL_STAFF)) return RecallStaffs.config.copperRecallCost;
        else if(item.getDefaultStack().isOf(ModItems.IRON_RECALL_STAFF)) return RecallStaffs.config.ironRecallCost;
        else if(item.getDefaultStack().isOf(ModItems.GOLDEN_RECALL_STAFF)) return RecallStaffs.config.goldenRecallCost;
        else if(item.getDefaultStack().isOf(ModItems.DIAMOND_RECALL_STAFF)) return RecallStaffs.config.diamondRecallCost;
        else if(item.getDefaultStack().isOf(ModItems.NETHERITE_RECALL_STAFF)) return RecallStaffs.config.netheriteRecallCost;
        else return 0;
    }

    private static int getStaffCooldown(Item item){
        if(item.getDefaultStack().isOf(ModItems.WOODEN_RECALL_STAFF)) return RecallStaffs.config.woodenRecallStaffCooldown;
        else if(item.getDefaultStack().isOf(ModItems.COPPER_RECALL_STAFF)) return RecallStaffs.config.copperRecallStaffCooldown;
        else if(item.getDefaultStack().isOf(ModItems.IRON_RECALL_STAFF)) return RecallStaffs.config.ironRecallStaffCooldown;
        else if(item.getDefaultStack().isOf(ModItems.GOLDEN_RECALL_STAFF)) return RecallStaffs.config.goldenRecallStaffCooldown;
        else if(item.getDefaultStack().isOf(ModItems.DIAMOND_RECALL_STAFF)) return RecallStaffs.config.diamondRecallStaffCooldown;
        else if(item.getDefaultStack().isOf(ModItems.NETHERITE_RECALL_STAFF)) return RecallStaffs.config.netheriteRecallStaffCooldown;
        else return 0;
    }

    private static int changePlayerLevel(Item item, int level){
        level -= getUsageCost(item);

        if(level < 0) level = 0;
        return level;
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

    private static int setStaffCooldown(Item item){
        return getStaffCooldown(item) * 20;
    }

}
