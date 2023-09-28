package net.fryc.recallstaffs.effects;

import net.fryc.recallstaffs.RecallStaffs;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEffects {

    public static StatusEffect INVULNERABILITY_EFFECT;


    static StatusEffect invulnerability = new InvulnerabilityEffect(StatusEffectCategory.BENEFICIAL, 0);


    public static StatusEffect registerStatusEffect(String name, StatusEffect effect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RecallStaffs.MOD_ID, name),
                effect);
    }



    public static void registerEffects() {
        if(INVULNERABILITY_EFFECT == null){
            INVULNERABILITY_EFFECT = registerStatusEffect("invulnerability", invulnerability);

        }
    }
}
