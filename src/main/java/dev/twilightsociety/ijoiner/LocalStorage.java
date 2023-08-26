package dev.twilightsociety.ijoiner;

import dev.twilightsociety.ijoiner.commons.config.YamlConfig;

import java.util.Map;

public class LocalStorage extends YamlConfig {
    @Ignore
    public static final LocalStorage LOCAL = new LocalStorage();

    @Comment("Список игроков и их сообщения")
    public Map<String, PLAYER> PLAYERS = Map.of(
            "Player1", createNodeSequence(PLAYER.class, "00000000-0000-0000-0000-000000000000", "default")
    );
    public static class PLAYER {
        public String UUID;
        @Comment(value = "Если default, то рандомный из конфига", at = Comment.At.SAME_LINE)
        public String TEXT;
    }
}
