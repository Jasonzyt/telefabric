package com.jasonzyt.telefabric.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Config {

    public Bot bot = new Bot();
    public Map<String, Command> commands = Collections.singletonMap("whitelist", new Command());
    public List<Long> chats = Collections.singletonList(-1001234567890L);
    public Features features = new Features();

    public static class Bot {
        public String username = "your_bot_username";
        public String token = "your_bot_token";
    }

    public static class Command {
        public String permission = "users"; // "all" or "users"
        public List<Long> allowed_users = Arrays.asList(123456789L, 987654321L);
    }

    public static class Features {
        public ChatForwarding chat_forwarding = new ChatForwarding();
        public JoinLeaveNotifications join_leave_notifications = new JoinLeaveNotifications();
        public DeathNotifications death_notifications = new DeathNotifications();
    }

    public static class ChatForwarding {
        public boolean enabled = true;
        public String format = "<%player%> %message%";
    }

    public static class JoinLeaveNotifications {
        public boolean enabled = true;
        public String join_format = "%player% joined the game";
        public String leave_format = "%player% left the game";
    }

    public static class DeathNotifications {
        public boolean enabled = true;
        public String format = "%death_message%";
    }

    private static final File configFile = new File("config/telefabric.yml");

    public static Config load() {
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(new Constructor(Config.class, new LoaderOptions()), representer);

        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(configFile);
                yaml.dump(new Config(), writer);
                return new Config();
            } catch (IOException e) {
                throw new RuntimeException("Could not create default config", e);
            }
        }

        try (FileReader reader = new FileReader(configFile)) {
            return yaml.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config", e);
        }
    }
}