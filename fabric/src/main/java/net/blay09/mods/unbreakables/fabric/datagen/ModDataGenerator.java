package net.blay09.mods.unbreakables.fabric.datagen;

import net.blay09.mods.balm.client.platform.util.I18nExport;
import net.blay09.mods.unbreakables.Unbreakables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.io.File;

public class ModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final var pack = fabricDataGenerator.createPack();
        pack.addProvider(ModBlockTagProvider::new);
        I18nExport.writeStaticI18nKeys(Unbreakables.MOD_ID, new File("i18n.export.json"));
    }

}
