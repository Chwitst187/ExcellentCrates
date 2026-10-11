package su.nightexpress.excellentcrates.keys.balance.command;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceService;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeySetCommand implements KeyCommand {

    private static final String ARGUMENT_PLAYER = "player";
    private static final String ARGUMENT_KEY    = "key";
    private static final String ARGUMENT_AMOUNT = "amount";

    private static final String FLAG_SILENT               = "s";
    private static final String FLAG_SILENT_LONG          = "silent";
    private static final String FLAG_SILENT_FEEDBACK      = "sf";
    private static final String FLAG_SILENT_FEEDBACK_LONG = "silentfeedback";

    private static final Logger LOGGER = LoggerFactory.getLogger(KeySetCommand.class);

    private final KeyBalanceService    balanceService;
    private final UserDataManager      userDataService;
    private final KeyMessageDispatcher dispatcher;

    public KeySetCommand(KeyBalanceService balanceService,
                         UserDataManager userDataService,
                         KeyMessageDispatcher dispatcher) {
        this.balanceService = balanceService;
        this.userDataService = userDataService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("set", builder -> builder
            .description(KeyLang.COMMAND_SET_DESCRIPTION)
            .permission(KeyPerms.COMMAND_SET)
            .withArguments(
                Arguments.playerName(ARGUMENT_PLAYER),
                Arguments.argument(ARGUMENT_KEY, CrateKey.class),
                Arguments.integer(ARGUMENT_AMOUNT, 1)
                    .optional()
                    .suggestions((reader, context) -> List.of("1", "5", "10"))
            )
            .withFlags(FLAG_SILENT, FLAG_SILENT_LONG, FLAG_SILENT_FEEDBACK, FLAG_SILENT_FEEDBACK_LONG)
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        String playerName = arguments.getString(ARGUMENT_PLAYER);
        CrateKey key = arguments.get(ARGUMENT_KEY, CrateKey.class);
        int amount = arguments.getInt(ARGUMENT_AMOUNT, 1);
        CommandSender sender = context.getSender();

        boolean silent = context.hasFlag(FLAG_SILENT) || context.hasFlag(FLAG_SILENT_LONG);
        boolean silentFeedback = context.hasFlag(FLAG_SILENT_FEEDBACK) || context.hasFlag(FLAG_SILENT_FEEDBACK_LONG);

        this.userDataService.loadByNameAndCacheAsync(playerName).thenCompose(opt -> {
            if (opt.isEmpty()) {
                this.dispatcher.send(sender, CoreLang.ERROR_INVALID_PLAYER);
                return CompletableFuture.completedFuture(null);
            }

            UserData userData = opt.get();

            return this.balanceService.setKeyAsync(userData.getId(), key, amount).thenAccept(result -> {
                result.handleFeedback((locale, actionContext) -> {
                    if (!silentFeedback && (context.getPlayer() == null || !context.getPlayer().getUniqueId().equals(userData.getId()))) {
                        this.dispatcher.sendBase(sender, key, locale, builder -> builder
                            .apply(actionContext)
                            .with(CommonPlaceholders.PLAYER_NAME, userData::getName)
                        );
                    }

                    if (result.success() && !silent) {
                        Players.findById(userData.getId()).ifPresent(target -> {
                            this.dispatcher.sendBase(target, key, KeyLang.KEY_SET_NOTIFY, actionContext);
                        });
                    }
                });
            });
        }).exceptionally(exception -> {
            LOGGER.error("An error occurred while setting key", exception);
            this.dispatcher.send(sender, Lang.GENERIC_INTERNAL_ERROR);
            return null;
        });

        return true;
    }
}
