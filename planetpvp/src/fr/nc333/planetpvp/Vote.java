package fr.nc333.planetpvp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;




public class Vote extends JavaPlugin{
	
	
	VoteConfig configManager;
	public final planetpvpConnect event = new planetpvpConnect();
	// CONFIG
	public static String index = "§1[§eEpicEnchant§1]§6";
	public static String tabliczka = "§6[EpicEnchant]";
	public static String color_yellow = "§e";
	public static String color_red = "§c";
	public static String color_gold = "§6";
	public static String color_green = "§2";
	public static Economy econ = null;
	public static int basecost = 100;
	public static double breakmultiplier = 1.0D;
	public List<Integer> toolsList = Arrays.asList(new Integer[] { Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35) });
	public List<Integer> armorList = Arrays.asList(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6) });
	public List<Integer> bowList = Arrays.asList(new Integer[] { Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51) });
	  
	String hote;
	String user;
	String mdp;
	Connection conn;
	String vote = "";
	String vote1;
	String voteoui;
	String voteoui1;
	String votenon;
	String votenon1;
	String voteend;
	String votetest;
	String nbs;
	String voter;
	String tapervoter;
	String taperact;
	String dejavoter;
	String voterok;
	String JoinMessage;

	
	boolean avote = false;
	public int task;
	
	public void connect(){
		try {
			conn = DriverManager.getConnection(hote, user, mdp);
			System.out.println("[Vote]: Connexion établie a MYSQL");
		} catch (SQLException e) {
			System.out.println("[Vote]: erreur de connexion a MYSQL");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	@Override
	public void onEnable() {
		
		configManager = new VoteConfig(this, "/config.yml");
		configManager.LoadConfig();
		if(configManager.getConfigu("hote") == null){
			configManager.addConfig("hote", "jdbc:mysql://localhost:3306/MyBase");
		}
		if(configManager.getConfigu("user") == null){
			configManager.addConfig("user", "root");
		}
		if(configManager.getConfigu("mdp") == null){
			configManager.addConfig("mdp", "");
		}
		if(configManager.getConfigu("voter") == null){
			configManager.addConfig("voter", "N'oubliez-pas de voter ! le vote actuelle est");
		}
		if(configManager.getConfigu("tapervoter") == null){
			configManager.addConfig("tapervoter", "tapez /vote pour voté !");
		}
		if(configManager.getConfigu("taperact") == null){
			configManager.addConfig("taperact", "tapez /vote pour voté !");
		}
		if(configManager.getConfigu("dejavoter") == null){
			configManager.addConfig("dejavoter", "Vous avez déjà voté!");
		}
		if(configManager.getConfigu("voterok") == null){
			configManager.addConfig("voterok", "Votre vote a bien été pris en compte.");
		}
		if(configManager.getConfigu("JoinPlayerColor") == null){
			configManager.addConfig("JoinPlayerColor", "aqua");
		}
		if(configManager.getConfigu("JoinMessageColor") == null){
			configManager.addConfig("JoinMessageColor", "yellow");
		}
		if(configManager.getConfigu("JoinMessage") == null){
			configManager.addConfig("JoinMessage", "c'est Connecter.");
		}
		
		
		JoinMessage = configManager.getConfigu("JoinMessage").toString();
		hote = configManager.getConfigu("hote").toString();
        user = configManager.getConfigu("user").toString();
        mdp = configManager.getConfigu("mdp").toString();
        voter = configManager.getConfigu("voter").toString();
        tapervoter = configManager.getConfigu("tapervoter").toString();
        taperact = configManager.getConfigu("taperact").toString();
        dejavoter = configManager.getConfigu("dejavoter").toString();
        voterok = configManager.getConfigu("voterok").toString();
        

	    connect();
		task = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{

			@Override
			public void run() {
				Statement state;
				try {
					state = conn.createStatement();
					ResultSet result = state.executeQuery("SELECT * FROM vote_option WHERE id='1'");
					while(result.next()) {
						vote = result.getString("nom");
						voteoui = result.getString("voteoui");
						votenon = result.getString("votenon");
						voteend = result.getString("end");
						}
					try {
						vote1 = new String (vote.getBytes(), "UTF-8");
						voteoui1 = new String (voteoui.getBytes(), "UTF-8");
						votenon1 = new String (votenon.getBytes(), "UTF-8");
						
					} catch (UnsupportedEncodingException e) {

						e.printStackTrace();
					}
					if(voteend != "1")
					{
					Vote.this.getServer().broadcastMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+ voter +" \" "+ChatColor.BOLD+vote1+ChatColor.RESET+ChatColor.AQUA+" \"");
					Vote.this.getServer().broadcastMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+ " " + tapervoter);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
				}
			
		}, 6000, 6000) ;
	System.out.println("[Vote]: Plugin vote propose par nc333, http://nc333.eu/");
	getServer().getPluginManager().registerEvents(this.event, this);
	}
	



	@Override
	public void onDisable() {
		try {
			configManager.saveConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if (cmd.getLabel().equalsIgnoreCase("epicenchant"))
	    {
			Player player = (Player)sender;
	      if (args.length == 0) {
	        player.sendMessage(index + "Use: /epicenchant <enchantment_name,all,removeall,list> [level]");
	        return true;
	      }
	      if (args.length == 1) {
	        if ((args[0].equalsIgnoreCase("all")) && ((player.isOp()) || (player.hasPermission("epicenchant.command.enchantall")) || (player.hasPermission("epicenchant.command.*")))) {
	          ItemStack item = player.getItemInHand();
	          String itemtype = ETools.canEnchant(item.getTypeId());
	          if (itemtype.equalsIgnoreCase("unknow")) {
	            player.sendMessage(index + "Can not enchant this item");
	            return true;
	          }
	          if (itemtype.equalsIgnoreCase("bow")) {
	            item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 127);
	            item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 127);
	            item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 127);
	            item.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 127);
	            player.sendMessage(index + "Bow enchanted");
	            return true;
	          }
	          if (itemtype.equalsIgnoreCase("armor")) {
	            item.addUnsafeEnchantment(Enchantment.OXYGEN, 127);
	            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 127);
	            item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 127);
	            item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 127);
	            item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 127);
	            item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 127);
	            item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 127);
	            player.sendMessage(index + "Part of armor enchanted");
	            return true;
	          }
	          if (itemtype.equalsIgnoreCase("tool")) {
	            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 127);
	            item.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 127);
	            item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 127);
	            item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 127);
	            item.addUnsafeEnchantment(Enchantment.DURABILITY, 127);
	            item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 127);
	            item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 127);
	            item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 127);
	            item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 127);
	            item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 127);
	            player.sendMessage(index + "Tool enchanted");
	            return true;
	          }
	        } else {
	          if ((args[0].equalsIgnoreCase("removeall")) && ((player.isOp()) || (player.hasPermission("epicenchant.command.removeall")) || (player.hasPermission("epicenchant.command.*"))))
	          {
	            ItemStack item = player.getItemInHand();
	            String itemtype = ETools.canEnchant(item.getTypeId());
	            if (itemtype.equalsIgnoreCase("unknow")) {
	              player.sendMessage(index + "Can not enchant this item");
	              return true;
	            }

	            if (item.containsEnchantment(Enchantment.ARROW_DAMAGE)) item.removeEnchantment(Enchantment.ARROW_DAMAGE);
	            if (item.containsEnchantment(Enchantment.ARROW_FIRE)) item.removeEnchantment(Enchantment.ARROW_FIRE);
	            if (item.containsEnchantment(Enchantment.ARROW_INFINITE)) item.removeEnchantment(Enchantment.ARROW_INFINITE);
	            if (item.containsEnchantment(Enchantment.ARROW_KNOCKBACK)) item.removeEnchantment(Enchantment.ARROW_KNOCKBACK);

	            if (item.containsEnchantment(Enchantment.OXYGEN)) item.removeEnchantment(Enchantment.OXYGEN);
	            if (item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) item.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
	            if (item.containsEnchantment(Enchantment.PROTECTION_EXPLOSIONS)) item.removeEnchantment(Enchantment.PROTECTION_EXPLOSIONS);
	            if (item.containsEnchantment(Enchantment.PROTECTION_PROJECTILE)) item.removeEnchantment(Enchantment.PROTECTION_PROJECTILE);
	            if (item.containsEnchantment(Enchantment.PROTECTION_FALL)) item.removeEnchantment(Enchantment.PROTECTION_FALL);
	            if (item.containsEnchantment(Enchantment.PROTECTION_FIRE)) item.removeEnchantment(Enchantment.PROTECTION_FIRE);
	            if (item.containsEnchantment(Enchantment.WATER_WORKER)) item.removeEnchantment(Enchantment.WATER_WORKER);

	            if (item.containsEnchantment(Enchantment.DAMAGE_ALL)) item.removeEnchantment(Enchantment.DAMAGE_ALL);
	            if (item.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) item.removeEnchantment(Enchantment.DAMAGE_ARTHROPODS);
	            if (item.containsEnchantment(Enchantment.DAMAGE_UNDEAD)) item.removeEnchantment(Enchantment.DAMAGE_UNDEAD);
	            if (item.containsEnchantment(Enchantment.DIG_SPEED)) item.removeEnchantment(Enchantment.DIG_SPEED);
	            if (item.containsEnchantment(Enchantment.DURABILITY)) item.removeEnchantment(Enchantment.DURABILITY);
	            if (item.containsEnchantment(Enchantment.FIRE_ASPECT)) item.removeEnchantment(Enchantment.FIRE_ASPECT);
	            if (item.containsEnchantment(Enchantment.KNOCKBACK)) item.removeEnchantment(Enchantment.KNOCKBACK);
	            if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) item.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
	            if (item.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) item.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
	            if (item.containsEnchantment(Enchantment.SILK_TOUCH)) item.removeEnchantment(Enchantment.SILK_TOUCH);

	            player.sendMessage(index + "All enchantments has been removed");
	            return true;
	          }

	          if ((args[0].equalsIgnoreCase("list")) && ((player.isOp()) || (player.hasPermission("epicenchant.command.list")) || (player.hasPermission("epicenchant.command.*")))) {
	            player.sendMessage(index + "List of Enchantments:");
	            player.sendMessage(index + "BOW: DAMAGE, FLAME, PUNCH, INFINITY");
	            player.sendMessage(index + "TOOL: EFFICIENCY, UNBREAKING, FORTUNE, SILKTOUCH, LOOTING, FIREASPECT, KNOCKBACK, VSARTHROPODS, VSUNDEAD, SHARPNESS");
	            player.sendMessage(index + "ARMOR: RESPIRATION, AQUAAFFINITY, PROJECTILE, FALLING, PROTECTION, BLAST, FIREPROTECTION");
	            return true;
	          }
	          if ((player.isOp()) || (player.hasPermission("epicenchant.command.enchant")) || (player.hasPermission("epicenchant.command.*"))) {
	            ItemStack item = player.getItemInHand();
	            String itemtype = ETools.canEnchant(item.getTypeId());
	            if (itemtype.equalsIgnoreCase("unknow")) {
	              player.sendMessage(index + "Can not enchant this item");
	              return true;
	            }
	            int ench = ETools.getEnchantIdByName(args[0]);
	            if (ench == -1) {
	              player.sendMessage(index + "Wrong enchantment name");
	              return true;
	            }

	            player.getItemInHand().addUnsafeEnchantment(Enchantment.getById(ench), 127);
	            player.sendMessage(index + "Enchantment added");
	            return true;
	          }
	        }

	      }
	      else if ((args.length == 2) && ((player.isOp()) || (player.hasPermission("epicenchant.command.enchant")) || (player.hasPermission("epicenchant.command.*"))))
	      {
	        ItemStack item = player.getItemInHand();
	        String itemtype = ETools.canEnchant(item.getTypeId());
	        if (itemtype.equalsIgnoreCase("unknow")) {
	          player.sendMessage(index + "Can not enchant this item");
	          return true;
	        }

	        int ench = ETools.getEnchantIdByName(args[0]);
	        if (ench == -1) {
	          player.sendMessage(index + "Wrong enchantment name");
	          return true;
	        }

	        int lvl = 1;
	        if (ETools.isInteger(args[1])) {
	          lvl = Integer.parseInt(args[1]);
	          if (lvl > 127) lvl = 127;
	          else if (lvl < 0) lvl = 0; 
	        }
	        else { lvl = 127; }

	        if (lvl == 0) {
	          player.getItemInHand().removeEnchantment(Enchantment.getById(ench));
	          player.sendMessage(index + "Enchantment removed");
	          return true;
	        }

	        player.getItemInHand().addUnsafeEnchantment(Enchantment.getById(ench), lvl);
	        player.sendMessage(index + "Enchantment added");
	        return true;
	      }

	      player.sendMessage(index + "Use: /epicenchant <enchantment_name,all,removeall,list> [level]");
	      return true;
	    }
		
		// ---------------------------------- VOTE ----------------------------------
		
		if(cmd.getName().equalsIgnoreCase("voteinfo"))
		{
		
			try {
				Statement state = conn.createStatement();
				ResultSet result = state.executeQuery("SELECT * FROM vote_option");
				while(result.next()) {
					vote = result.getString("nom");
					voteoui = result.getString("voteoui");
					votenon = result.getString("votenon");
					voteend = result.getString("end");
				}
				try {
					vote1 = new String (vote.getBytes(), "UTF-8");
					voteoui1 = new String (voteoui.getBytes(), "UTF-8");
					votenon1 = new String (votenon.getBytes(), "UTF-8");
					if(voteend != "1"){
					sender.sendMessage(ChatColor.GOLD+"[Vote]:"+ChatColor.RESET+ChatColor.AQUA+ taperact +" \""+ChatColor.BOLD+vote1+ChatColor.RESET+ChatColor.AQUA+"\"");
					ResultSet result1 = state.executeQuery("SELECT * FROM vote WHERE user='"+sender.getName()+"'");
					while(result1.next()) {
					    if(result1.getString("user").equalsIgnoreCase(sender.getName())){
					    	sender.sendMessage(ChatColor.GOLD+"[Vote]:"+ChatColor.RESET+ChatColor.AQUA+" " + dejavoter);
					    }else{
					    sender.sendMessage(ChatColor.GOLD+"[Vote]:"+ChatColor.RESET+ChatColor.AQUA+" tapez /voteoui pour voté \""+voteoui1+"\" ou /votenon pour voté \""+votenon1+"\"");
					    }
					}
					}
					}
					    
				 catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
			} catch (SQLException e) { 
				e.printStackTrace();
			}
		}
		
		// ---------------------------------- VOTE OUI ----------------------------------
		
		if(cmd.getName().equalsIgnoreCase("voteoui"))
		{
			Statement state;
			/*Player player = null;
			if (sender instanceof Player) { 
				player = (Player) sender;
			} */
			try {
				state = conn.createStatement();
				ResultSet result = state.executeQuery("SELECT * FROM vote_option");
				while(result.next()) {
					voteend = result.getString("end");
				}
				if(voteend != "1")
				{
				ResultSet result1 = state.executeQuery("SELECT COUNT(*) AS nb FROM vote WHERE user='"+sender.getName()+"'");
				while(result1.next()) {
					
					nbs = result1.getString("nb");
					if(nbs.contains("0"))
					{
						state.executeUpdate("INSERT INTO vote (user, vote) VALUES ('"+sender.getName()+"', 1)");
				    	sender.sendMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+ voterok);
					}else{
						
						sender.sendMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+" "+ dejavoter);
					}

			    	
				    
				}
				}
				}

			catch (SQLException e) {
				e.printStackTrace();
			}
			
		} 
		
		// ---------------------------------- VOTE NON ----------------------------------
		
		if(cmd.getName().equalsIgnoreCase("votenon"))
		{
			Statement state;
			/*Player player = null;
			if (sender instanceof Player) { 
				player = (Player) sender;
			} */
			try {
				state = conn.createStatement();
				ResultSet result = state.executeQuery("SELECT * FROM vote_option");
				while(result.next()) {
					voteend = result.getString("end");
				}
				if(voteend != "1")
				{
				ResultSet result1 = state.executeQuery("SELECT COUNT(*) AS nb FROM vote WHERE user='"+sender.getName()+"'");
				while(result1.next()) {
					
					nbs = result1.getString("nb");
					if(nbs.contains("0"))
					{
						state.executeUpdate("INSERT INTO vote(user, vote) VALUES ('"+sender.getName()+"', 0)");
				    	sender.sendMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+ voterok);
					}else{
						
						sender.sendMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+" "+ dejavoter);
					}
				}
				}
				}

			catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		} 
		
		if(cmd.getName().equalsIgnoreCase("votetest"))
		{
			Statement state;
			/*Player player = null;
			if (sender instanceof Player) { 
				player = (Player) sender;
			} */
			try {
				state = conn.createStatement();
				ResultSet result = state.executeQuery("SELECT * FROM vote_option");
				while(result.next()) {
					voteend = result.getString("end");
				}
				if(voteend != "1")
				{
					ResultSet result1 = state.executeQuery("SELECT COUNT(*) AS nb FROM vote WHERE user='"+sender.getName()+"'");
					while(result1.next()) {
						nbs = result1.getString("nb");
						if(nbs.contains("0"))
						{
							sender.sendMessage("message NULL");
						}else{
							
							sender.sendMessage(nbs);
						}
					}
					
				}
				}

			catch (SQLException e) {
				e.printStackTrace();
			}
			
		} 
		
		
		// ---------------------------------- VOTE ----------------------------------
		
		
		if(cmd.getName().equalsIgnoreCase("vote"))
		{
			// si il tape que /vote
			if(args.length == 0)
			{
				
				try {
					Statement state = conn.createStatement();
					ResultSet result = state.executeQuery("SELECT * FROM vote_option");
					while(result.next()) {
						vote = result.getString("nom");
						voteoui = result.getString("voteoui");
						votenon = result.getString("votenon");
						voteend = result.getString("end");
					}
					try {
						vote1 = new String (vote.getBytes(), "UTF-8");
						voteoui1 = new String (voteoui.getBytes(), "UTF-8");
						votenon1 = new String (votenon.getBytes(), "UTF-8");
						if(voteend != "1"){
						sender.sendMessage(ChatColor.GOLD+"[Vote]:"+ChatColor.RESET+ChatColor.AQUA+ taperact +" \""+ChatColor.BOLD+vote1+ChatColor.RESET+ChatColor.AQUA+"\"");
						ResultSet result1 = state.executeQuery("SELECT * FROM vote WHERE user='"+sender.getName()+"'");
						while(result1.next()) {
						    if(result1.getString("user").equalsIgnoreCase(sender.getName())){
						    	sender.sendMessage(ChatColor.GOLD+"[Vote]:"+ChatColor.RESET+ChatColor.AQUA+" " + dejavoter);
						    }else{
						    sender.sendMessage(ChatColor.GOLD+"[Vote]:"+ChatColor.RESET+ChatColor.AQUA+" tapez /voteoui pour voté \""+voteoui1+"\" ou /votenon pour voté \""+votenon1+"\"");
						    }
						}
						}
						}
						    
					 catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
				} catch (SQLException e) { 
					e.printStackTrace();
				}
				return true;
			}// si il tape "/vote oui" ou "/vote non"
			else if(args.length == 1)
			{	// la il tape "/vote oui"
				if(args[0].equalsIgnoreCase("oui"))
				{
					Statement state;
					/*Player player = null;
					if (sender instanceof Player) { 
						player = (Player) sender;
					} */
					try {
						state = conn.createStatement();
						ResultSet result = state.executeQuery("SELECT * FROM vote_option");
						while(result.next()) {
							voteend = result.getString("end");
						}
						if(voteend != "1")
						{
						ResultSet result1 = state.executeQuery("SELECT COUNT(*) AS nb FROM vote WHERE user='"+sender.getName()+"'");
						while(result1.next()) {
							
							nbs = result1.getString("nb");
							if(nbs.contains("0"))
							{
								state.executeUpdate("INSERT INTO vote (user, vote) VALUES ('"+sender.getName()+"', 1)");
						    	sender.sendMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+ voterok);
							}else{
								
								sender.sendMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+ dejavoter);
							}

					    	
						    
						}
						}
						}

					catch (SQLException e) {
						e.printStackTrace();
					}
					return true;
				}	// la il tape "/vote non"
				if(args[0].equalsIgnoreCase("non"))
				{
					Statement state;
					/*Player player = null;
					if (sender instanceof Player) { 
						player = (Player) sender;
					} */
					try {
						state = conn.createStatement();
						ResultSet result = state.executeQuery("SELECT * FROM vote_option");
						while(result.next()) {
							voteend = result.getString("end");
						}
						if(voteend != "1")
						{
						ResultSet result1 = state.executeQuery("SELECT COUNT(*) AS nb FROM vote WHERE user='"+sender.getName()+"'");
						while(result1.next()) {
							
							nbs = result1.getString("nb");
							if(nbs.contains("0"))
							{
								state.executeUpdate("INSERT INTO vote(user, vote) VALUES ('"+sender.getName()+"', 0)");
						    	sender.sendMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+ voterok);
							}else{
								
								sender.sendMessage(ChatColor.GOLD+"[Vote]: "+ChatColor.RESET+ChatColor.AQUA+" "+ dejavoter);
							}
						}
						}
						}

					catch (SQLException e) {
						e.printStackTrace();
					}
					return true;
				}
			}
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return true;
	}
}