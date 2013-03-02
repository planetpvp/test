package fr.nc333.planetpvp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener
  implements Listener
{
  public static Vote plugin;

  public PlayerListener(Vote main)
  {
    plugin = main;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if (event.getClickedBlock() == null) {
      return;
    }

    Block block = event.getClickedBlock();
    BlockState state = block.getState();

    if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && ((state instanceof Sign)))
    {
      Sign sign = (Sign)block.getState();
      if ((sign.getLine(0).equalsIgnoreCase(Vote.tabliczka)) && (event.getPlayer().getInventory().getItemInHand() != null))
      {
        if (!event.getPlayer().hasPermission("epicenchant.use")) {
          event.getPlayer().sendMessage(Vote.index + " You do not have permission to use.");
          return;
        }

        if (!sign.getLine(1).toLowerCase().startsWith("random"))
        {
          int ItemId = event.getPlayer().getInventory().getItemInHand().getTypeId();
          Enchantment ench = Enchantment.getById(ETools.getEnchantIdByName(sign.getLine(1).toLowerCase()));
          int enchantid = ETools.getEnchantIdByName(sign.getLine(1).toLowerCase());

          if (ETools.canEnchant(ItemId).equals("unknow")) {
            event.getPlayer().sendMessage(Vote.index + " You can not enchant this item");
            return;
          }
          if ((ETools.canEnchant(ItemId).equals("tool")) && (!plugin.toolsList.contains(Integer.valueOf(enchantid)))) {
            event.getPlayer().sendMessage(Vote.index + " You can not enchant this item - Wrong Enchant for this tool.");
            return;
          }
          if ((ETools.canEnchant(ItemId).equals("bow")) && (!plugin.bowList.contains(Integer.valueOf(enchantid)))) {
            event.getPlayer().sendMessage(Vote.index + " You can not enchant this item - Wrong Enchant for bow.");
            return;
          }
          if ((ETools.canEnchant(ItemId).equals("armor")) && (!plugin.armorList.contains(Integer.valueOf(enchantid)))) {
            event.getPlayer().sendMessage(Vote.index + " You can not enchant this item - Wrong Enchant for this part of armor.");
            return;
          }

          int bcost = 0;
          int amount = 0;
          int itemid = 0;
          int subid = 0;
          short data = 0;
          ItemStack is = null;

          if (!sign.getLine(2).contains("-")) {
            bcost = Integer.parseInt(sign.getLine(2));
          }
          else {
            int pos1 = sign.getLine(2).indexOf("-");
            int pos2 = sign.getLine(2).indexOf("=");
            int pos3 = sign.getLine(2).indexOf(":");
            bcost = Integer.parseInt(sign.getLine(2).substring(0, pos1));
            amount = Integer.parseInt(sign.getLine(2).substring(pos1 + 1, pos2));
            itemid = Integer.parseInt(sign.getLine(2).substring(pos2 + 1, pos3));
            subid = Integer.parseInt(sign.getLine(2).substring(pos3 + 1));
            data = (short)subid;

            is = new ItemStack(itemid, amount, data);
          }
          int currlvl = event.getPlayer().getInventory().getItemInHand().getEnchantmentLevel(ench);

          if ((sign.getLine(3).equalsIgnoreCase("reverse")) || (sign.getLine(3).equalsIgnoreCase("max:reverse")))
          {
            reverse(event, sign, ench, currlvl, bcost, amount, itemid, data, is);
          }
          else
          {
            enchant(event, sign, ench, currlvl, bcost, amount, itemid, data, is);
          }

        }
        else
        {
          random_enchant(event, sign);
        }
      }
    }
  }

  private void reverse(PlayerInteractEvent event, Sign sign, Enchantment ench, int currlvl, int bcost, int amount, int itemid, short subid, ItemStack is)
  {
    if (currlvl <= 0) {
      event.getPlayer().sendMessage(Vote.index + " You can not reverse this enchant.");
      return;
    }
    boolean max = false;
    if (sign.getLine(3).equalsIgnoreCase("max:reverse")) {
      max = true;
    }

    double cost = currlvl * bcost;
    int reversed = currlvl - 1;
    double balance = Vote.econ.getBalance(event.getPlayer().getName());
    int ilosc = currlvl * amount;
    ItemStack costItem = new ItemStack(itemid, ilosc, subid);

    if (max) {
      cost = bcost;
      reversed = 0;
      costItem = is;
    }

    if (event.getPlayer().hasPermission("epicenchant.admin")) cost = 0.0D;
    else if (event.getPlayer().hasPermission("epicenchant.epicvip")) cost *= 0.25D;
    else if (event.getPlayer().hasPermission("epicenchant.vip")) cost *= 0.5D;
    else if (event.getPlayer().hasPermission("epicenchant.minivip")) cost *= 0.75D;

    cost = ETools.round(cost);

    if (!event.getPlayer().isSneaking()) {
      event.getPlayer().sendMessage(Vote.index + " You will get back " + cost + " money.");
      return;
    }

    if ((currlvl == 1) || (max)) event.getPlayer().getInventory().getItemInHand().removeEnchantment(ench); else
      event.getPlayer().getInventory().getItemInHand().addUnsafeEnchantment(ench, reversed);
    Vote.econ.depositPlayer(event.getPlayer().getName(), cost);
    event.getPlayer().getInventory().addItem(new ItemStack[] { costItem });
    event.getPlayer().sendMessage(Vote.index + " Successfully reversed " + sign.getLine(1) + " to " + Vote.color_green + reversed);
    event.getPlayer().sendMessage(Vote.index + " Balance changed from: " + ETools.round(balance) + " --> " + Vote.color_red + "[" + ETools.round(Vote.econ.getBalance(event.getPlayer().getName())) + "]" + Vote.color_gold + " (+" + cost + ")");
  }

  private void enchant(PlayerInteractEvent event, Sign sign, Enchantment ench, int currlvl, int bcost, int amount, int itemid, short subid, ItemStack is)
  {
    int max = 0;
    if (sign.getLine(3).toLowerCase().startsWith("max:")) {
      max = Integer.parseInt(sign.getLine(3).substring(4));
    }

    double cost = (currlvl + 1) * bcost;
    double balance = Vote.econ.getBalance(event.getPlayer().getName());
    int maxenchant = 127;
    int ilosc = (currlvl + 1) * amount;
    ItemStack costItem = new ItemStack(itemid, ilosc, subid);

    if (max > 0) {
      cost = bcost;
      maxenchant = max;
      costItem = is;
    } else {
      maxenchant = Integer.parseInt(sign.getLine(3));
    }
    if (event.getPlayer().hasPermission("epicenchant.admin")) cost = 0.0D;
    else if (event.getPlayer().hasPermission("epicenchant.epicvip")) cost *= 0.25D;
    else if (event.getPlayer().hasPermission("epicenchant.vip")) cost *= 0.5D;
    else if (event.getPlayer().hasPermission("epicenchant.minivip")) cost *= 0.75D;

    cost = ETools.round(cost);
    double pech = Vote.basecost - currlvl * Vote.breakmultiplier;

    if (event.getPlayer().hasPermission("epicenchant.downgrade")) {
      if (event.getPlayer().hasPermission("epicenchant.admin")) pech = 100.0D;
      else if (event.getPlayer().hasPermission("epicenchant.epicvip")) pech *= 1.3D;
      else if (event.getPlayer().hasPermission("epicenchant.vip")) pech *= 1.2D;
      else if (event.getPlayer().hasPermission("epicenchant.minivip")) pech *= 1.1D;
      pech = ETools.round(pech);
      if (pech > 100.0D) pech = 100.0D;
      else if (pech < 1.0D) pech = 1.0D;

    }

    if (currlvl < maxenchant) {
      if (!event.getPlayer().isSneaking()) {
        if (amount <= 0) {
          event.getPlayer().sendMessage(Vote.index + " Cost of upgrade is " + cost + "  You Have: " + ETools.round(balance));
        }
        else {
          String itemname = costItem.toString().substring(costItem.toString().indexOf("{") + 1, costItem.toString().indexOf("x") - 1) + ":" + is.getDurability();
          event.getPlayer().sendMessage(Vote.index + " Cost of upgrade is " + cost + " Money and " + itemname + " amount: " + costItem.getAmount() + " You Have: " + ETools.round(balance));
        }

        if (event.getPlayer().hasPermission("epicenchant.downgrade")) {
          event.getPlayer().sendMessage(Vote.index + " CARE, your upgrade chance is: " + Vote.color_red + pech + "%");
        }
        return;
      }
      if (balance < cost) {
        event.getPlayer().sendMessage(Vote.index + Vote.color_red + " You do not have enough money.");
        return;
      }
      if (amount > 0)
      {
        boolean czyma = false;
        int liczba = 0;
        ItemStack[] caleEq = event.getPlayer().getInventory().getContents();
        for (ItemStack itemStack : caleEq) {
          if ((itemStack != null) && 
            (itemStack.getType().equals(costItem.getType())) && (itemStack.getDurability() == costItem.getDurability())) {
            if (itemStack.getAmount() >= costItem.getAmount()) {
              czyma = true;
              break;
            }

            liczba += itemStack.getAmount();
            if (liczba >= ilosc) { czyma = true; break;
            }
          }
        }
        if (!czyma) {
          event.getPlayer().sendMessage(Vote.index + Vote.color_red + " You do not have enough items to upgrade.");
          return;
        }
      }

      int upgraded = currlvl + 1;
      if (max > 0) upgraded = max;
      Vote.econ.withdrawPlayer(event.getPlayer().getName(), cost);

      if ((!event.getPlayer().hasPermission("epicenchant.downgrade")) || (max > 0)) {
        event.getPlayer().getInventory().getItemInHand().addUnsafeEnchantment(ench, upgraded);
        event.getPlayer().sendMessage(Vote.index + " Successfully enchanted " + sign.getLine(1) + " to " + Vote.color_green + upgraded);
      }
      else {
        double los = Math.random() * 100.0D;
        if (los < pech) {
          event.getPlayer().getInventory().getItemInHand().addUnsafeEnchantment(ench, upgraded);
          event.getPlayer().sendMessage(Vote.index + " Successfully enchanted " + sign.getLine(1) + " to " + Vote.color_green + upgraded);
        }
        else if (event.getPlayer().hasPermission("epicenchant.downgrade.remove")) {
          event.getPlayer().sendMessage(Vote.index + Vote.color_red + " Upgreading failed " + sign.getLine(1) + " removed.");
          event.getPlayer().getInventory().getItemInHand().removeEnchantment(ench);
        }
        else if (event.getPlayer().hasPermission("epicenchant.downgrade.nothing")) {
          event.getPlayer().sendMessage(Vote.index + Vote.color_red + " Upgreading failed, but nothing happened");
        }
        else if (event.getPlayer().hasPermission("epicenchant.downgrade.half")) {
          upgraded = currlvl / 2;
          event.getPlayer().sendMessage(Vote.index + Vote.color_red + " Upgreading failed " + sign.getLine(1) + " downgraded to " + Vote.color_green + upgraded);
          event.getPlayer().getInventory().getItemInHand().addUnsafeEnchantment(ench, upgraded);
        }
        else {
          upgraded = currlvl - 1;
          event.getPlayer().sendMessage(Vote.index + Vote.color_red + " Upgreading failed " + sign.getLine(1) + " downgraded to " + Vote.color_green + upgraded);
          if (upgraded <= 0)
            event.getPlayer().getInventory().getItemInHand().removeEnchantment(ench);
          else {
            event.getPlayer().getInventory().getItemInHand().addUnsafeEnchantment(ench, upgraded);
          }
        }
      }
      event.getPlayer().sendMessage(Vote.index + " Balance changed from: " + ETools.round(balance) + " --> " + Vote.color_yellow + "[" + ETools.round(Vote.econ.getBalance(event.getPlayer().getName())) + "]" + Vote.color_gold + " (-" + cost + ")");
      if (amount > 0) {
        event.getPlayer().getInventory().removeItem(new ItemStack[] { costItem });
        event.getPlayer().updateInventory();
      }
      return;
    }

    event.getPlayer().sendMessage(Vote.index + " You can not enchant this item - Max level reached.");
  }

  private void random_enchant(PlayerInteractEvent event, Sign sign) {
    int maxlvl = Integer.parseInt(sign.getLine(3));
    int cost = 0;
    int amount = 0;
    int itemid = 0;
    short subid = 0;
    ItemStack is = null;
    if (!sign.getLine(2).contains("-")) {
      cost = Integer.parseInt(sign.getLine(2));
    }
    else {
      int pos1 = sign.getLine(2).indexOf("-");
      int pos2 = sign.getLine(2).indexOf("=");
      int pos3 = sign.getLine(2).indexOf(":");
      cost = Integer.parseInt(sign.getLine(2).substring(0, pos1));
      amount = Integer.parseInt(sign.getLine(2).substring(pos1 + 1, pos2));
      itemid = Integer.parseInt(sign.getLine(2).substring(pos2 + 1, pos3));
      subid = Short.parseShort(sign.getLine(2).substring(pos3 + 1));

      is = new ItemStack(itemid, amount, subid);
    }

    double balance = Vote.econ.getBalance(event.getPlayer().getName());

    String type = sign.getLine(1).toLowerCase().substring(7);
    String typeinhand = ETools.canEnchant(event.getPlayer().getItemInHand().getTypeId());

    if (!type.equalsIgnoreCase(typeinhand.toLowerCase())) {
      event.getPlayer().sendMessage(Vote.index + " Wrong item for this RandomEnchant.");
      return;
    }
    ItemStack iteminhand = event.getPlayer().getItemInHand();
    if (!iteminhand.getEnchantments().isEmpty()) {
      event.getPlayer().sendMessage(Vote.index + " This item has enchantments already.");
      return;
    }

    if (!event.getPlayer().isSneaking()) {
      if (amount <= 0) {
        event.getPlayer().sendMessage(Vote.index + " Cost of RandomEnchantments: " + cost);
      }
      else {
        String itemname = is.toString().substring(is.toString().indexOf("{") + 1, is.toString().indexOf("x") - 1) + ":" + is.getDurability();
        event.getPlayer().sendMessage(Vote.index + " Cost of upgrade is " + cost + " Money and " + itemname + " amount: " + is.getAmount() + " You Have: " + ETools.round(balance));
      }
      return;
    }
    if (balance < cost) {
      event.getPlayer().sendMessage(Vote.index + " You do not have enough money.");
      return;
    }
    if (amount > 0)
    {
      boolean czyma = false;
      int liczba = 0;
      ItemStack[] caleEq = event.getPlayer().getInventory().getContents();
      for (ItemStack itemStack : caleEq) {
        if ((itemStack != null) && 
          (itemStack.getType().equals(is.getType())) && (itemStack.getDurability() == is.getDurability())) {
          if (itemStack.getAmount() >= is.getAmount()) {
            czyma = true;
            break;
          }

          liczba += itemStack.getAmount();
          if (liczba >= is.getAmount()) { czyma = true; break;
          }
        }
      }
      if (!czyma) {
        event.getPlayer().sendMessage(Vote.index + " You do not have enough items to upgrade.");
        return;
      }

    }

    List enchants = new ArrayList();
    List enchantsused = new ArrayList();
    double rand = Math.random();
    if (event.getPlayer().hasPermission("epicenchant.admin")) rand *= 0.1D;
    else if (event.getPlayer().hasPermission("epicenchant.epicvip")) rand *= 0.5D;
    else if (event.getPlayer().hasPermission("epicenchant.vip")) rand *= 0.7D;
    else if (event.getPlayer().hasPermission("epicenchant.minivip")) rand *= 0.9D;

    int ea = 1;
    String str1;
    switch ((str1 = type).hashCode()) { case 97738:
      if (str1.equals("bow")) break; break;
    case 3565976:
      if (str1.equals("tool"));
      break;
    case 93086015:
      if (!str1.equals("armor")) { break label1526;

        enchants.add(Integer.valueOf(48));
        enchants.add(Integer.valueOf(49));
        enchants.add(Integer.valueOf(50));
        enchants.add(Integer.valueOf(51));

        if (rand < 0.01D) { ea = 4;
        } else if (rand < 0.05D) { ea = 3;
        } else if (rand < 0.1D) { ea = 2; } else {
          ea = 1;

          break label1526;

          enchants.add(Integer.valueOf(16));
          enchants.add(Integer.valueOf(17));
          enchants.add(Integer.valueOf(18));
          enchants.add(Integer.valueOf(19));
          enchants.add(Integer.valueOf(20));
          enchants.add(Integer.valueOf(21));
          enchants.add(Integer.valueOf(32));
          enchants.add(Integer.valueOf(33));
          enchants.add(Integer.valueOf(34));
          enchants.add(Integer.valueOf(35));

          if (rand < 0.001D) ea = 10;
          else if (rand < 0.002D) ea = 9;
          else if (rand < 0.005D) ea = 8;
          else if (rand < 0.0075D) ea = 7;
          else if (rand < 0.01D) ea = 6;
          else if (rand < 0.025D) ea = 5;
          else if (rand < 0.05D) ea = 4;
          else if (rand < 0.1D) ea = 3;
          else if (rand < 0.2D) ea = 2; else
            ea = 1;
        }
      }
      else
      {
        enchants.add(Integer.valueOf(0));
        enchants.add(Integer.valueOf(1));
        enchants.add(Integer.valueOf(2));
        enchants.add(Integer.valueOf(3));
        enchants.add(Integer.valueOf(4));
        enchants.add(Integer.valueOf(5));
        enchants.add(Integer.valueOf(6));
        if (rand < 0.005D) ea = 7;
        else if (rand < 0.01D) ea = 6;
        else if (rand < 0.02D) ea = 5;
        else if (rand < 0.05D) ea = 4;
        else if (rand < 0.1D) ea = 3;
        else if (rand < 0.2D) ea = 2; else {
          ea = 1;
        }
      }
      break;
    }
    label1526: 
    	r = new Random();
    int id = 0;
    double level = 0.0D;
    int los = 0;
    boolean done = false;
    while (!done) {
      los = r.nextInt(enchants.size());
      id = ((Integer)enchants.get(los)).intValue();

      if (enchantsused.isEmpty()) {
        rand = Math.random() * Math.random();
        level = maxlvl * rand;

        if (event.getPlayer().hasPermission("epicenchant.admin")) level *= 2.0D;
        else if (event.getPlayer().hasPermission("epicenchant.epicvip")) level *= 1.3D;
        else if (event.getPlayer().hasPermission("epicenchant.vip")) level *= 1.2D;
        else if (event.getPlayer().hasPermission("epicenchant.minivip")) level *= 1.1D;

        level = Math.round(level);
        if (level <= 0.0D) level = 1.0D;
        else if (level > maxlvl) level = maxlvl;

        iteminhand.addUnsafeEnchantment(Enchantment.getById(id), (int)level);
        enchantsused.add(Integer.valueOf(id));
        ea--;
      }
      else if (!enchantsused.contains(Integer.valueOf(id))) {
        rand = Math.random() * Math.random();
        level = maxlvl * rand;

        if (event.getPlayer().hasPermission("epicenchant.admin")) level *= 2.0D;
        else if (event.getPlayer().hasPermission("epicenchant.epicvip")) level *= 1.3D;
        else if (event.getPlayer().hasPermission("epicenchant.vip")) level *= 1.2D;
        else if (event.getPlayer().hasPermission("epicenchant.minivip")) level *= 1.1D;

        level = Math.round(level);
        if (level <= 0.0D) level = 1.0D;
        else if (level > maxlvl) level = maxlvl;

        iteminhand.addUnsafeEnchantment(Enchantment.getById(id), (int)level);
        enchantsused.add(Integer.valueOf(id));
        ea--;
      }
      if (ea <= 0) done = true;
    }
    Vote.econ.withdrawPlayer(event.getPlayer().getName(), cost);
    double money = ETools.round(Vote.econ.getBalance(event.getPlayer().getName()));
    event.getPlayer().sendMessage(Vote.index + " Random enchantments applied for " + money + " money. (-" + cost + ")");
    if (amount > 0) {
      event.getPlayer().getInventory().removeItem(new ItemStack[] { is });
      event.getPlayer().updateInventory();
    }
  }
}