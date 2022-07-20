package net.F53.bookmasscraft;

import fi.dy.masa.itemscroller.recipes.CraftingHandler;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.TickHandler;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import fi.dy.masa.malilib.util.GuiUtils;
import net.F53.bookmasscraft.mixin.RecipeBookWidgetAccessor;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

import static fi.dy.masa.itemscroller.config.Hotkeys.HOTKEY_LIST;

public class RecipeBookMassCraft implements ClientModInitializer, IClientTickHandler {
	private static final KeybindSettings GUI_NO_ORDER;
	public static final ConfigHotkey RECIPE_BOOK_MASS_CRAFT;
	public static final List<ConfigHotkey> NEW_HOTKEY_LIST;

	public void onInitializeClient() {
		TickHandler.getInstance().registerClientTickHandler(this);
		InputEventHandler.getKeybindManager().registerKeybindProvider(new IKeybindProvider() {
			public void addKeysToMap(final IKeybindManager manager) {
				manager.addKeybindToMap(RecipeBookMassCraft.RECIPE_BOOK_MASS_CRAFT.getKeybind());
			}
			public void addHotkeys(final IKeybindManager manager) {
			}
		});
	}

	public void onClientTick(final MinecraftClient minecraftClient) {
		if (GuiUtils.getCurrentScreen() instanceof HandledScreen && !(GuiUtils.getCurrentScreen() instanceof CreativeInventoryScreen) && RecipeBookMassCraft.RECIPE_BOOK_MASS_CRAFT.getKeybind().isKeybindHeld()) {
			final Screen guiScreen = GuiUtils.getCurrentScreen();
			final HandledScreen<?> gui = (HandledScreen<?>)guiScreen;
			final Slot outputSlot = CraftingHandler.getFirstCraftingOutputSlotForGui(gui);
			final Screen currentScreen = GuiUtils.getCurrentScreen();
			if (currentScreen instanceof CraftingScreen craftingScreen) {
				final RecipeBookWidgetAccessor recipeBookWidget = (RecipeBookWidgetAccessor)craftingScreen.getRecipeBookWidget();
				final Recipe<?> lastClickedRecipe = recipeBookWidget.getRecipeBookResults().getLastClickedRecipe();
				if (lastClickedRecipe != null) {
					final Slot slot = CraftingHandler.getFirstCraftingOutputSlotForGui(gui);
					if (slot != null && !InventoryUtils.isStackEmpty(lastClickedRecipe.getOutput())) {
						InventoryUtils.dropStacks(gui, lastClickedRecipe.getOutput(), slot, false);
					}
					minecraftClient.interactionManager.clickRecipe(craftingScreen.getScreenHandler().syncId, lastClickedRecipe, true);
					int failsafe = 0;
					while (++failsafe < 40 && InventoryUtils.areStacksEqual(outputSlot.getStack(), lastClickedRecipe.getOutput())) {
						InventoryUtils.dropStacksWhileHasItem(gui, outputSlot.id, lastClickedRecipe.getOutput());
						InventoryUtils.tryClearCursor(gui);
						if (slot != null && !InventoryUtils.isStackEmpty(lastClickedRecipe.getOutput())) {
							InventoryUtils.dropStacks(gui, lastClickedRecipe.getOutput(), slot, false);
						}
						minecraftClient.interactionManager.clickRecipe(craftingScreen.getScreenHandler().syncId, lastClickedRecipe, true);
					}
				}
			}
		}
	}

	static {
		GUI_NO_ORDER = KeybindSettings.create(KeybindSettings.Context.GUI, KeyAction.PRESS, false, false, false, true);
		RECIPE_BOOK_MASS_CRAFT = new ConfigHotkey("recipeBookMassCraft", "LEFT_CONTROL,Q", RecipeBookMassCraft.GUI_NO_ORDER, "Select Recipe in book, press and hold Q");

		// Take hotkey list from ItemScroller, add RECIPE_BOOK_MASS_CRAFT to the last element
		NEW_HOTKEY_LIST = new ArrayList<>(HOTKEY_LIST);
		NEW_HOTKEY_LIST.add(RECIPE_BOOK_MASS_CRAFT);
	}
}
