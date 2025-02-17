package me.pepperbell.simplenetworking;

import me.pepperbell.simplenetworking.SimpleChannel.ResponseTarget;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public interface C2SPacket extends Packet {
	/**
	 * This method will be run on the network thread. Most method calls should be performed on the server thread by wrapping the code in a lambda:
	 * <pre>
	 * <code>server.execute(() -> {
	 * 	// code here
	 * }</code></pre>
	 */
	void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, ResponseTarget responseTarget);
}
