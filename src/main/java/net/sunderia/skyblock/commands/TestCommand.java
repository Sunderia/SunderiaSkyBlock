package net.sunderia.skyblock.commands;

import net.sunderia.skyblock.annotation.CommandInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "test", aliases = {"t"}, permission = "sunderiaskyblock.test")
public class TestCommand extends CommandPlugin {

    @Override
    public void execute(@NotNull Player player, @NotNull String[] args) {

    }

}