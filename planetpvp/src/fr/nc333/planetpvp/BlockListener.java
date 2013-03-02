package fr.nc333.planetpvp;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class BlockListener
  implements Listener
{
  public BlockListener(Vote main)
  {
  }

  @EventHandler
  public void onSignChange(SignChangeEvent event)
  {
    if ((!event.getLine(0).equalsIgnoreCase("[epicenchant]")) && (!event.getLine(0).equalsIgnoreCase(Vote.tabliczka))) {
      return;
    }
    if ((!event.getPlayer().isOp()) && (!event.getPlayer().hasPermission("epicenchant.create"))) {
      event.getPlayer().sendMessage(Vote.index + " You can't place Enchant signs.");
      event.setCancelled(true);
      return;
    }
    boolean rand = false;

    Player player = event.getPlayer();

    if (event.getLine(1).toLowerCase().startsWith("random")) {
      int pos = event.getLine(1).indexOf(":") + 1;
      String type = event.getLine(1).toLowerCase().substring(pos);
      if ((!type.equals("bow")) && (!type.equals("armor")) && (!type.equals("tool"))) {
        event.getPlayer().sendMessage(Vote.index + " Wrong type of item for random.");
        event.setCancelled(true);
        return;
      }

      String fromLowerToUpper = "Random:" + type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
      event.setLine(1, fromLowerToUpper);
      rand = true;
    }
    else if (!ETools.isCorrectEnchantment(event.getLine(1).toLowerCase())) {
      player.sendMessage(Vote.index + " You typed incorrect enchantment !");
      event.setCancelled(true);
      return;
    }

    if (event.getLine(2).toLowerCase().contains("-")) {
      String tekst = event.getLine(2).toLowerCase();

      if ((!tekst.contains("=")) || (!tekst.contains(":"))) {
        player.sendMessage(Vote.index + " Wrong Cost formula use: Cost-ItemAmount=Itemid:SubId !");
        event.setCancelled(true);
        return;
      }

      String cost = "";
      String itemid = "";
      String itemsubid = "";
      String amount = "";

      int pos1 = tekst.indexOf("-");
      int pos2 = tekst.indexOf("=");
      int pos3 = tekst.indexOf(":");

      cost = tekst.substring(0, pos1);
      amount = tekst.substring(pos1 + 1, pos2);
      itemid = tekst.substring(pos2 + 1, pos3);
      itemsubid = tekst.substring(pos3 + 1);

      if (!ETools.isInteger(cost)) {
        player.sendMessage(Vote.index + " Cost must be Integer or wrong formula !");
        event.setCancelled(true);
        return;
      }
      if (Integer.parseInt(cost) < 0) cost = "0";

      if (!ETools.isInteger(amount)) {
        player.sendMessage(Vote.index + " Amount must be Integer or wrong formula !");
        event.setCancelled(true);
        return;
      }
      if (Integer.parseInt(amount) <= 0) amount = "1";

      if (!ETools.isInteger(itemid)) {
        player.sendMessage(Vote.index + " ItemID must be Integer or wrong formula !");
        event.setCancelled(true);
        return;
      }
      if (Integer.parseInt(itemid) <= 0) { itemid = "1";
      } else {
        Material mat = Material.getMaterial(Integer.parseInt(itemid));
        if (mat == null) {
          player.sendMessage(Vote.index + " Wrong Item ID !");
          event.setCancelled(true);
          return;
        }
      }

      if (!ETools.isInteger(itemsubid)) {
        player.sendMessage(Vote.index + " SubID must be Integer or wrong formula !");
        event.setCancelled(true);
        return;
      }
      if (Integer.parseInt(itemid) <= 0) itemid = "1";
      else if (Integer.parseInt(itemid) > 32000) itemid = "32000";

      event.setLine(2, cost + "-" + amount + "=" + itemid + ":" + itemsubid);
    }
    else {
      if (!ETools.isInteger(event.getLine(2))) {
        event.getPlayer().sendMessage(Vote.index + " Cost must be Integer.");
        event.setCancelled(true);
        return;
      }
      if (Integer.parseInt(event.getLine(2)) < 0) event.setLine(2, "0");
    }

    if (!rand)
    {
      if (event.getLine(3).equalsIgnoreCase("reverse")) { event.setLine(3, "Reverse");
      } else if (event.getLine(3).contains("-"))
      {
        int pos = event.getLine(3).indexOf("-");
        String min = event.getLine(3).substring(0, pos);
        String max = event.getLine(3).substring(pos + 1);

        if ((!ETools.isInteger(min)) || (!ETools.isInteger(max))) {
          event.getPlayer().sendMessage(Vote.index + " Min and Max must be integers.");
          event.setCancelled(true);
          return;
        }

        int minint = Integer.parseInt(min);
        int maxint = Integer.parseInt(max);

        if (minint > maxint) {
          event.setLine(3, maxint + "-" + minint);
        }

      }
      else if (ETools.isInteger(event.getLine(3))) {
        if (Integer.parseInt(event.getLine(3)) > 127) event.setLine(3, "127");
        else if (Integer.parseInt(event.getLine(3)) < 1) event.setLine(3, "1");
      }
      else if (event.getLine(3).toLowerCase().startsWith("max:")) {
        String txt = event.getLine(3).substring(4);
        if (ETools.isInteger(txt)) {
          if (Integer.parseInt(txt) > 127) event.setLine(3, "Max:127");
          else if (Integer.parseInt(txt) < 1) event.setLine(3, "Max:1");
        }
        else if (txt.equalsIgnoreCase("reverse"))
          event.setLine(3, "Max:Reverse");
        else
          event.setLine(3, "Max:127");
      } else {
        event.setLine(3, "127");
      }
      event.getPlayer().sendMessage(Vote.index + " Shop created succesfully.");
      event.setLine(0, Vote.tabliczka);
      String FromLowerToUpper = event.getLine(1).substring(0, 1).toUpperCase() + event.getLine(1).substring(1).toLowerCase();
      event.setLine(1, FromLowerToUpper);
      return;
    }

    if (!ETools.isInteger(event.getLine(3))) {
      event.setLine(3, "127");
    }
    else if (Integer.parseInt(event.getLine(3)) < 0) event.setLine(3, "1");
    else if (Integer.parseInt(event.getLine(3)) > 127) event.setLine(3, "127");

    event.getPlayer().sendMessage(Vote.index + " RandomEnchant shop created succesfully.");
    event.setLine(0, Vote.tabliczka);
  }
}