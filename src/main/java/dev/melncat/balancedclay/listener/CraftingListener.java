package dev.melncat.balancedclay.listener;

import dev.melncat.balancedclay.BalancedClay;
import dev.melncat.balancedclay.recipes.ClayRecipes;
import dev.melncat.balancedclay.recipes.RecipeUtil;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CraftingListener implements Listener {
	private static Material[] itemMaterials = new Material[]{};
	private static Random random = new Random();

	@EventHandler
	private void onPrepareCraft(PrepareItemCraftEvent event) {
		Recipe recipe = event.getRecipe();
		if (!(recipe instanceof Keyed) ||
			!((Keyed) recipe).getKey().getNamespace().equals(
				BalancedClay.getInstance().getName().toLowerCase(Locale.ROOT)))
			return;
		ItemStack[] matrix = event.getInventory().getMatrix();
		ClayRecipes.shapedRecipes.parallelStream().filter(x -> {
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++) {
					ItemStack item = matrix[i * 3 + j];
					if (x.getShape()[i].charAt(j) != shapeChar(item))
						return false;
				}
			return true;
		}).findAny().ifPresent(found -> {
			event.getInventory().setResult(found.getResult());
		});
	}

	private char shapeChar(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) return ' ';
		if (item.getType() == Material.CLAY_BALL) return 'C';
		if (item.getType() == Material.CLAY) return 'B';
		return '#';
	}

	@EventHandler
	private void onBlockCook(BlockCookEvent event) {
		if (itemMaterials.length == 0) reloadMaterials();
		if (event.getSource().getType() != Material.CLAY_BALL) return;
		ItemStack it = RecipeUtil.itemOf(itemMaterials[random.nextInt(itemMaterials.length)]);
		event.setResult(it);
	}

	public static void reloadMaterials() {
		itemMaterials = Arrays.stream(Material.values())
			.filter(x -> x.isItem() && x != Material.AIR && BalancedClay.getInstance().getClayConfig().materialAllowed(x))
			.toArray(Material[]::new);
	}
}
