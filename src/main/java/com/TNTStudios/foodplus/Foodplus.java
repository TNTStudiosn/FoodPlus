package com.TNTStudios.foodplus;

import com.TNTStudios.foodplus.recipe.FoodPotionRecipeSerializer;
import net.fabricmc.api.ModInitializer;

public class Foodplus implements ModInitializer {

    @Override
    public void onInitialize() {
        FoodPotionRecipeSerializer.register();
    }
}
