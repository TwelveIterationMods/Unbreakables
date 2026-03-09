package net.blay09.mods.unbreakables.api;

import net.blay09.mods.shogi.scope.ShogiScope;
import net.blay09.mods.unbreakables.rules.UnbreakablesRules;

public class UnbreakablesAPI {

    public static ShogiScope shogiScope() {
        return UnbreakablesRules.scope;
    }

    private UnbreakablesAPI() {
    }
}
