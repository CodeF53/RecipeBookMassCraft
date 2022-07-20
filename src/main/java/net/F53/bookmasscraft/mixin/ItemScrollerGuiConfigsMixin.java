package net.F53.bookmasscraft.mixin;

import fi.dy.masa.itemscroller.gui.GuiConfigs;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import net.F53.bookmasscraft.RecipeBookMassCraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;


@Mixin(value = { GuiConfigs.class }, remap = false)
public class ItemScrollerGuiConfigsMixin {
    @Redirect(method = { "getConfigs" }, at = @At(value = "FIELD", target = "Lfi/dy/masa/itemscroller/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;"))
    private List<ConfigHotkey> moreHotkeys() {
        return RecipeBookMassCraft.NEW_HOTKEY_LIST;
    }
}
