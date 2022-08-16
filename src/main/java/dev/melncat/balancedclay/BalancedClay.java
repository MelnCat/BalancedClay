package dev.melncat.balancedclay;

import dev.melncat.balancedclay.command.BalancedClayCommand;
import dev.melncat.balancedclay.config.ClayConfig;
import dev.melncat.balancedclay.listener.CraftingListener;
import dev.melncat.balancedclay.recipes.ClayRecipes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class BalancedClay extends JavaPlugin {
	private static BalancedClay INSTANCE;
	private ClayConfig config;
	private BalancedClayCommand balancedClayCommand;

	public static BalancedClay getInstance() {
		return INSTANCE;
	}

	public ClayConfig getClayConfig() {
		return config;
	}

	@Override
	public void onEnable() {
		INSTANCE = this;
		saveDefaultConfig();
		reloadClayConfig();
		ClayRecipes.loadRecipes();
		Bukkit.getPluginManager().registerEvents(new CraftingListener(), this);
	}

	@Override
	public void onDisable() {

	}

	public void reloadClayConfig() {
		reloadConfig();
		config = new ClayConfig(getConfig());
		balancedClayCommand = new BalancedClayCommand();
		setCommand("balancedclay", balancedClayCommand);
		CraftingListener.reloadMaterials();
	}

	private void setCommand(String name, CommandExecutor executor) {
		PluginCommand command = Objects.requireNonNull(getCommand(name));
		command.setExecutor(executor);
		if (executor instanceof TabCompleter) command.setTabCompleter((TabCompleter) executor);
	}
}
