package me.encast.clara.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public enum ClaraEntitySubType {

    REGULAR(ChatColor.GREEN, ChatColor.DARK_GREEN),
    FLOOR_GUARDIAN(ChatColor.GOLD, ChatColor.YELLOW),
    FLOOR_BOSS(ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE);

    private ChatColor color;
    private ChatColor subColor;
}
