package net.blay09.mods.unbreakables.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class UnbreakablesAPI {

    private static final InternalMethods internalMethods;

    static {
        try {
            internalMethods = (InternalMethods) Class.forName("net.blay09.mods.unbreakables.InternalMethodsImpl").getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void registerParameterSerializer(Class<T> type, Function<String, T> deserializer) {
        internalMethods.registerParameterSerializer(type, deserializer);
    }

    public static <T> void registerDefaultParameterSerializer(Class<T> type) {
        internalMethods.registerDefaultParameterSerializer(type);
    }

    public static <P> void registerCondition(String name, Class<P> parameterType, BiFunction<BreakContext, P, Boolean> resolver) {
        internalMethods.registerCondition(name, parameterType, resolver);
    }

    public static <T extends BreakRequirement, P> void registerModifier(String name, RequirementType<T> requirementType, Class<P> parameterType, BreakModifierFunction<T, P> function, Supplier<Boolean> predicate) {
        internalMethods.registerModifier(name, requirementType, parameterType, function, predicate);
    }

    public static BreakRequirement resolveRequirements(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player) {
        return internalMethods.resolveRequirements(blockGetter, pos, state, player);
    }

    public static boolean testRequirements(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player) {
        return internalMethods.testRequirements(blockGetter, pos, state, player);
    }
}
