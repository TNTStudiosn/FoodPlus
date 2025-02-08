package com.TNTStudios.foodplus.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

public class FoodPotionRecipeSerializer implements RecipeSerializer<FoodPotionRecipe> {
    public static final FoodPotionRecipeSerializer INSTANCE = new FoodPotionRecipeSerializer();
    public static final Identifier ID = new Identifier("foodplus", "food_potion_recipe");

    @Override
    public FoodPotionRecipe read(Identifier id, JsonObject json) {
        return new FoodPotionRecipe(id);
    }

    @Override
    public FoodPotionRecipe read(Identifier id, PacketByteBuf buf) {
        return new FoodPotionRecipe(id);
    }

    @Override
    public void write(PacketByteBuf buf, FoodPotionRecipe recipe) {
    }

    public static void register() {
        Registry.register(Registries.RECIPE_TYPE, FoodPotionRecipeType.ID, FoodPotionRecipeType.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, FoodPotionRecipeSerializer.ID, FoodPotionRecipeSerializer.INSTANCE);
    }
}

class FoodPotionRecipeType implements RecipeType<FoodPotionRecipe> {
    public static final FoodPotionRecipeType INSTANCE = new FoodPotionRecipeType();
    public static final Identifier ID = new Identifier("foodplus", "food_potion_recipe");
}
