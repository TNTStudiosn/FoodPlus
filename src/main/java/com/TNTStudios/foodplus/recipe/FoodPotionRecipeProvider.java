package com.TNTStudios.foodplus.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public class FoodPotionRecipeProvider {
    public static void register(Consumer<RecipeJsonProvider> exporter) {
        exporter.accept(new RecipeJsonProvider() {
            @Override
            public void serialize(JsonObject json) {
                json.addProperty("type", "foodplus:food_potion_recipe");
            }

            @Override
            public Identifier getRecipeId() {
                return new Identifier("foodplus", "food_potion_recipe");
            }

            @Override
            public RecipeSerializer<?> getSerializer() {
                return FoodPotionRecipeSerializer.INSTANCE;
            }

            @Override
            public JsonObject toJson() {
                JsonObject json = new JsonObject();
                serialize(json);
                return json;
            }

            @Nullable
            @Override
            public JsonObject toAdvancementJson() {
                return null; // No se requiere un advancement para esta receta
            }

            @Nullable
            @Override
            public Identifier getAdvancementId() {
                return null; // Retorna null ya que no necesita advancements
            }
        });
    }
}
