package me.encast.clara.util.hologram;

import com.google.common.collect.Lists;
import me.encast.clara.entity.ClaraEntity;
import me.encast.clara.entity.ClaraEntitySubType;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.text.DecimalFormat;
import java.util.List;

public class ClaraEntityHologram extends Hologram {

    private ClaraEntity entity;
    private String[] animatedName;

    private static final DecimalFormat FORMAT = new DecimalFormat("#");

    public ClaraEntityHologram(Location location, ClaraEntity entity) {
        super(location);
        this.entity = entity;

        animateName();
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
        if(entity.getCurrentTick() % 10 >= 5) {
            setText(position, "  ");
            position++;
        }
        setText(position++, "§e| |");
        setText(position++, "§e| |");
        setText(position++, "§e| |");
        setText(position++, "§e\\ | | /");
        setText(position++, "§e\\ /");
        setText(position++, "§ev");

        if(entity.getCurrentTick() % 10 < 5) {
            setText(position++, "  ");
        }

        if(entity.getSubType() == ClaraEntitySubType.FLOOR_BOSS) {
            setText(position, "§7Alive Time: §c" + entity.getCurrentTick() / 20 + "s");
            position++;
        }

        setText(position, "§7[LVL x] " + animatedName[entity.getCurrentTick() % animatedName.length] +
                " §a" + FORMAT.format(el.getHealth()) + "§7/§c" + FORMAT.format(el.getMaxHealth()) +
                " §7- " + entity.getSubType().getColor() + "§l" +
                entity.getSubType().name().replaceAll("_", " "));
    }

    private void animateName() {
        final String name = entity.getName();
        if(name.length() >= 2) {
            int firstPos = 0;
            int secondPos = 1;
            List<String> names = Lists.newArrayList();
            String current;
            while (true) {
                if(firstPos < secondPos) {
                    current = entity.getSubType().getColor() + name.substring(firstPos, secondPos) +
                            entity.getSubType().getSubColor() + name.substring(secondPos);
                } else {
                    current = entity.getSubType().getSubColor() + name.substring(secondPos, firstPos) +
                            entity.getSubType().getColor() + name.substring(firstPos);
                }
                names.add(current);
                // Increasing first position to end, rolling over second position
                if(secondPos == name.length() - 1) {
                    secondPos = 0;
                } else if(secondPos != 0) {
                    secondPos++;
                }
                if(secondPos == 0)
                    firstPos++;

                if(firstPos >= name.length())
                    break;
            }
            animatedName = names.toArray(new String[0]);
        } else {
            animatedName = new String[] { entity.getSubType().getColor() + name };
        }
    }
}
