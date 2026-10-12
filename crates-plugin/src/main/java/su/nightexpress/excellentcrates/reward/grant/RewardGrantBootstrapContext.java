package su.nightexpress.excellentcrates.reward.grant;

import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.grant.RegisteredGrantProcessor;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantAPI;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.grant.pipeline.RewardGrantPipelineExecutor;

@NullMarked
public class RewardGrantBootstrapContext {

    public final RewardGrantService grantService;
    public final RewardGrantAPI     api;

    private final RewardGrantPipelineExecutor pipelineExecutor;

    public RewardGrantBootstrapContext(CratesPlugin plugin,
                                       CratePlaceholders cratePlaceholders,
                                       RewardPlaceholders rewardPlaceholders,
                                       RewardMessageDispatcher dispatcher) {
        TinyRegistry<RegisteredGrantProcessor> processors = new SimpleRegistry<>();

        this.grantService = new RewardGrantService(
            processors, cratePlaceholders, rewardPlaceholders, dispatcher
        );
        this.api = new DefaultRewardGrantAPI(processors, grantService);

        this.pipelineExecutor = new RewardGrantPipelineExecutor(plugin, this.grantService);
    }

    public RewardGrantBootstrapContext(CratePlaceholders cratePlaceholders,
                                       RewardPlaceholders rewardPlaceholders,
                                       RewardMessageDispatcher dispatcher) {
        this((CratesPlugin) JavaPlugin.getProvidingPlugin(RewardGrantBootstrapContext.class),
            cratePlaceholders, rewardPlaceholders, dispatcher);
    }

    public RewardGrantPipelineExecutor getPipelineExecutor() {
        return this.pipelineExecutor;
    }
}
