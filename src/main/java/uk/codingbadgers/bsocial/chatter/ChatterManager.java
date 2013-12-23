package uk.codingbadgers.bsocial.chatter;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatterManager {

    private final List<Chatter> chatters = new ArrayList<>();

    public Chatter getChatter(ProxiedPlayer player) {
        return getChatter(player.getName());
    }

    public Chatter getChatter(String player) {
        List<Chatter> chatters = new ArrayList<>(this.chatters);

        for (Chatter chatter : chatters) {
            if (chatter.getName().equalsIgnoreCase(player)) {
                return chatter;
            }
        }

        return null;
    }

    public void loadChatter(ProxiedPlayer player) {
        chatters.add(new Chatter(player));
    }

    public boolean removeChatter(ProxiedPlayer player) {
        Chatter chatter = getChatter(player);

        if (chatter == null) {
            return false;
        }

        chatter.destroy();
        chatters.remove(chatter);
        return true;
    }

}
