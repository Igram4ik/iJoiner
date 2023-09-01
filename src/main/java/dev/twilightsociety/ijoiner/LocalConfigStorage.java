package dev.twilightsociety.ijoiner;

import dev.igrammine.commons.YamlConfig;

import java.util.Map;

public class LocalConfigStorage extends YamlConfig {
    @Ignore
    public static final LocalConfigStorage LOCAL = new LocalConfigStorage();

    @Comment("Список игроков и их сообщения")
    public Map<String, PLAYER> PLAYERS = Map.of(
            "Player1", createNodeSequence(PLAYER.class, "00000000-0000-0000-0000-000000000000", "default")
    );
    public static class PLAYER {
        public String UUID;
        @Comment(value = "Если default, то рандомный из конфига", at = Comment.At.SAME_LINE)
        public String TEXT;
    }
    public static class Manager {
        public static void addPlayer(String player, String UUID, String TEXT) {
            var newPlayer = new PLAYER();
            newPlayer.TEXT = TEXT;
            newPlayer.UUID = UUID;
            LOCAL.PLAYERS.put(player, newPlayer);
        }
    }
}
