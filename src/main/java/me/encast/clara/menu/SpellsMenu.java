package me.encast.clara.menu;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.Tuple;
import me.encast.clara.util.inventory.invx.*;
import me.encast.clara.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.function.Consumer;

public class SpellsMenu implements NullInv {

    private ClaraPlayer player;
    private PageableInventory pageable;

    private LayerInventory circleSpellsInv;
    private LayerInventory fullDisplaySpellsInv;

    private UpdateableInventory spellsUpdater;

    private List<Tuple<ItemBuilder, Consumer<ItemBuilder>>> items = Lists.newArrayListWithCapacity(5);

    private static final char[] MAPPING_KEYS = {'1', '2', '3', '4', '5'};

    public SpellsMenu(ClaraPlayer player) {
        this.player = player;
        this.pageable = new PageableInventory();
        // Circle spells menu
        this.circleSpellsInv = Clara.getInstance().getInventoryManager().constructLayerInv(
                null,
                null,
                ctx -> ctx.setCancel(true)
        )
                .withLayer("         ")
                .withLayer("    1    ")
                .withLayer(" 2     3 ")
                .withLayer("         ")
                .withLayer("  4   5  ")
                .withLayer("        s")
                .map('s', ItemContext.of(new ItemBuilder(Material.EYE_OF_ENDER, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§aGo back to Spells")
                        .build(),
                        click -> {
                            spellsUpdater.cancel();
                            pageable.setCurrentPage(0);
                            pageable.openInv(player.getBukkitPlayer());
                        }));
        addItems();
        mapItems();

        this.fullDisplaySpellsInv = Clara.getInstance().getInventoryManager().constructLayerInv(
                null,
                null,
                ctx -> ctx.setCancel(true)
        )
                .withLayer("    s    ")
                .withLayer(" iiiiiii ")
                .withLayer(" iiiiiii ")
                .withLayer(" iiiiiii ")
                .withLayer(" iiiiiii ")
                .withLayer("    h   a")
                .map('s', ItemContext.of(new ItemBuilder(Material.EYE_OF_ENDER, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§aSpells")
                        .build()))
                .map('i', ItemContext.of(new ItemBuilder(Material.STAINED_GLASS, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§6Spell: N/A")
                        .setData((short) 1)
                        .build()))
                .map('h', ItemContext.of(new ItemBuilder(Material.LONG_GRASS, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§cClose")
                        .setData((short) 2)
                        .build(),
                        click -> click.getPlayer().closeInventory()))
                .map('a', ItemContext.of(new ItemBuilder(Material.ENDER_PEARL, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§aGo to Spell Alt Menu")
                        .build(),
                        click -> {
                            pageable.setCurrentPage(1);
                            spellsUpdater.setInterval(0, 10);
                            pageable.openInv(player.getBukkitPlayer());
                        }))
                .apply();


        pageable.add(spellsUpdater = new UpdateableInventory(circleSpellsInv, Clara.getInstance().getInventoryManager(), player.getBukkitPlayer(), 0, 10));
        pageable.add(fullDisplaySpellsInv);
    }

    public void addItems() {
        ItemBuilder spell = new ItemBuilder(Material.INK_SACK, ItemBuilder.ItemType.NORMAL);
        items.add(new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell : N/A")
                        .setData((short) 1)
                        .addLore(" ", "§7You've stumbled upon an", "§7unimplemented spell!"),
                builder -> {}
        ));
        items.add(new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 2: N/A")
                        .setData((short) 1)
                        .addLore(" ", "§7You've stumbled upon an", "§7unimplemented spell!"),
                builder -> {}
        ));
        items.add(new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 3: N/A")
                        .setData((short) 1)
                        .addLore(" ", "§7You've stumbled upon an", "§7unimplemented spell!"),
                builder -> {}
        ));
        items.add(new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 4: N/A")
                        .setData((short) 1)
                        .addLore(" ", "§7You've stumbled upon an", "§7unimplemented spell!"),
                builder -> {}
        ));
        items.add(new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 5: N/A")
                        .setData((short) 1)
                        .addLore(" ", "§7You've stumbled upon an", "§7unimplemented spell!"),
                builder -> {}
        ));
    }

    public void mapItems() {
        for(int i = 0; i < MAPPING_KEYS.length; i++) {
            if(i < items.size()) {
                Tuple<ItemBuilder, Consumer<ItemBuilder>> tuple = items.get(i);
                // Update the builder
                tuple.getB().accept(tuple.getA());
                circleSpellsInv.map(MAPPING_KEYS[i], ItemContext.of(tuple.getA().build()));
            }
        }
        // Taking the first element and moving it to the end
        items.add(items.remove(0));
    }

    @Override
    public Inventory build() {
        return pageable.build();
    }
}
