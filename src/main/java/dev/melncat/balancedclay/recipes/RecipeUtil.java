package dev.melncat.balancedclay.recipes;

import dev.melncat.balancedclay.BalancedClay;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;

public class RecipeUtil {
	private static int lastOverflow = 0;
	public static ShapedRecipe shapedRecipeOf(int index, ItemStack result) {
		ShapedRecipe recipe = new ShapedRecipe(
			new NamespacedKey(BalancedClay.getInstance(), "clay_shaped_" + result.getType().getKey().getKey()),
			result
		);
		String[] shape = new String[3];
		if (index < 512)
			for (int i = 0; i < 3; i++) {
				StringBuilder line = new StringBuilder();
				for (int j = 0; j < 3; j++) {
					int pos = i * 3 + j;
					line.append(
						(index & (1 << pos)) == 0 ? ' ' : 'C'
					);
				}
				shape[i] = line.toString();
			}
		else {
			outer:
			while (true) {
				int n = lastOverflow;
				while (n != 0) {
					if (n % 3 == 2) break outer;
					n /= 3;
				}
				lastOverflow++;
			}
			for (int i = 0; i < 3; i++) {
				StringBuilder line = new StringBuilder();
				for (int j = 0; j < 3; j++) {
					int pos = i * 3 + j;
					long trit = (lastOverflow / (long) Math.pow(3, pos)) % 3;
					line.append(
						trit == 0 ? ' ' : trit == 1 ? 'C' : 'B'
					);
				}
				shape[i] = line.toString();
			}
			lastOverflow++;
		}
		recipe.shape(shape);
		if (recipe.getIngredientMap().containsKey('C')) recipe.setIngredient('C', Material.CLAY_BALL);
		if (recipe.getIngredientMap().containsKey('B')) recipe.setIngredient('B', Material.CLAY);
		return recipe;
	}
	public static StonecuttingRecipe stonecutterRecipeOf(ItemStack result) {
		StonecuttingRecipe recipe = new StonecuttingRecipe(
			new NamespacedKey(BalancedClay.getInstance(), "clay_stonecutter_" + result.getType().getKey().getKey()),
			result, Material.CLAY_BALL
		);
		return recipe;
	}
	public static SmithingRecipe smithingRecipeOf(Material previous, ItemStack next) {
		SmithingRecipe recipe = new SmithingRecipe(
			new NamespacedKey(BalancedClay.getInstance(), "clay_smithing_" + next.getType().getKey().getKey()),
			next, new RecipeChoice.MaterialChoice(previous), new RecipeChoice.MaterialChoice(Material.CLAY_BALL)
		);
		return recipe;
	}

	public static ItemStack itemOf(Material m) {
		ItemStack item = new ItemStack(m);
		return item;
	}

	private RecipeUtil() {}
}
