package su.nightexpress.excellentcrates.reward.items.processor;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantProcessor;
import su.nightexpress.excellentcrates.api.reward.items.RewardItemsComponent;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RewardItemsGrantProcessor implements RewardGrantProcessor {

    private final CratesPlugin plugin;

    public RewardItemsGrantProcessor(CratesPlugin plugin) {
        this.plugin = plugin;
    }

    public RewardItemsGrantProcessor() {
        this((CratesPlugin) JavaPlugin.getProvidingPlugin(RewardItemsGrantProcessor.class));
    }

    @Override
    public void execute(Player player, Crate crate, Reward reward, PlaceholderContext placeholders) {
        RewardItemsComponent component = reward.getComponentOrNull(RewardComponentKeys.ITEMS);
        if (component == null || component.isEmpty()) return;

        this.plugin.runTask(player, () -> {
            component.getItems().forEach(provider -> {
                ItemStack itemStack = provider.getItemStack();
                if (itemStack == null) return;

                Players.addItem(player, itemStack);
            });
        });
    }
}
