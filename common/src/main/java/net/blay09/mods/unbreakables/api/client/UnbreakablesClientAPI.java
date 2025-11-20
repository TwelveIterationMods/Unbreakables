package net.blay09.mods.unbreakables.api.client;

import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.resources.Identifier;

import java.lang.reflect.InvocationTargetException;

public class UnbreakablesClientAPI {

    private static final InternalClientMethods internalMethods;

    static {
        try {
            internalMethods = (InternalClientMethods) Class.forName("net.blay09.mods.unbreakables.client.InternalClientMethodsImpl").getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends BreakHint<T>> void registerHintRenderer(Identifier id, BreakHintRenderer<T> renderer) {
        internalMethods.registerHintRenderer(id, renderer);
    }
}
