# TeleFabric

A Fabric mod that bridges your Minecraft server with a Telegram group.

## Features

-   **Chat Bridging:** Forward in-game chat to your Telegram group and relay messages from Telegram back to the Minecraft server.
-   **Remote Commands:** Execute server commands (e.g., `/list`, `/whitelist`) directly from Telegram with a flexible permission system.
-   **Player Notifications:** Broadcast player join, leave, and death notifications to your Telegram chat.

## Build

```shell
./gradlew build
```

## Setup Guide

Follow these steps to get TeleFabric up and running.

### 1. Create a Telegram Bot

1.  Open Telegram and send a direct message to [@BotFather](https://t.me/BotFather).
2.  Send the `/newbot` command and follow the on-screen instructions.
    *   *Note: Pay attention to the distinction between the bot's **name** and **username**. You will need the username later.*
3.  Once the bot is created, **copy the bot token** provided by BotFather.
4.  Add your new bot to the Telegram group you want to link.

### 2. Install the Mod

1.  Download the latest `.jar` file from the [Releases](https://github.com/your-username/your-repo/releases) page.
2.  Place the `.jar` file into your server's `mods` directory (`/path/to/server/mods/`).

### 3. Initial Configuration

1.  Start your server once to generate the configuration file.
2.  Stop the server.
3.  Open and edit the new file at `/path/to/server/config/telefabric.yml`.
4.  Fill in the `bot_token` and `bot_username` fields with the credentials you received from BotFather.

### 4. Get Your Chat & User IDs

1.  Start your server again. The bot should now be online.
2.  In the Telegram group where you added the bot, type the command `/getid`.
3.  The bot will reply with the **Chat ID** and your personal **User ID**. Copy these values.
    *   *Important: Group Chat IDs start with a hyphen (`-`). Don't forget to include it.*

### 5. Finalize Configuration

1.  Stop the server and open `telefabric.yml` again.
2.  Add the Chat ID(s) and admin User ID(s) to the appropriate fields.

    ```yml
    # A list of chat IDs the bot will listen to and broadcast messages to.
    chats: [-100123456789, -1145141919810] # <-- Replace with your Chat ID(s)
  
    features:
      # ...
      bot_cmd_command:
        enabled: true
        commands:
          whitelist:
            # A list of user IDs allowed to execute this command from Telegram.
            allowed_users: [123456789] # <-- Replace with an admin's User ID
            permission: users
          # ...
      # ...
    ```

3.  Save the file and start your server. The bridge is now fully configured!
4.  If you need to apply changes later without restarting the server, you can use the `/telefabric reload` command in the server console.

## Troubleshooting

### Incorrect Bot Settings

-   **Symptom:** The bot responds to commands (like `/getid`) in a direct message but ignores all commands in the group chat.
-   **Solution:** This is likely due to the bot's privacy settings.
    1.  DM [@BotFather](https://t.me/BotFather) and send the `/mybots` command.
    2.  Select your bot from the list.
    3.  Navigate to `Bot Settings` -> `Group Privacy`.
    4.  Ensure that group privacy is **turned OFF**. When disabled, the bot can receive all messages sent in the group.

## Acknowledgements

This project was created in memory of a fellow developer.

While searching for a Fabric-Telegram bridge mod for Minecraft 1.21.4, I came across [CuteNekoOwO/FabricTgBridge](https://github.com/CuteNekoOwO/FabricTgBridge). I was deeply saddened to learn that its creator, MeowBot233, had passed away, and the project would remain on version 1.20 forever. 🕯️

Inspired by their work, I decided to create a new mod with similar functionality for modern versions of Minecraft. And so, TeleFabric was born.

## Dependencies

This project relies on the following open-source libraries:

-   [**Minecraft**](https://www.minecraft.net/en-us/about-minecraft) - Proprietary License
-   [**Fabric**](https://fabricmc.net/) - Apache License 2.0
-   [**TelegramBots**](https://github.com/rubenlagus/TelegramBots) - MIT License
-   [**SnakeYAML**](https://github.com/snakeyaml/snakeyaml) - Apache License 2.0
