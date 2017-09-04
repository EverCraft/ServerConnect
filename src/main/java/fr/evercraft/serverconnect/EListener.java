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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EListener implements Listener {
	private ServerConnect plugin;

	public EListener(ServerConnect plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		if (this.plugin.getServer().getOnlinePlayers().size() != 1) return;
		
		this.plugin.getMessageBungee().sendGetServers();
	}

	@EventHandler
	public void onPlayerPreCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		String command = event.getMessage().replaceFirst("/", "").toLowerCase();
		String server = this.plugin.getServer(command);
		if (server == null) return;
		
		event.setCancelled(true);
		
		if (player.isOp() || player.hasPermission(ServerConnect.PERMISSION_SERVERS + "*") || player.hasPermission(ServerConnect.PERMISSION_SERVERS + server)) {
			this.plugin.getMessageBungee().connect(player, command);
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("noPermission", "&cYou don't have permissions for that server!")));
		}
	}
}
