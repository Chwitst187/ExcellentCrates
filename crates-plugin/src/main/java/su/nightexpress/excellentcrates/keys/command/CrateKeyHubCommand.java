package su.nightexpress.excellentcrates.keys.command;

import java.util.Collection;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class CrateKeyHubCommand implements CrateCommand {

    private final Collection<KeyCommand> commands;

    public CrateKeyHubCommand(Collection<KeyCommand> commands) {
        this.commands = commands;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.hub("key", builder -> {
            builder.description(KeyLang.COMMAND_ROOT_DESCRIPTION);

            for (KeyCommand command : this.commands) {
                builder.branch(command.createCommand());
            }
        });
    }
}