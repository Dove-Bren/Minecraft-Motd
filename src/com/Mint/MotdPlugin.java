package com.Mint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Jeru
 * @version 0.1
 * 
 * desc
 */

public class MotdPlugin extends JavaPlugin implements Listener
{
	private String _defaultMessage = "Welcome to the New Mexico Tech Minecraft Server";
	private String _message;
	
	/**
	 * Registers events and resolves the motd.
	 */
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		
		Path filePath = Paths.get("/res/message.txt");
		
		if (Files.notExists(filePath))
		{
			try
			{
				Files.createFile(filePath);
				Files.write(filePath, _defaultMessage.getBytes(), StandardOpenOption.WRITE);
				_message = _defaultMessage;
			} catch (IOException e) {
				getLogger().info("Failed to create and write to the motd file: " + e.toString());
			}
		}
		
		if (_message == null)
		{
			try
			{
				_message = new String(Files.readAllBytes(filePath));
			} catch (IOException e) {
				getLogger().info("Failed to read the to the motd file: " + e.toString());
			}
		}
	}
	
	/**
	 * Unregisters the event
	 */
	@Override
	public void onDisable()
	{
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event)
	{
		event.getPlayer().sendMessage(_message);
	}
}
