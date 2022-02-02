package io.github.toberocat.core.utility.factions.rank;

import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

public class GuestRank extends Rank {
    public GuestRank() {
        super("&e&lGuest", "guest", false);
    }

    @Override
    public String description(Player player) {
        return Language.getMessage(LangMessage.RANK_GUEST_DESCRIPTION, player);
    }
}
