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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerConnect extends JavaPlugin {
	
	public static final String PERMISSION_RELOAD = "serverconnect.reload";
	public static final String PERMISSION_SERVERS = "serverconnect.servers.";
	
	private MessageBungee messages;
	private final ConcurrentMap<String, String> servers;
	
	public ServerConnect() {
		this.servers = new ConcurrentHashMap<String, String>();
	}
	
	@Override
	public void onEnable() {
		this.initMetrics();
		this.initConfig();
		
		this.messages = new MessageBungee(this);
		this.messages.sendGetServers();
		
		this.getServer().getPluginManager().registerEvents(new EListener(this), this);
		this.getCommand("serverconnect").setExecutor(new Commands(this));
	}
	
	public void onReload() {
		if (!this.isEnabled()) return;
		
		// Configs
		this.initConfig();
		this.messages.sendGetServers();
	}
	
	public void initMetrics() {
        new Metrics(this);
	}
	
	public void initConfig() {
		this.saveDefaultConfig();
		this.reloadConfig();
		
		this.getConfig().addDefault("noPermission", "&cYou don't have permissions for that server!");
		
		this.getConfig().options().copyDefaults(true);
		this.getConfig().options().copyHeader(true);
		this.saveConfig();
		this.reloadConfig();
	}
	
	/*
	 * Getters and Setters
	 */

	public MessageBungee getMessageBungee() {
		return this.messages;
	}
	
	public void setServers(String[] servers) {
		this.servers.clear();
		
		for (String server : servers) {
			this.servers.put(server.toLowerCase(), server);
		}
	}
	
	public String getServer(String command) {
		return this.servers.get(command);
	}
}
