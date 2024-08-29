package net.fryc.recallstaffs.effects;

import net.fryc.recallstaffs.RecallStaffs;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static RegistryEntry<StatusEffect> INVULNERABILITY_EFFECT;


    static StatusEffect invulnerability = new InvulnerabilityEffect(StatusEffectCategory.BENEFICIAL, 0);


    public static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(RecallStaffs.MOD_ID, name),
                effect);
    }



    public static void registerEffects() {
        if(INVULNERABILITY_EFFECT == null){
            INVULNERABILITY_EFFECT = registerStatusEffect("invulnerability", invulnerability);

        }
    }
}
