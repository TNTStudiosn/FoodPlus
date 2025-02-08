package com.TNTStudios.foodplus.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class FoodItemMixin {

    @Inject(method = "finishUsing", at = @At("RETURN"))
    private void onEat(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.isFood()) {
            List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);

            for (StatusEffectInstance effect : effects) {
                if (effect != null) {
                    user.addStatusEffect(new StatusEffectInstance(effect));
                }
            }
        }
    }
}

