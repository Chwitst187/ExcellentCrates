package su.nightexpress.excellentcrates.reward.items.processor;

import org.bukkit.Bukkit;
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

    private final JavaPlugin plugin;

    public RewardItemsGrantProcessor() {
        this.plugin = JavaPlugin.getProvidingPlugin(RewardItemsGrantProcessor.class);
    }

    @Override
    public void execute(Player player, Crate crate, Reward reward, PlaceholderContext placeholders) {
        RewardItemsComponent component = reward.getComponentOrNull(RewardComponentKeys.ITEMS);
        if (component == null || component.isEmpty()) return;

        Bukkit.getScheduler().runTask(this.plugin, () -> {
            component.getItems().forEach(provider -> {
                ItemStack itemStack = provider.getItemStack();
                if (itemStack == null) return;

                Players.addItem(player, itemStack);
            });
        });
    }
}
