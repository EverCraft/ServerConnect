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

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class MessageBungee implements PluginMessageListener {
	
	private static final String CHANNEL_BUNGEE = "BungeeCord";
	private static final String GET_SERVERS = "GetServers";
	private static final String CONNECT = "Connect";
	
	public ServerConnect plugin;

	public MessageBungee(ServerConnect plugin) {
		this.plugin = plugin;
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this.plugin, CHANNEL_BUNGEE);
		Bukkit.getMessenger().registerIncomingPluginChannel(this.plugin, CHANNEL_BUNGEE, this);
	}
	
	public void sendGetServers() {
		Iterator<? extends Player> players = this.plugin.getServer().getOnlinePlayers().iterator();
		if (!players.hasNext()) return;
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(GET_SERVERS);
		Player player = players.next();
		player.sendPluginMessage(this.plugin, CHANNEL_BUNGEE, out.toByteArray());
	}
	
	public void connect(Player player, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CONNECT);
		out.writeUTF(server);
		
		player.sendPluginMessage(this.plugin, CHANNEL_BUNGEE, out.toByteArray());
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals(CHANNEL_BUNGEE)) return;

		ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    
		if (!subchannel.equals(GET_SERVERS)) return;

		this.plugin.setServers(in.readUTF().split(", "));
	}
}
