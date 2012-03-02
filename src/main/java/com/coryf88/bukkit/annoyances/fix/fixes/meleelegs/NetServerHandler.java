package com.coryf88.bukkit.annoyances.fix.fixes.meleelegs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet7UseEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.WorldServer;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NetServerHandler extends net.minecraft.server.NetServerHandler {
	private MinecraftServer minecraftServer;
	private final CraftServer server;

	public NetServerHandler(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
		super(minecraftserver, networkmanager, entityplayer);
		this.minecraftServer = minecraftserver;
		this.networkManager = networkmanager;
		networkmanager.a(this);
		this.player = entityplayer;
		entityplayer.netServerHandler = this;

		// CraftBukkit start
		this.server = minecraftserver.server;
	}

	@Override
	public void a(Packet7UseEntity packet7useentity) {
		if (this.player.dead) return; // CraftBukkit

		WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
		Entity entity = worldserver.getEntity(packet7useentity.target);

		//if (entity != null && this.player.g(entity) && this.player.i(entity) < 36.0D) { // Bukkit
		if (entity != null && this.canSeeEntity(entity) && this.player.i(entity) < 36.0D) { // Modified
			ItemStack itemInHand = this.player.inventory.getItemInHand(); // CraftBukkit
			if (packet7useentity.action == 0) {
				// CraftBukkit start
				PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(this.getPlayer(), entity.getBukkitEntity());
				this.server.getPluginManager().callEvent(event);

				if (event.isCancelled()) return;
				// CraftBukkit end
				this.player.e(entity);
				// CraftBukkit start - update the client if the item is an infinite one
				if (itemInHand != null && itemInHand.count <= -1) {
					this.player.updateInventory(this.player.activeContainer);
				}
			} else if (packet7useentity.action == 1) {
				if (entity instanceof EntityItem || entity instanceof EntityExperienceOrb || entity instanceof EntityArrow) {
					String type = entity.getClass().getSimpleName();
					this.disconnect("Attacking an " + type + " is not permitted");
					System.out.println("Player " + this.player.name + " tried to attack an " + type + ", so I have disconnected them for exploiting.");
					return;
				}

				this.player.attack(entity);

				if (itemInHand != null && itemInHand.count <= -1) {
					this.player.updateInventory(this.player.activeContainer);
				}
				// CraftBukkit end
			}
		}
	}

	private boolean canSeeEntity(Entity entity) {
		for (double heightOffset = entity.getHeadHeight(); heightOffset >= 0.0; heightOffset -= 1.0D) {
			if (this.player.world.a(Vec3D.create(this.player.locX, this.player.locY + this.player.getHeadHeight(), this.player.locZ), Vec3D.create(entity.locX, entity.locY + heightOffset, entity.locZ)) == null) return true;
		}
		return false;
	}

	public static void override(net.minecraft.server.NetServerHandler netServerHandler) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		// Grab the field values for NetServerHandler's constructor.
		Field minecraftServerField = net.minecraft.server.NetServerHandler.class.getDeclaredField("minecraftServer");
		minecraftServerField.setAccessible(true);
		MinecraftServer minecraftserver = (MinecraftServer)minecraftServerField.get(netServerHandler);
		NetworkManager networkmanager = netServerHandler.networkManager;
		EntityPlayer entityplayer = netServerHandler.player;

		NetServerHandler newNSH = new NetServerHandler(minecraftserver, networkmanager, entityplayer);
		newNSH.disconnected = netServerHandler.disconnected;

		// Copy all field values over to the new instance.
		// Skip copying the following fields:
		//  - private static Random k = new Random();
		//  - public static Logger a = Logger.getLogger("Minecraft");
		//  - private MinecraftServer minecraftServer;
		//  - private final CraftServer server;
		//  - private static final int PLACE_DISTANCE_SQUARED = 6 * 6;
		//  - public NetworkManager networkManager;
		//  - public EntityPlayer player; // CraftBukkit - private -> public
		for (Field field : net.minecraft.server.NetServerHandler.class.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
				continue;
			}
			if (field.getName().equals("minecraftServer") || field.getName().equals("networkManager") || field.getName().equals("player")) {
				continue;
			}

			field.setAccessible(true);
			field.set(newNSH, field.get(netServerHandler));
		}
	}
}