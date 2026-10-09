# CoreTools

Paper 1.21.11 plugin. Requires Vault and an economy plugin (e.g. EssentialsX).

## Features
- Mobs and animals drop heads when killed by a player (chance and price per mob in `config.yml`).
- `/sellheads` sells every plugin-made head in your inventory. `/sellheads hand` sells only the held stack.
- `/heal [player]` and `/feed [player]` with cooldowns and permissions.
- Anti-farm: mobs from spawners, breeding, eggs and slime splits never drop heads.
- Only heads made by this plugin can be sold. `/give`d or player-placed heads are worthless.

## Commands and permissions
| Command | Permission | Default |
|---|---|---|
| `/sellheads [hand]` | `coretools.sell` | everyone |
| `/heal [player]` | `coretools.heal`, `coretools.heal.others` | op |
| `/feed [player]` | `coretools.feed`, `coretools.feed.others` | op |
| `/coretools reload` | `coretools.admin` | op |
| ignore cooldowns | `coretools.cooldown.bypass` | op |

Give `/heal` and `/feed` to ranks through LuckPerms if you want players to use them.

## Build the jar

**Option A: GitHub (nothing to install)**
1. Create a GitHub repository and upload this whole folder, including `.github`.
2. Open the Actions tab, run "Build", and download the `CoreTools` artifact. It contains the jar.

**Option B: your PC**
1. Install JDK 21 and Maven.
2. In this folder run `mvn clean package`.
3. The jar is `target/CoreTools-1.0.0.jar`.

## Install on ScalaCube
1. Make sure the server runs Paper 1.21.11.
2. Upload `CoreTools-1.0.0.jar`, Vault, and EssentialsX to `/plugins`.
3. Restart. Edit `plugins/CoreTools/config.yml`, then `/coretools reload`.

## Notes
- Prices and chances in `config.yml` are placeholders. Tune them against your server's income rates.
- Non-vanilla heads (cow, pig, etc.) are plain player heads named "Cow Head" unless you paste a base64 `texture` value for that mob in the config.
- Heal/feed cooldowns reset when the server restarts.
