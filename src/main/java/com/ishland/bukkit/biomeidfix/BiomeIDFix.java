package com.ishland.bukkit.biomeidfix;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BiomeIDFix extends JavaPlugin {

    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(
                this, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() != PacketType.Play.Server.MAP_CHUNK) return;
                final PacketContainer packet = event.getPacket();
                int[] biomeArray = packet.getIntegerArrays().read(0);
                for (int i = 0; i < biomeArray.length; i++)
                    if (biomeArray[i] < 0)
                        biomeArray[i] = 0;
                packet.getIntegerArrays().write(0, biomeArray);
            }
        });
    }

    @Override
    public void onDisable() {
        protocolManager.removePacketListeners(this);
    }
}
