package dev.melncat.balancedclay.recipes;

import dev.melncat.balancedclay.BalancedClay;
import dev.melncat.balancedclay.config.ClayConfig;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class ClayRecipes {
	public static List<ShapedRecipe> shapedRecipes = new ArrayList<>();

	public static void loadRecipes() {
		ClayConfig config = getConfig();
		if (config.clayCrafting) loadCraftingRecipes();
		if (config.clayStonecutter) loadStonecuttingRecipes();
		if (config.claySmithing) loadSmithingRecipes();
		loadSmeltingRecipes();
	}
	public static void loadCraftingRecipes() {
		ClayConfig config = getConfig();
		int i = 0;
		for (Material m : Material.values()) {
			if (!isMaterialValid(m)) continue;
			ItemStack item = RecipeUtil.itemOf(m);
			if (i == 26) {
				ShapedRecipe r = RecipeUtil.shapedRecipeOf(i + 1, new ItemStack(Material.CLAY));
				Bukkit.addRecipe(r);
				shapedRecipes.add(r);
				i++;
			};
			ShapedRecipe recipe = RecipeUtil.shapedRecipeOf(i + 1, item);
			Bukkit.addRecipe(recipe);
			shapedRecipes.add(recipe);
			i++;
		}
	}
	public static void loadStonecuttingRecipes() {
		ClayConfig config = getConfig();
		for (Material m : Material.values()) {
			if (!isMaterialValid(m)) continue;
			ItemStack item = RecipeUtil.itemOf(m);
			StonecuttingRecipe recipe = RecipeUtil.stonecutterRecipeOf(item);
			Bukkit.addRecipe(recipe);
		}
	}
	public static void loadSmithingRecipes() {
		ClayConfig config = getConfig();
		Material[] materials = Arrays.stream(Material.values())
			.filter(ClayRecipes::isMaterialValid)
			.toArray(Material[]::new);
		for (int i = 0; i < materials.length; i++) {
			Material m = materials[i];
			Material n = materials[(i - 1 + materials.length) % materials.length];
			ItemStack item = RecipeUtil.itemOf(m);
			SmithingRecipe recipe = RecipeUtil.smithingRecipeOf(n, item);
			Bukkit.addRecipe(recipe);
		}
	}
	public static void loadSmeltingRecipes() {
		ClayConfig config = getConfig();
		if (config.claySmelting.furnace) Bukkit.addRecipe(new FurnaceRecipe(
			new NamespacedKey(BalancedClay.getInstance(), "clay_smelting_furnace"), new ItemStack(Material.CLAY_BALL), Material.CLAY_BALL, 10f, 200
		));
		if (config.claySmelting.blastFurnace) Bukkit.addRecipe(new BlastingRecipe(
			new NamespacedKey(BalancedClay.getInstance(), "clay_smelting_blast_furnace"), new ItemStack(Material.CLAY_BALL), Material.CLAY_BALL, 10f, 100
		));
		if (config.claySmelting.smoker) Bukkit.addRecipe(new SmokingRecipe(
			new NamespacedKey(BalancedClay.getInstance(), "clay_smelting_smoker"), new ItemStack(Material.CLAY_BALL), Material.CLAY_BALL, 10f, 100
		));
		if (config.claySmelting.campfire) Bukkit.addRecipe(new CampfireRecipe(
			new NamespacedKey(BalancedClay.getInstance(), "clay_smelting_campfire"), new ItemStack(Material.CLAY_BALL), Material.CLAY_BALL, 10f, 600
		));
	}

	private static ClayConfig getConfig() {
		return BalancedClay.getInstance().getClayConfig();
	}

	private static boolean isMaterialValid(Material m) {
		return getConfig().materialAllowed(m) && m.isItem() && m != Material.AIR;
	}
}
