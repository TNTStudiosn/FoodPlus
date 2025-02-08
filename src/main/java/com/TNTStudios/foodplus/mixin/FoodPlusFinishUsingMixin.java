package com.TNTStudios.foodplus.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.potion.PotionUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public abstract class FoodPlusFinishUsingMixin {

    /**
     * Inyectamos al principio del método finishUsing para que, al consumir un alimento
     * que tenga en su NBT la información de una poción (es decir, las claves "Potion" o "CustomPotionEffects"),
     * se extraigan y apliquen los efectos al usuario antes de que se pierda el NBT.
     */
    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void foodPlus_finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        // Solo se ejecuta en el servidor.
        if (world.isClient()) {
            return;
        }
        // Procesamos únicamente si el item es alimento y posee en su NBT datos de poción.
        if (stack.getItem().getFoodComponent() != null && stack.hasNbt() &&
                (stack.getNbt().contains("Potion") || stack.getNbt().contains("CustomPotionEffects"))) {

            // Obtenemos la poción base usando el NBT (la clave "Potion")
            Potion potion = PotionUtil.getPotion(stack);
            // Extraemos la lista de efectos base y custom (definidos en "CustomPotionEffects")
            List<StatusEffectInstance> baseEffects = potion.getEffects();
            List<StatusEffectInstance> customEffects = PotionUtil.getCustomPotionEffects(stack);

            // Aplicamos los efectos base.
            if (baseEffects != null) {
                for (StatusEffectInstance effectInstance : baseEffects) {
                    user.addStatusEffect(new StatusEffectInstance(
                            effectInstance.getEffectType(),
                            effectInstance.getDuration(),
                            effectInstance.getAmplifier(),
                            effectInstance.isAmbient(),
                            effectInstance.shouldShowParticles()
                    ));
                }
            }

            // Aplicamos los efectos custom.
            if (customEffects != null) {
                for (StatusEffectInstance effectInstance : customEffects) {
                    user.addStatusEffect(new StatusEffectInstance(
                            effectInstance.getEffectType(),
                            effectInstance.getDuration(),
                            effectInstance.getAmplifier(),
                            effectInstance.isAmbient(),
                            effectInstance.shouldShowParticles()
                    ));
                }
            }
        }
    }
}
