package net.blay09.mods.unbreakables.rulesets;

import java.util.List;

public class JsonRulesetData {
    private List<String> rules;

    public List<String> getRules() {
        return rules;
    }

    public JsonRulesetData() {
    }

    public JsonRulesetData(List<String> rules) {
        this.rules = rules;
    }
}
