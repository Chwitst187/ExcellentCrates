package su.nightexpress.excellentcrates.crates.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.excellentcrates.crates.lang.CratesLang;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class CrateReloadCommand implements CrateCommand {

    private final CratesPlugin      plugin;
    private final MessageDispatcher dispatcher;

    public CrateReloadCommand(CratesPlugin plugin, MessageDispatcher dispatcher) {
        this.plugin = plugin;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("reload", builder -> builder
            .description(CratesLang.COMMAND_RELOAD_DESCRIPTION)
            .permission(Perms.COMMAND_RELOAD)
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        this.plugin.reload();
        this.dispatcher.send(context.getSender(), CoreLang.PLUGIN_RELOADED);
        return true;
    }
}