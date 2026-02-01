package com.github.nautic;

import com.github.nautic.api.AtlasAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class AtlasAddonFileManager {

    public static void generateForAllLanguages() {
        Set<String> langs = AtlasAPI.getRegisteredLanguages();

        for (String lang : langs) {
            File dir = new File("plugins/AtlasLang/languages/" + lang);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, "atlasaddon.yml");

            if (!file.exists()) {
                YamlConfiguration cfg = new YamlConfiguration();

                cfg.set("menu.title", "Languages > <page>/<pages>");

                cfg.set("menu.language-item.material", "PAPER");
                cfg.set("menu.language-item.name", "&#35ADFF<lang>");
                cfg.set("menu.language-item.lore", List.of(
                        "&aClick to select"
                ));

                cfg.set("arrows.next.material", "ARROW");
                cfg.set("arrows.next.name", "&8->");
                cfg.set("arrows.next.lore", List.of(
                        "&fNext page"
                ));

                cfg.set("arrows.previous.material", "ARROW");
                cfg.set("arrows.previous.name", "&8<-");
                cfg.set("arrows.previous.lore", List.of(
                        "&fPrevious page"
                ));

                cfg.set("messages.selected", "&#35ADFF&lAtlasLang &#CDCDCD» &fLanguage selected &7(<lang>)");
                cfg.set("messages.invalid", "&#35ADFF&lAtlasLang &#CDCDCD» &cInvalid language!");


                try {
                    cfg.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
