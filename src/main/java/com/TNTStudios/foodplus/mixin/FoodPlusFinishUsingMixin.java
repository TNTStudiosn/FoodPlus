package com.TNTStudios.foodplus.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class FoodPlusFinishUsingMixin {

    /**
     * Inyectamos al final del método finishUsing para aplicar efectos
     * si el ItemStack consumido contiene el tag "FoodPlusPotion".
     * Si el ItemStack pierde NBT en el proceso de consumo, habrá que
     * capturarlo antes o inyectarse en otro método.
     */
    @Inject(method = "finishUsing", at = @At("TAIL"))
    private void foodPlus_finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        // Solo se ejecuta en el servidor.
        if (world.isClient()) {
            return;
        }
        // Comprobamos si el ItemStack tiene NBT y contiene la key "FoodPlusPotion"
        if (stack.hasNbt() && stack.getNbt().contains("FoodPlusPotion")) {
            String potionIdStr = stack.getNbt().getString("FoodPlusPotion");
            Identifier potionId = new Identifier(potionIdStr);
            Potion potion = Registries.POTION.get(potionId);
            if (potion != null && potion != Potions.EMPTY) {
                // Se recorren los efectos de la poción y se aplican al usuario.
                for (StatusEffectInstance effectInstance : potion.getEffects()) {
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
