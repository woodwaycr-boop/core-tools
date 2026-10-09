package dev.coretools;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Handles /heal and /feed. Cooldowns are kept in memory and reset on restart. */
final class HealFeedCommands implements CommandExecutor {

    private final CoreTools plugin;
    private final Map<UUID, Long> healCooldowns = new HashMap<>();
    private final Map<UUID, Long> feedCooldowns = new HashMap<>();

    HealFeedCommands(CoreTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean heal = command.getName().equalsIgnoreCase("heal");
        String name = heal ? "heal" : "feed";
        Messages messages = plugin.messages();

        if (!sender.hasPermission("coretools." + name)) {
            messages.send(sender, "no-permission");
            return true;
        }

        Player target;
        boolean self;
        if (args.length >= 1) {
            if (!sender.hasPermission("coretools." + name + ".others")) {
                messages.send(sender, "no-permission");
                return true;
            }
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                messages.send(sender, "player-not-found");
                return true;
            }
            self = sender instanceof Player p && p.getUniqueId().equals(target.getUniqueId());
        } else {
            if (!(sender instanceof Player p)) {
                messages.send(sender, "usage-" + name);
                return true;
            }
            target = p;
            self = true;
        }

        // Cooldown only applies when a player uses it on themselves.
        if (self && !sender.hasPermission("coretools.cooldown.bypass")) {
            Map<UUID, Long> cooldowns = heal ? healCooldowns : feedCooldowns;
            long seconds = plugin.getConfig().getLong("cooldowns." + name, 0L);
            long now = System.currentTimeMillis();
            Long readyAt = cooldowns.get(target.getUniqueId());
            if (readyAt != null && readyAt > now) {
                long remaining = (readyAt - now + 999) / 1000;
                messages.send(sender, "cooldown", Placeholder.unparsed("seconds", String.valueOf(remaining)));
                return true;
            }
            if (seconds > 0) {
                cooldowns.put(target.getUniqueId(), now + seconds * 1000);
            }
        }

        if (heal) {
            AttributeInstance maxHealth = target.getAttribute(Attribute.MAX_HEALTH);
            target.setHealth(maxHealth != null ? maxHealth.getValue() : 20.0);
            target.setFireTicks(0);
        } else {
            target.setFoodLevel(20);
            target.setSaturation((float) plugin.getConfig().getDouble("feed-saturation", 20.0));
        }

        if (self) {
            messages.send(target, heal ? "healed" : "fed");
        } else {
            messages.send(sender, heal ? "healed-other" : "fed-other",
                    Placeholder.unparsed("player", target.getName()));
            messages.send(target, heal ? "healed" : "fed");
        }
        return true;
    }
}
