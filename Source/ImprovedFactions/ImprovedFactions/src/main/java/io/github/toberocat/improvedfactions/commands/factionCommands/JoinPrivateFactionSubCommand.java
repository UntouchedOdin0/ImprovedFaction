package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.event.faction.FactionJoinEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Parseable;
import io.github.toberocat.improvedfactions.ranks.NewMemberRank;
import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinPrivateFactionSubCommand extends SubCommand {
    public static UUID joinUUID = UUID.randomUUID();
    public JoinPrivateFactionSubCommand() {
        super("join" + joinUUID.toString(), "");
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setAllowAliases(false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) == null) {
            if (args.length != 1) {
                CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
                return;
            }
            Faction foundFaction = FactionUtils.getFactionByRegistry(args[0]);
            if (foundFaction == null)
            {
                Language.sendMessage(LangMessage.JOIN_ERROR_NO_FACTION_FOUND, player);
                return;
            }

            if (foundFaction.Join(player, Rank.fromString(NewMemberRank.registry))) {
                Language.sendMessage(LangMessage.JOIN_SUCCESS, player,
                        new Parseable("{faction_displayname}", foundFaction.getDisplayName()));
            } else {
                if (foundFaction.hasMaxMembers())
                    Language.sendMessage(LangMessage.JOIN_FULL, player);
                else
                    CommandExecuteError(CommandExecuteError.OtherError, player);
            }
        } else {
            player.sendMessage(Language.getPrefix() + "§cYou have already joined a faction. Please leave before joining another faction");
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        return arguments;
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        return false;
    }

    @Override
    public void CallSubCommand(Player player, String[] args) {
        CommandExecute(player, args);
    }
}
