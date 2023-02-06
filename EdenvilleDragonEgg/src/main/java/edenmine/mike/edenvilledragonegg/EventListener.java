package edenmine.mike.edenvilledragonegg;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {

    long startingPlayerTime;

    @EventHandler
    public void onPlayerJoin(){
        startingPlayerTime = System.currentTimeMillis();
    }

    //Non lo fa droppare al player
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e){
        if(e.getItemDrop().getItemStack().getType() == Material.DRAGON_EGG){
            e.setCancelled(true);
            e.getPlayer().sendMessage("You can't drop this item");
        }
    }

    //Lo piazza nella posizione di despawn del player
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if(player.getInventory().contains(Material.DRAGON_EGG) && player.getLocation().getWorld().getName().equals("world")){
            player.getInventory().remove(Material.DRAGON_EGG);
            Location location = player.getLocation();
            location.getBlock().setType(Material.DRAGON_EGG);
        }
    }
    //Non viene preso dagli hopper
    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent e){
        if(e.getItem().getItemStack().getType() == Material.DRAGON_EGG){
            e.setCancelled(true);
        }
    }

    //Non puoi aprire una gui con l'uovo nell'inventario
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        if(e.getInventory().getType() != InventoryType.PLAYER && e.getPlayer().getInventory().contains(Material.DRAGON_EGG)){
            e.setCancelled(true);
            e.getPlayer().sendMessage("You must to place the Dragon Egg before to open any type of storage");
        }
        /*if(System.currentTimeMillis() - startingPlayerTime >= 10000){
            startingPlayerTime = System.currentTimeMillis();
            e.getPlayer().sendMessage("Sono passati più di 10 secondi dall'ultima interazione");
        }else{
            startingPlayerTime = System.currentTimeMillis();
            e.getPlayer().sendMessage("Sono passati meno di 10 secondi dall'ultima interazione");
        }*/
    }

    //Comunica la vicinanza all'uovo
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Location eggLocation = new Location(player.getWorld(), -110, 64, 78);
        if(player.getLocation().distance(eggLocation) < 5 && System.currentTimeMillis() - startingPlayerTime >= 10000){
            startingPlayerTime = System.currentTimeMillis();
            player.sendMessage("Sei vicino all'uovo");
        }
    }

    //Non puù despawnare
    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e){
        if(e.getEntity().getItemStack().getType() == Material.DRAGON_EGG){
            e.setCancelled(true);
        }
    }

    //lo rende non bruciabile
    @EventHandler
    public void onIntemBurn(EntityCombustEvent e){
        if(e.getEntity() instanceof Item){
            ItemStack burnedItem = ((Item) e.getEntity()).getItemStack();
            if(burnedItem.getType().equals(Material.DRAGON_EGG)){
                e.setCancelled(true);
            }
        }
    }

    //Non fa tippare l'uovo ci si clicca sopra
    @EventHandler
    public void onEggClick(BlockFromToEvent e){
        if(e.getToBlock().getType().equals(Material.DRAGON_EGG)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        if(e.blockList().size() > 0){
            for(Block b : e.blockList().){
                if(b.getType() == Material.DRAGON_EGG){
                    e.blockList().remove(b);
                }
            }
        }
    }

}
