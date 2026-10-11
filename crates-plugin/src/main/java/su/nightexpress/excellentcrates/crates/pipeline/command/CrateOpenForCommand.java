package su.nightexpress.excellentcrates.crates.pipeline.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.batch.BatchPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.core.crate.permission.CratePerms;
import su.nightexpress.excellentcrates.crates.batch.pipeline.component.DefaultCrateBatchPipelineComponent;
import su.nightexpress.excellentcrates.crates.lang.CratesLang;
import su.nightexpress.excellentcrates.crates.pipeline.CratePipelineService;
import su.nightexpress.excellentcrates.crates.pipeline.component.DefaultFastOpenPipelineComponent;
import su.nightexpress.excellentcrates.crates.pipeline.component.DefaultFreeOpenComponent;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CrateOpenForCommand implements CrateCommand {

    private static final String ARG_PLAYER = "player";
    private static final String ARG_CRATE  = "crate";

    private static final String FLAG_SILENT               = "s";
    private static final String FLAG_SILENT_LONG          = "silent";
    private static final String FLAG_SILENT_FEEDBACK      = "sf";
    private static final String FLAG_SILENT_FEEDBACK_LONG = "silentfeedback";
    private static final String FLAG_FORCE                = "f";
    private static final String FLAG_FORCE_LONG           = "force";
    private static final String FLAG_FREE                 = "free";
    private static final String FLAG_FAST                 = "fast";
    private static final String FLAG_MASS                 = "m";
    private static final String FLAG_MASS_LONG            = "mass";

    private final CratePipelineService   pipelineService;
    private final CrateMessageDispatcher dispatcher;

    public CrateOpenForCommand(CratePipelineService pipelineService,
                               CrateMessageDispatcher dispatcher) {
        this.pipelineService = pipelineService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("openfor", builder -> builder
            .permission(CratePerms.COMMAND_OPEN_FOR)
            .description(CratesLang.COMMAND_OPEN_FOR_DESCRIPTION)
            .withArguments(
                Arguments.player(ARG_PLAYER),
                Arguments.argument(ARG_CRATE, Crate.class)
            )
            .withFlags(
                FLAG_SILENT, FLAG_SILENT_LONG,
                FLAG_SILENT_FEEDBACK, FLAG_SILENT_FEEDBACK_LONG,
                FLAG_FORCE, FLAG_FORCE_LONG, FLAG_FREE,
                FLAG_FAST,
                FLAG_MASS, FLAG_MASS_LONG
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        CommandSender sender = context.getSender();
        Player target = arguments.getPlayer(ARG_PLAYER);
        Crate crate = arguments.get(ARG_CRATE, Crate.class);

        boolean silent = context.hasFlag(FLAG_SILENT) || context.hasFlag(FLAG_SILENT_LONG);
        boolean silentFeedback = context.hasFlag(FLAG_SILENT_FEEDBACK) || context.hasFlag(FLAG_SILENT_FEEDBACK_LONG);
        boolean force = context.hasFlag(FLAG_FORCE) || context.hasFlag(FLAG_FORCE_LONG) || context.hasFlag(FLAG_FREE);
        boolean fast = context.hasFlag(FLAG_FAST);
        boolean mass = context.hasFlag(FLAG_MASS) || context.hasFlag(FLAG_MASS_LONG);

        if (!silent) {
            this.dispatcher.sendBase(target, crate, CratesLang.COMMAND_OPEN_FOR_NOTIFY);
        }

        if (!silentFeedback && sender != target) {
            this.dispatcher.sendBase(sender, crate, CratesLang.COMMAND_OPEN_FOR_DONE, ctx -> ctx
                .with(CommonPlaceholders.PLAYER_NAME, target::getName)
            );
        }

        this.pipelineService.startPipeline(target, crate, ctx -> {
            if (force) {
                ctx.putComponent(PipelineComponentKeys.FREE_OPEN, new DefaultFreeOpenComponent());
            }
            if (fast) {
                ctx.putComponent(PipelineComponentKeys.FAST_OPEN, new DefaultFastOpenPipelineComponent());
            }
            if (mass) {
                BatchPipelineComponent batch = ctx.getComponentOrNull(PipelineComponentKeys.BATCH);
                if (batch == null) {
                    batch = new DefaultCrateBatchPipelineComponent(30);
                    ctx.putComponent(PipelineComponentKeys.BATCH, batch);
                }
                batch.setSelectedAmount(batch.getMaxAllowed());
            }
        });

        return true;
    }
}