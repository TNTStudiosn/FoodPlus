package com.TNTStudios.foodplus.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public abstract class CraftingResultSlotMixin {

    // Se usa el RecipeInputInventory (alias: input) para recorrer los ingredientes.
    @Shadow
    private RecipeInputInventory input;

    /**
     * Inyectamos al final del método onTakeItem para modificar el ItemStack resultante.
     * Solo se procesa si el item es alimento (tiene FoodComponent). Se recorre la
     * matriz de ingredientes para buscar la primera poción.
     */
    @Inject(method = "onTakeItem", at = @At("TAIL"))
    private void foodPlus_onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        // Solo procesamos si el resultado es alimento.
        if (stack.getItem().getFoodComponent() == null) {
            return;
        }
        // Recorremos los ingredientes de la receta.
        for (int i = 0; i < input.size(); i++) {
            ItemStack ingredient = input.getStack(i);
            if (!ingredient.isEmpty() && ingredient.getItem() instanceof PotionItem) {
                Potion potion = PotionUtil.getPotion(ingredient);
                if (potion != Potions.EMPTY) {
                    Identifier potionId = Registries.POTION.getId(potion);
                    // Usamos getOrCreateNbt() para obtener o crear el NBT del ItemStack.
                    stack.getOrCreateNbt().putString("FoodPlusPotion", potionId.toString());
                    break; // Se procesa solo la primera poción encontrada.
                }
            }
        }
    }
}
