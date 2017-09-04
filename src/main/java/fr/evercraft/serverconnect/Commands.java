/*
 * This file is part of ServerConnect.
 *
 * ServerConnect is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ServerConnect is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ServerConnect.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.evercraft.serverconnect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class Commands implements CommandExecutor, TabCompleter {
	
	private final ServerConnect plugin;

	public Commands(ServerConnect plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmds, String commandLabel, String[] args) {
		if (!this.plugin.isEnabled()) {
			sender.sendMessage(ChatColor.RED + "---------- [ServerConnect v" + this.plugin.getDescription().getVersion() + " : By rexbut] ----------");
			sender.sendMessage(ChatColor.RED + "Plugin : Disable");
			return true;
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if (sender.hasPermission(ServerConnect.PERMISSION_RELOAD)) {
				this.plugin.onReload();
				sender.sendMessage(ChatColor.GREEN + "[ServerConnect] Reloaded");
			} else {
				sender.sendMessage(ChatColor.RED + "[ServerConnect] You don't have permission !");
			}
			return true;
		} else {
			sender.sendMessage(ChatColor.GREEN + "---------- [ServerConnect v" + this.plugin.getDescription().getVersion() + " : By rexbut] ----------");
			sender.sendMessage(ChatColor.GREEN + "/serverconnect help : Help plugin");
			if (sender.hasPermission(ServerConnect.PERMISSION_RELOAD)) {
				sender.sendMessage(ChatColor.GREEN + "/serverconnect reload : Reload plugin");
			}
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> suggests = new ArrayList<String>();
		if(alias.equalsIgnoreCase("serverconnect") && args.length <= 1) {
			suggests.add("help");
			if (sender.hasPermission(ServerConnect.PERMISSION_RELOAD)) {
				suggests.add("reload");
			}
		}
		return suggests;
	}
}
