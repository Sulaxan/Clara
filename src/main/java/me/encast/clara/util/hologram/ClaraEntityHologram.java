package me.encast.clara.util.hologram;

import com.google.common.collect.Lists;
import me.encast.clara.entity.ClaraEntity;
import me.encast.clara.entity.ClaraEntityBoss;
import me.encast.clara.entity.ClaraEntitySubType;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.DecimalFormat;
import java.util.List;

public class ClaraEntityHologram extends Hologram {

    private ClaraEntity entity;

    private final String[] ANIMATED_NAME_TEXT;
    private final String[] ANIMATED_INCARNATION_TEXT;

    private static final DecimalFormat FORMAT = new DecimalFormat("#");

    public ClaraEntityHologram(Location location, ClaraEntity entity) {
        super(location);
        this.entity = entity;

        ANIMATED_NAME_TEXT = animate(
                entity.getName(),
                entity.getSubType().getColor(),
                entity.getSubType().getSubColor()
        );

        if(entity instanceof ClaraEntityBoss) {
            ClaraEntityBoss boss = (ClaraEntityBoss) entity;
            ANIMATED_INCARNATION_TEXT = animate(
                    ((ClaraEntityBoss) entity).getIncarnation().getName(),
                    ChatColor.DARK_GRAY,
                    ChatColor.GRAY
            );
        } else {
            ANIMATED_INCARNATION_TEXT = null;
        }
    }

    public void update() {
        EntityLiving el = entity.getEntity();
        if(getLocation().getX() != el.locX || getLocation().getY() != el.locY || getLocation().getZ() != el.locZ) {
            getLocation().setX(el.locX);
            getLocation().setY(el.locY + 2.25);
            getLocation().setZ(el.locZ);
            move(getLocation());
        }
        // Position of the information hologram
        int position = 0;
        if(entity.getSubType() == ClaraEntitySubType.FLOOR_BOSS) {
            setText(position, "§7" + entity.getCurrentTick() / 20 + "s");
            position++;
        }

        setText(position++, "§7[LVL x] " + ANIMATED_NAME_TEXT[entity.getCurrentTick() % ANIMATED_NAME_TEXT.length] +
                " §a" + FORMAT.format(el.getHealth()) + "§7/§c" + FORMAT.format(el.getMaxHealth()) +
                " §7- " + entity.getSubType().getColor() + "§l" +
                entity.getSubType().name().replaceAll("_", " "));
        if(ANIMATED_INCARNATION_TEXT != null)
            setText(position, ANIMATED_INCARNATION_TEXT[entity.getCurrentTick() % ANIMATED_INCARNATION_TEXT.length]);
    }

    private String[] animate(String text, ChatColor first, ChatColor second) {
        if(text.length() >= 2) {
            int firstPos = 0;
            int secondPos = 1;
            List<String> names = Lists.newArrayList();
            String current;
            while (true) {
                if(firstPos < secondPos) {
                    current = first + text.substring(firstPos, secondPos) +
                            second + text.substring(secondPos);
                } else {
                    current = second + text.substring(secondPos, firstPos) +
                            first + text.substring(firstPos);
                }
                names.add(current);
                // Increasing first position to end, rolling over second position
                if(secondPos == text.length() - 1) {
                    secondPos = 0;
                } else if(secondPos != 0) {
                    secondPos++;
                }
                if(secondPos == 0)
                    firstPos++;

                if(firstPos >= text.length())
                    break;
            }
            return names.toArray(new String[0]);
        } else {
            return new String[] { entity.getSubType().getColor() + text};
        }
    }
}
