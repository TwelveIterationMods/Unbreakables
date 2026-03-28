package net.blay09.mods.unbreakables.rules;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import net.blay09.mods.balm.platform.event.callback.ConfigCallback;
import net.blay09.mods.shogi.Shogi;
import net.blay09.mods.shogi.ShogiValue;
import net.blay09.mods.shogi.coercion.Coercion;
import net.blay09.mods.shogi.common.effect.compose.AggregateEffect;
import net.blay09.mods.shogi.context.MutableShogiContext;
import net.blay09.mods.shogi.common.parse.ShogiRuleParser;
import net.blay09.mods.shogi.effect.ShogiEffect;
import net.blay09.mods.shogi.scope.ShogiScope;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.UnbreakablesConfig;
import net.blay09.mods.unbreakables.rules.hint.ShogiHintFactory;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import static net.blay09.mods.unbreakables.Unbreakables.MOD_ID;

public class UnbreakablesRules {

    private static @Nullable ShogiEffect<?> cachedBreakRequirements;

    public static final ShogiScope scope = Shogi.scope(id("rules"), it -> it.setDefaultNamespaces(List.of(MOD_ID, "shogi")));

    public static final ShogiValue<MutableShogiContext, List<?>> breakRequirements = scope.maybe(id("break_requirements"), UnbreakablesRules::evaluateBreakRequirements)
            .coerce(Coercion.LIST);

    public static void initialize() {
        ConfigCallback.Reloaded.EVENT.register(schema -> {
            if (schema.identifier().equals(id("common"))) {
                cachedBreakRequirements = null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static Either<List<Object>, List<Object>> evaluateBreakRequirements(MutableShogiContext context) {
        if (!(context.entity() instanceof ServerPlayer)) {
            return Either.left(List.of());
        }

        Either<List<Object>, List<Object>> result;
        try {
            result = (Either<List<Object>, List<Object>>) (Either<?, ?>) getOrBuildBreakRequirementsEffect(context).apply(context)
                    .mapLeft(Coercion.LIST)
                    .mapRight(Coercion.LIST);
            if (result.right().filter(ShogiHintFactory::isDeferredOnlyFailure).isPresent()) {
                result = result.swap();
            }
        } catch (Throwable t) {
            result = Either.right(List.of(t));
        }
        return result;
    }

    private static ShogiEffect<?> getOrBuildBreakRequirementsEffect(MutableShogiContext context) {
        final var level = context.requireLevel();
        if (cachedBreakRequirements == null) {
            final var registryOps = RegistryOps.create(JsonOps.INSTANCE, level.registryAccess());
            final List<ShogiEffect<?>> rules = UnbreakablesConfig.getActive().rules.stream()
                    .filter(rule -> !rule.isBlank())
                    .peek(rule -> Unbreakables.logger.info("Loading breakable rule {}", rule))
                    .map(rule -> new ParsedRule(rule, ShogiRuleParser.parse(scope, registryOps, rule)))
                    .filter(parsedRule -> {
                        final var result = parsedRule.result();
                        result.error().ifPresent(error -> Unbreakables.logger.error("Invalid breakable rule {}: {}", parsedRule.rule(), error.message()));
                        return result.isSuccess();
                    })
                    .map(parsedRule -> parsedRule.result().result().orElseThrow())
                    .collect(Collectors.toList());
            cachedBreakRequirements = AggregateEffect.withAutoApplied(scope, registryOps, rules);
            Unbreakables.logger.info("{} breakable rules loaded", rules.size());
        }
        return cachedBreakRequirements;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    private UnbreakablesRules() {
    }

    private record ParsedRule(String rule, com.mojang.serialization.DataResult<ShogiEffect<?>> result) {
    }
}
