package dev.melncat.balancedclay.config;

import dev.melncat.balancedclay.BalancedClay;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClayConfig {
	public final boolean clayCrafting;
	public final ClaySmeltingConfig claySmelting;
	public final boolean clayStonecutter;
	public final boolean claySmithing;
	public final Set<Material> blacklist;
	public final boolean useBlacklistAsWhitelist;


	public ClayConfig(FileConfiguration config) {
		clayCrafting = config.getBoolean("enable-clay-crafting");
		claySmelting = new ClaySmeltingConfig(
			Objects.requireNonNull(config.getConfigurationSection("enable-clay-smelting"))
		);
		clayStonecutter = config.getBoolean("enable-clay-stonecutter");
		claySmithing = config.getBoolean("enable-clay-smithing");
		blacklist = config.getStringList("blacklist").stream().map(x -> {
			Material m = Material.matchMaterial(x);
			Objects.requireNonNull(m, x + " is not a valid material. (in ClayConfig blacklist)");
			return m;
		}).collect(Collectors.toSet());
		useBlacklistAsWhitelist = config.getBoolean("use-blacklist-as-whitelist");

	}

	public boolean materialAllowed(Material mat) {
		return useBlacklistAsWhitelist == blacklist.contains(mat);
	}

	public static class ClaySmeltingConfig {
		public final boolean furnace;
		public final boolean smoker;
		public final boolean blastFurnace;
		public final boolean campfire;

		public boolean all(boolean bool) {
			return furnace == bool && smoker == bool && blastFurnace == bool && campfire == bool;
		}

		private ClaySmeltingConfig(ConfigurationSection config) {
			furnace = config.getBoolean("furnace");
			smoker = config.getBoolean("smoker");
			blastFurnace = config.getBoolean("blast-furnace");
			campfire = config.getBoolean("campfire");
		}
	}
}
