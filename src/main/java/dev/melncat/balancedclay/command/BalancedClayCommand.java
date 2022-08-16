package dev.melncat.balancedclay.command;

import dev.melncat.balancedclay.BalancedClay;
import dev.melncat.balancedclay.config.ClayConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BalancedClayCommand implements CommandExecutor, TabCompleter {
	private static final String BLANK = "&e\u2610";
	private static final String CHECK = "&a\u2611";
	private static final String CROSS = "&c\u2612";
	private final String text = getInfoText();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("balancedclay.command.balancedclay.reload")) return false;
			BalancedClay.getInstance().reloadClayConfig();
			sender.sendMessage(ChatColor.GREEN + "The BalancedClay config has been successfully reloaded.\n" +
				ChatColor.RED + "Note: This does not delete any existing recipes. Restart the server to remove blacklisted item recipes.");
			return true;
		}
		sender.sendMessage(text);
		return true;
	}

	private String getInfoText() {
		BalancedClay plugin = BalancedClay.getInstance();
		ClayConfig config = plugin.getClayConfig();
		StringBuilder str = new StringBuilder("&eBalancedClay &fv&b");
		str.append(plugin.getDescription().getVersion())
			.append("\n&8- &fMade with clay by &aMelnCat&f!\n&8- &fCrafting: ")
			.append(getIcon(config.clayCrafting))
			.append("\n&8- &fSmelting: ")
			.append(config.claySmelting.all(true) ? CHECK : config.claySmelting.all(false) ? CROSS : BLANK);
		if (!(config.claySmelting.all(true) || config.claySmelting.all(false))) {
			str.append("\n&8   - &fFurnaces: ").append(getIcon(config.claySmelting.furnace))
				.append("\n&8   - &fBlast Furnaces: ").append(getIcon(config.claySmelting.blastFurnace))
				.append("\n&8   - &fSmokers: ").append(getIcon(config.claySmelting.smoker))
				.append("\n&8   - &fCampfires: ").append(getIcon(config.claySmelting.campfire));
		}
		str.append("\n&8- &fStonecutters: ")
			.append(getIcon(config.clayStonecutter))
			.append("\n&8- &fSmithing: ")
			.append(getIcon(config.claySmithing))
			.append("\n&8- &f").append(config.useBlacklistAsWhitelist ? "Whitelist" : "Blacklist").append(": ")
			.append(config.blacklist.stream().map(x -> "&e" + materialName(x)).collect(Collectors.joining("&f, ")));
		return ChatColor.translateAlternateColorCodes('&', str.toString());
	}

	private String getIcon(boolean bool) {
		return bool ? CHECK : CROSS;
	}

	private String materialName(Material m) {
		return Arrays.stream(m.getKey().getKey().split("_"))
			.map(x -> Character.toUpperCase(x.charAt(0)) + x.substring(1))
			.collect(Collectors.joining(" "));
	}

	private static final List<String> reloadCompletion = Collections.singletonList("reload");

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1 && sender.hasPermission("balancedclay.command.balancedclay.reload"))
			return reloadCompletion;
		return null;
	}
}
