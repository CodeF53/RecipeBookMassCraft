package net.F53.bookmasscraft.mixin;

import net.minecraft.client.gui.screen.recipebook.RecipeBookResults;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ RecipeBookWidget.class })
public interface RecipeBookWidgetAccessor {
    @Accessor("recipesArea")
    RecipeBookResults getRecipeBookResults();
}
