package su.nightexpress.excellentcrates.crates.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class CratesLang implements LangContainer {

    public static final TextLocale COMMAND_ROOT_DESCRIPTION = LangEntry
        .builder("crates.command.root.description")
        .text("Crates root command.");

    public static final TextLocale COMMAND_RELOAD_DESCRIPTION = LangEntry
        .builder("crates.command.reload.description")
        .text("Reload the plugin.");

    public static final TextLocale COMMAND_EDITOR_DESCRIPTION = LangEntry
        .builder("crates.command.editor.description")
        .text("Crates editor command.");

    public static final TextLocale COMMAND_OPEN_DESCRIPTION = LangEntry
        .builder("crates.command.open.description")
        .text("Open a specific crate remotely.");

    public static final TextLocale COMMAND_OPEN_FOR_DESCRIPTION = LangEntry
        .builder("crates.command.openfor.description")
        .text("Open crate for a player.");

    public static final MessageLocale COMMAND_OPEN_FOR_DONE = LangEntry
        .builder("crates.command.openfor.done")
        .chatMessage("Opened " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " for " +
            TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "."
        );

    public static final MessageLocale COMMAND_OPEN_FOR_NOTIFY = LangEntry
        .builder("crates.command.openfor.notify")
        .chatMessage("You have been forced to open " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + ".");

    public static final MessageLocale COMMAND_SYNTAX_INVALID_CRATE_ARGUMENT = LangEntry
        .builder("crates.command.syntax.invalid_crate_argument")
        .chatMessage(
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) + " is not a valid crate!")
        );

    public static final MessageLocale GENERIC_CRATE_NOT_FOUND = LangEntry
        .builder("crates.generic.crate_not_found")
        .chatMessage(TagWrappers.GRAY.wrap("Crate " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_VALUE) +
            " not found!")
        );

    public static final MessageLocale PIPELINE_START_OTHERS = LangEntry
        .builder("crates.pipeline.start.others")
        .chatMessage("You have started the " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " crate pipeline for " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "."
        );

    public static final MessageLocale PIPELINE_START_SELF = LangEntry
        .builder("crates.pipeline.start.self")
        .chatMessage("You have started the " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " crate pipeline."
        );
}