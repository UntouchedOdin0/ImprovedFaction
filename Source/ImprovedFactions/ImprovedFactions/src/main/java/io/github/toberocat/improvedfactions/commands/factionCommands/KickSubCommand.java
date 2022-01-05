package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.data.PlayerData;
import io.github.toberocat.improvedfactions.event.faction.FactionDeleteEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionMember;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.language.Parseable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.language.bm.Lang;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KickSubCommand extends SubCommand {
    public KickSubCommand() {
        super("kick", LangMessage.KICK_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) != null) {
            Faction faction = FactionUtils.getFaction(player);
            if (FactionUtils.getPlayerRank(faction, player).isAdmin()) {
                if (args.length >= 1) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
                    if (faction.Leave(p)) {
                        Language.sendMessage(LangMessage.KICK_SUCCESS_SENDER, player,
                                new Parseable("{kicked}", p.getName()));
                        Language.sendMessage(LangMessage.KICK_SUCCESS_RECEIVER, player);

                        Language.sendMessage(LangMessage.KICK_SUCCESS_RECEIVER, player, new Parseable("{faction_displayname}", faction.getDescription()));

                    } else {
                        CommandExecuteError(CommandExecuteError.OtherError, player);
                    }
                } else {
                    CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
                }
            } else {
                CommandExecuteError(CommandExecuteError.OnlyAdminCommand, player);
            }
        } else {
            CommandExecuteError(CommandExecuteError.NoFactionPermission, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            PlayerData data = ImprovedFactionsMain.playerData.get(player.getUniqueId());
            for (FactionMember uuid : data.playerFaction.getMembers()) {
                if (uuid != null) {
                    OfflinePlayer ofPlayer = Bukkit.getPlayer(uuid.getUuid());
                    if (ofPlayer == null) continue;
                    arguments.add(ofPlayer.getName());
                }
            }
        }
        return arguments;
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        boolean result = super.CommandDisplayCondition(player, args);
        Faction faction = FactionUtils.getFaction(player);
        if (faction == null) {
            result = false;
            return result;
        }
        if (!FactionUtils.getPlayerRank(faction, player).isAdmin())
            result = false;
        return result;
    }
}
