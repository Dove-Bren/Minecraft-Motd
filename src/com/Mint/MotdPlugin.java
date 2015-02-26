package com.Mint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Jeru
 * @version 0.1
 * 
 * A message of the day system for the Minecraft Bukkit server.
 */

public class MotdPlugin extends JavaPlugin implements Listener
{
	private static final String _defaultMessage = "Welcome to the New Mexico Tech Minecraft Server";
	private static String _message;
	private static File configFile;
	private static String configFileName = "message.yml";
	
	/**
	 * Created a version number to keep track of any changes that may be made in the future.
	 * This is great for checking compliance of configuration files. What if you want to add different MOTD to different
	 * worlds, for example?
	 */
	private static final double version = 1.0; //added version. 
	
	/**
	 * config keeps the configuration file for this plugin in memory. Configuration files are an easier alternative to
	 * plain text files because they sort data easily. In this way, you can have multiple fields and not have to do
	 * nasty scanning and token parsing to find them.
	 */
	private static YamlConfiguration config;
	
	private static boolean enable = false;
		
	/**
	 * Loads up the config and resolves the motd
	 */
	@Override
	public void onLoad() {
		

		configFile = new File(getDataFolder(), configFileName);
		if (!configFile.exists())
		{
			try
			{
				YamlConfiguration defaultConfig = new YamlConfiguration(); //create new config file. Currently empty and in memory
				defaultConfig.set("version", version);
				defaultConfig.set("message", _defaultMessage);
				
				defaultConfig.save(configFile); //save our new default config out to the file.
				
			} catch (IOException e) {
				getLogger().info("Failed to create and write to the motd file: " + e.toString());
			}
		}
	}
	
	/**
	 * Registers events.
	 */
	@Override
	public void onEnable()
	{
		
		if (!enable) {
			enable = true;
			getServer().getPluginManager().registerEvents(this, this);
		}

		
		config = new YamlConfiguration();
		
		try {
			config.load(configFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (config.getString("message", null) == null)
		{
				_message = _defaultMessage;
		}
		else {
			_message = config.getString("message");
		}
	}
	
	/**
	 * Unregisters the event
	 */
	@Override
	public void onDisable()
	{
		this.config.set("message", _message);
		try {
			config.save(configFile);
		} catch (IOException e) {
			getLogger().warning("Unable to save modified motd config file!");
			e.printStackTrace();
		}
	}
	
	public void reload() {
		onEnable();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerJoinEvent event)
	{
		//System.out.println("Player login!\nMessage: " + _message);
		event.getPlayer().sendMessage(_message);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if (cmd.getName().equalsIgnoreCase("motd")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage("Reloading MOTD...");
				getLogger().info("Reloading MOTD");
				reload();
				sender.sendMessage("Reload complete!");
				getLogger().info("Reload of MOTD complete!");
				return true;
			}
			if (args[0].equalsIgnoreCase("set")) {
				if (args.length < 2) {
					return false;
				}
				String msg = "";
				for (int i = 1; i < args.length; i++) {
					msg += args[i] + " ";
				}
				
				if (msg.trim().isEmpty()) {
					sender.sendMessage("Please supply a non-empty string!");
					return true;
				}
				
				_message = msg;
				onDisable();
				sender.sendMessage("MOTD set to: " + msg);
				return true;
			}
			
			
		}
		
		
		
		return false;
	}
}
