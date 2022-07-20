package net.F53.bookmasscraft.mixin;

import fi.dy.masa.itemscroller.config.Configs;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import net.F53.bookmasscraft.RecipeBookMassCraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = { Configs.class }, remap = false)
public class ItemScrollerConfigsMixin {
	@Redirect(method = { "loadFromFile" }, at = @At(value = "FIELD", target = "Lfi/dy/masa/itemscroller/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;"))
	private static List<ConfigHotkey> moreHotkeys1() {
		return RecipeBookMassCraft.NEW_HOTKEY_LIST;
	}

	@Redirect(method = { "saveToFile" }, at = @At(value = "FIELD", target = "Lfi/dy/masa/itemscroller/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;"))
	private static List<ConfigHotkey> moreHotkeys2() {
		return RecipeBookMassCraft.NEW_HOTKEY_LIST;
	}
}
