package su.nightexpress.excellentcrates.permission;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.crate.permission.CratePerms;
import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.excellentcrates.crates.item.permission.CrateItemPerms;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
import su.nightexpress.excellentcrates.preview.permission.PreviewPerms;

@NullMarked
public final class LegacyPermissions {

    public static final String PREFIX         = "excellentcrates.";
    public static final String PREFIX_COMMAND = PREFIX + "command.";
    public static final String PREFIX_BYPASS  = PREFIX + "bypass.";

    public static final String PLUGIN  = PREFIX + "*";
    public static final String COMMAND = PREFIX_COMMAND + "*";
    public static final String BYPASS  = PREFIX_BYPASS + "*";

    public static final String MASS_OPEN = PREFIX + "massopen";

    public static final String INCLUDE_KEY_GIVEALL = PREFIX + "include.giveall";

    public static final String AUTO_REDEEM     = PREFIX + "auto.redeem";
    public static final String AUTO_REDEEM_KEY = PREFIX + "key.auto.redeem";

    public static final String COMMAND_RELOAD             = PREFIX_COMMAND + "reload";
    public static final String COMMAND_EDITOR             = PREFIX_COMMAND + "editor";
    public static final String COMMAND_DROP               = PREFIX_COMMAND + "drop";
    public static final String COMMAND_DROP_KEY           = PREFIX_COMMAND + "dropkey";
    public static final String COMMAND_DROP_KEY_ALT       = PREFIX_COMMAND + "key.drop";
    public static final String COMMAND_OPEN               = PREFIX_COMMAND + "open";
    public static final String COMMAND_OPEN_OTHERS        = PREFIX_COMMAND + "open.others";
    public static final String COMMAND_OPEN_FOR           = PREFIX_COMMAND + "openfor";
    public static final String COMMAND_GIVE               = PREFIX_COMMAND + "give";
    public static final String COMMAND_KEY                = PREFIX_COMMAND + "key";
    public static final String COMMAND_KEY_GIVE           = PREFIX_COMMAND + "key.give";
    public static final String COMMAND_KEY_GIVEALL        = PREFIX_COMMAND + "key.giveall";
    public static final String COMMAND_KEY_TAKE           = PREFIX_COMMAND + "key.take";
    public static final String COMMAND_KEY_REMOVE         = PREFIX_COMMAND + "key.remove";
    public static final String COMMAND_KEY_SET            = PREFIX_COMMAND + "key.set";
    public static final String COMMAND_KEY_INSPECT        = PREFIX_COMMAND + "key.show";
    public static final String COMMAND_KEY_BALANCE        = PREFIX_COMMAND + "key.balance";
    public static final String COMMAND_KEY_INSPECT_OTHERS = PREFIX_COMMAND + "key.show.others";
    public static final String COMMAND_KEY_BALANCE_OTHERS = PREFIX_COMMAND + "key.balance.others";
    public static final String COMMAND_KEY_REDEEM         = PREFIX_COMMAND + "key.redeem";
    public static final String COMMAND_MENU               = PREFIX_COMMAND + "menu";
    public static final String COMMAND_MENU_OTHERS        = PREFIX_COMMAND + "menu.others";
    public static final String COMMAND_PREVIEW            = PREFIX_COMMAND + "preview";
    public static final String COMMAND_PREVIEW_OTHERS     = PREFIX_COMMAND + "preview.others";
    public static final String COMMAND_RESETCOOLDOWN      = PREFIX_COMMAND + "resetcooldown";

    public static final String BYPASS_CRATE_COOLDOWN = PREFIX_BYPASS + "crate.opencooldown";

    private LegacyPermissions() {
    }

    public static void setup(PluginManager pm) {
        link(pm, COMMAND_RELOAD, Perms.COMMAND_RELOAD);

        link(pm, COMMAND_EDITOR, CratePerms.COMMAND_EDITOR);
        link(pm, COMMAND_EDITOR, KeyPerms.COMMAND_EDITOR);

        link(pm, COMMAND_DROP, CrateItemPerms.COMMAND_DROP);
        link(pm, COMMAND_DROP_KEY, KeyPerms.COMMAND_DROP);
        link(pm, COMMAND_DROP_KEY_ALT, KeyPerms.COMMAND_DROP);

        link(pm, COMMAND_OPEN, CratePerms.COMMAND_OPEN);
        link(pm, COMMAND_OPEN_OTHERS, CratePerms.COMMAND_OPEN_OTHERS);
        link(pm, COMMAND_OPEN_FOR, CratePerms.COMMAND_OPEN_FOR);
        link(pm, COMMAND_OPEN_FOR, CratePerms.COMMAND_OPEN_OTHERS);

        link(pm, COMMAND_GIVE, CrateItemPerms.COMMAND_GIVE);

        link(pm, COMMAND_KEY, KeyPerms.COMMAND_ROOT);
        link(pm, COMMAND_KEY_GIVE, KeyPerms.COMMAND_GIVE);
        link(pm, COMMAND_KEY_GIVE, KeyPerms.COMMAND_GIVE_ALL);
        link(pm, COMMAND_KEY_GIVEALL, KeyPerms.COMMAND_GIVE_ALL);
        link(pm, COMMAND_KEY_TAKE, KeyPerms.COMMAND_REMOVE);
        link(pm, COMMAND_KEY_REMOVE, KeyPerms.COMMAND_REMOVE);
        link(pm, COMMAND_KEY_SET, KeyPerms.COMMAND_SET);
        link(pm, COMMAND_KEY_INSPECT, KeyPerms.COMMAND_BALANCE);
        link(pm, COMMAND_KEY_BALANCE, KeyPerms.COMMAND_BALANCE);
        link(pm, COMMAND_KEY_INSPECT_OTHERS, KeyPerms.COMMAND_BALANCE_OTHERS);
        link(pm, COMMAND_KEY_BALANCE_OTHERS, KeyPerms.COMMAND_BALANCE_OTHERS);
        link(pm, COMMAND_KEY_REDEEM, KeyPerms.COMMAND_REDEEM);

        link(pm, COMMAND_PREVIEW, PreviewPerms.COMMAND_PREVIEW);
        link(pm, COMMAND_PREVIEW_OTHERS, PreviewPerms.COMMAND_PREVIEW_OTHERS);

        link(pm, COMMAND_RESETCOOLDOWN, CratePerms.COMMAND_RESET_COOLDOWN);

        link(pm, AUTO_REDEEM, KeyPerms.AUTO_REDEEM);
        link(pm, AUTO_REDEEM_KEY, KeyPerms.AUTO_REDEEM);

        // Include in giveall (default: true for all players)
        Permission legacyGiveAll = getOrCreatePermission(pm, INCLUDE_KEY_GIVEALL, PermissionDefault.TRUE);
        legacyGiveAll.getChildren().put(KeyPerms.COMMAND_GIVE_ALL_INCLUDED.getName(), true);
        KeyPerms.COMMAND_GIVE_ALL_INCLUDED.getChildren().put(legacyGiveAll.getName(), true);
        legacyGiveAll.recalculatePermissibles();
        KeyPerms.COMMAND_GIVE_ALL_INCLUDED.recalculatePermissibles();

        // Crate root permission hierarchy
        CratePerms.COMMAND_ROOT.getChildren().put(CratePerms.COMMAND_EDITOR.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(CratePerms.COMMAND_OPEN.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(CratePerms.COMMAND_OPEN_OTHERS.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(CratePerms.COMMAND_OPEN_FOR.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(CratePerms.COMMAND_RESET_COOLDOWN.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(CrateItemPerms.COMMAND_GET.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(CrateItemPerms.COMMAND_GIVE.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(CrateItemPerms.COMMAND_DROP.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(PreviewPerms.COMMAND_PREVIEW.getName(), true);
        CratePerms.COMMAND_ROOT.getChildren().put(PreviewPerms.COMMAND_PREVIEW_OTHERS.getName(), true);
        CratePerms.COMMAND_ROOT.recalculatePermissibles();

        // Key root permission hierarchy
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_EDITOR.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_BALANCE.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_BALANCE_OTHERS.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_GET.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_GIVE.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_SET.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_GIVE_ALL.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_DROP.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_REMOVE.getName(), true);
        KeyPerms.COMMAND_ROOT.getChildren().put(KeyPerms.COMMAND_REDEEM.getName(), true);
        KeyPerms.COMMAND_ROOT.recalculatePermissibles();

        // Bypass permissions
        getOrCreatePermission(pm, BYPASS_CRATE_COOLDOWN, PermissionDefault.OP);
        Permission bypassWildcard = getOrCreatePermission(pm, BYPASS, PermissionDefault.OP);
        bypassWildcard.getChildren().put(BYPASS_CRATE_COOLDOWN, true);
        bypassWildcard.recalculatePermissibles();

        // Mass open
        getOrCreatePermission(pm, MASS_OPEN, PermissionDefault.OP);

        // Menu permissions (legacy compatibility)
        getOrCreatePermission(pm, COMMAND_MENU, PermissionDefault.OP);
        getOrCreatePermission(pm, COMMAND_MENU_OTHERS, PermissionDefault.OP);

        // Command wildcard: excellentcrates.command.*
        Permission commandWildcard = getOrCreatePermission(pm, COMMAND, PermissionDefault.OP);
        commandWildcard.getChildren().put(COMMAND_RELOAD, true);
        commandWildcard.getChildren().put(COMMAND_EDITOR, true);
        commandWildcard.getChildren().put(COMMAND_DROP, true);
        commandWildcard.getChildren().put(COMMAND_DROP_KEY, true);
        commandWildcard.getChildren().put(COMMAND_DROP_KEY_ALT, true);
        commandWildcard.getChildren().put(COMMAND_OPEN, true);
        commandWildcard.getChildren().put(COMMAND_OPEN_OTHERS, true);
        commandWildcard.getChildren().put(COMMAND_OPEN_FOR, true);
        commandWildcard.getChildren().put(COMMAND_GIVE, true);
        commandWildcard.getChildren().put(COMMAND_KEY, true);
        commandWildcard.getChildren().put(COMMAND_KEY_GIVE, true);
        commandWildcard.getChildren().put(COMMAND_KEY_GIVEALL, true);
        commandWildcard.getChildren().put(COMMAND_KEY_TAKE, true);
        commandWildcard.getChildren().put(COMMAND_KEY_REMOVE, true);
        commandWildcard.getChildren().put(COMMAND_KEY_SET, true);
        commandWildcard.getChildren().put(COMMAND_KEY_INSPECT, true);
        commandWildcard.getChildren().put(COMMAND_KEY_BALANCE, true);
        commandWildcard.getChildren().put(COMMAND_KEY_INSPECT_OTHERS, true);
        commandWildcard.getChildren().put(COMMAND_KEY_BALANCE_OTHERS, true);
        commandWildcard.getChildren().put(COMMAND_KEY_REDEEM, true);
        commandWildcard.getChildren().put(COMMAND_MENU, true);
        commandWildcard.getChildren().put(COMMAND_MENU_OTHERS, true);
        commandWildcard.getChildren().put(COMMAND_PREVIEW, true);
        commandWildcard.getChildren().put(COMMAND_PREVIEW_OTHERS, true);
        commandWildcard.getChildren().put(COMMAND_RESETCOOLDOWN, true);
        commandWildcard.getChildren().put(CratePerms.COMMAND_ROOT.getName(), true);
        commandWildcard.getChildren().put(KeyPerms.COMMAND_ROOT.getName(), true);
        commandWildcard.recalculatePermissibles();

        // Plugin root wildcard: excellentcrates.*
        Permission pluginWildcard = getOrCreatePermission(pm, PLUGIN, PermissionDefault.OP);
        pluginWildcard.getChildren().put(COMMAND, true);
        pluginWildcard.getChildren().put(BYPASS, true);
        pluginWildcard.getChildren().put(MASS_OPEN, true);
        pluginWildcard.getChildren().put(INCLUDE_KEY_GIVEALL, true);
        pluginWildcard.getChildren().put(AUTO_REDEEM, true);
        pluginWildcard.getChildren().put(AUTO_REDEEM_KEY, true);
        pluginWildcard.recalculatePermissibles();
    }

    private static void link(PluginManager pm, String legacyName, Permission newPerm) {
        Permission legacyPerm = getOrCreatePermission(pm, legacyName, PermissionDefault.OP);
        legacyPerm.getChildren().put(newPerm.getName(), true);
        newPerm.getChildren().put(legacyPerm.getName(), true);
        legacyPerm.recalculatePermissibles();
        newPerm.recalculatePermissibles();
    }

    private static Permission getOrCreatePermission(PluginManager pm, String name, PermissionDefault defaultValue) {
        Permission permission = pm.getPermission(name);
        if (permission == null) {
            permission = new Permission(name, defaultValue);
            pm.addPermission(permission);
        }
        return permission;
    }
}
