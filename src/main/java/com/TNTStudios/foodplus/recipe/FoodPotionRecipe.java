package com.TNTStudios.foodplus.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FoodPotionRecipe implements Recipe<RecipeInputInventory> {

    private final Identifier id;

    public FoodPotionRecipe(Identifier id) {
        this.id = id;
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        ItemStack baseItem = ItemStack.EMPTY;
        ItemStack potionItem = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof PotionItem) {
                    if (potionItem.isEmpty()) {
                        potionItem = stack;
                    } else {
                        return false; // Solo se permite una poción
                    }
                } else {
                    if (baseItem.isEmpty()) {
                        baseItem = stack;
                    } else {
                        return false; // Solo se permite un item base
                    }
                }
            }
        }
        return !baseItem.isEmpty() && !potionItem.isEmpty();
    }


    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack baseItem = ItemStack.EMPTY;
        ItemStack potionItem = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof PotionItem) {
                    potionItem = stack;
                } else {
                    baseItem = stack;
                }
            }
        }

        if (!baseItem.isEmpty() && !potionItem.isEmpty()) {
            // Se hace una copia del item base para no modificar el original
            ItemStack result = baseItem.copy();
            result.setCount(1);  // Asegurarse de que el resultado sea un solo item

            // Se aplica el efecto de la poción al item resultante.
            // Esto añade la etiqueta NBT necesaria, de modo que en el mixin, al consumir
            // el item, se extraigan los efectos.
            PotionUtil.setPotion(result, PotionUtil.getPotion(potionItem));
            return result;
        }
        return ItemStack.EMPTY;
    }


    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY; // Fabric ahora requiere un output dinámico
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FoodPotionRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return FoodPotionRecipeType.INSTANCE;
    }

    @Override
    public Identifier getId() {
        return id;
    }
}
