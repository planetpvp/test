package fr.nc333.planetpvp;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class planetpvpConnect implements Listener
{
	VoteConfig configManager;
	Vote plugin;
	@EventHandler
	  public void connectionJoueur(PlayerJoinEvent event)
	  {
		event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + " " + configManager.getConfigu("JoinMessage").toString());
	  }
	  @EventHandler
	  public void deconnectionJoueur(PlayerQuitEvent event) 
	  {
		  event.setQuitMessage(ChatColor.YELLOW + event.getPlayer().getName() + " se déconnecte.");
	  }
}
