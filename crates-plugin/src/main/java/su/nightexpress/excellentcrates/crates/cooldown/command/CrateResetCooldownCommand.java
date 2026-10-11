package su.nightexpress.excellentcrates.crates.cooldown.command;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.core.crate.permission.CratePerms;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.crates.cooldown.CrateCooldownService;
import su.nightexpress.excellentcrates.crates.cooldown.lang.CrateCooldownsLang;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CrateResetCooldownCommand implements CrateCommand {

    private static final String ARG_PLAYER = "player";
    private static final String ARG_CRATE  = "crate";

    private static final Logger LOGGER = LoggerFactory.getLogger(CrateResetCooldownCommand.class);

    private final CrateCooldownService   cooldownService;
    private final UserDataManager        userDataService;
    private final CrateMessageDispatcher dispatcher;

    public CrateResetCooldownCommand(CrateCooldownService cooldownService,
                                     UserDataManager userDataService,
                                     CrateMessageDispatcher dispatcher) {
        this.cooldownService = cooldownService;
        this.userDataService = userDataService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("resetcooldown", builder -> builder
            .description(CrateCooldownsLang.COMMAND_RESET_COOLDOWN_DESCRIPTION)
            .permission(CratePerms.COMMAND_RESET_COOLDOWN)
            .withArguments(
                Arguments.playerName(ARG_PLAYER),
                Arguments.argument(ARG_CRATE, Crate.class)
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        String playerName = arguments.getString(ARG_PLAYER);
        Crate crate = arguments.get(ARG_CRATE, Crate.class);
        CommandSender sender = context.getSender();

        this.userDataService.loadByNameAndCacheAsync(playerName).thenAccept(opt -> {
            if (opt.isEmpty()) {
                this.dispatcher.send(sender, CoreLang.ERROR_INVALID_PLAYER);
                return;
            }

            UserData userData = opt.get();
            this.cooldownService.resetCooldown(userData.getId(), crate.id());

            this.dispatcher.sendBase(sender, crate, CrateCooldownsLang.COMMAND_RESET_COOLDOWN_FEEDBACK, ctx -> ctx
                .with(CommonPlaceholders.PLAYER_NAME, userData::getName)
            );
        }).exceptionally(exception -> {
            LOGGER.error("An error occurred while resetting crate cooldown", exception);
            this.dispatcher.send(sender, Lang.GENERIC_INTERNAL_ERROR);
            return null;
        });

        return true;
    }
}