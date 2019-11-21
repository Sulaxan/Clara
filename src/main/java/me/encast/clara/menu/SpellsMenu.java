package me.encast.clara.menu;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.Tuple;
import me.encast.clara.util.inventory.invx.*;
import me.encast.clara.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.function.Consumer;

public class SpellsMenu implements NullInv {

    private ClaraPlayer player;
    private PageableInventory pageable;

    private LayerInventory circleSpellsInv;
    private LayerInventory fullDisplaySpellsInv;

    private UpdateableInventory spellsUpdater;

    private Tuple<ItemBuilder, Consumer<ItemBuilder>>[] items = new Tuple[5];

    private static final char[] MAPPING_KEYS = {'1', '2', '3', '4', '5'};

    public SpellsMenu(ClaraPlayer player) {
        this.player = player;
        this.pageable = new PageableInventory();
        // Circle spells menu
        this.circleSpellsInv = Clara.getInstance().getInventoryManager().constructLayerInv(
                (p, session) -> spellsUpdater.setRunning(true),
                (p, session) -> {
                    if(pageable.isSwitchingPages()) { // pageable is not switching pages
                        spellsUpdater.setRunning(false);
                    } else if(!spellsUpdater.isUpdating()) { // updateable inv is not updating
                        spellsUpdater.cancel();
                    }
                },
                ctx -> ctx.setCancel(true)
        )
                .withLayer("         ")
                .withLayer("    1    ")
                .withLayer(" 2     5 ")
                .withLayer("         ")
                .withLayer("  3   4  ")
                .withLayer("    s    ")
                .map('s', ItemContext.of(new ItemBuilder(Material.EYE_OF_ENDER, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§aGo back to Spells")
                        .build(),
                        click -> {
                            spellsUpdater.setRunning(false);
                            click.addEndProcess(() -> pageable.openAndSetNext(player.getBukkitPlayer(), 0));
                        }));
        circleSpellsInv.setName("Spells Alt");
        addItems();
        mapItems();

        this.fullDisplaySpellsInv = Clara.getInstance().getInventoryManager().constructLayerInv(
                (p, session) -> spellsUpdater.setRunning(false),
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
                        click -> click.addEndProcess(() -> pageable.openAndSetNext(player.getBukkitPlayer()))))
                .apply();
        fullDisplaySpellsInv.setName("Spells");

        pageable.add(fullDisplaySpellsInv);
        pageable.add(spellsUpdater = new UpdateableInventory(circleSpellsInv, Clara.getInstance().getInventoryManager(), player.getBukkitPlayer(), 0, 20));
        spellsUpdater.setRunning(false);
        spellsUpdater.setUpdateFunction(inv -> {
            mapItems();
            return inv;
        });
    }

    public void addItems() {
        ItemBuilder spell = new ItemBuilder(Material.INK_SACK, ItemBuilder.ItemType.NORMAL);
        items[0] = new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 1: N/A")
                        .setData((short) 8)
                        .setLore(Lists.newArrayList(" ", "§7You've stumbled upon an", "§7unimplemented spell!")),
                builder -> {}
        );
        items[1] = new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 2: N/A")
                        .setData((short) 9)
                        .setLore(Lists.newArrayList(" ", "§7You've stumbled upon an", "§7unimplemented spell!")),
                builder -> {}
        );
        items[2] = new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 3: N/A")
                        .setData((short) 13)
                        .setLore(Lists.newArrayList(" ", "§7You've stumbled upon an", "§7unimplemented spell!")),
                builder -> {}
        );
        items[3] = new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 4: N/A")
                        .setData((short) 5)
                        .setLore(Lists.newArrayList(" ", "§7You've stumbled upon an", "§7unimplemented spell!")),
                builder -> {}
        );
        items[4] = new Tuple<>(
                spell.clone()
                        .setDisplayName("§aSpell 5: N/A")
                        .setData((short) 10)
                        .setLore(Lists.newArrayList(" ", "§7You've stumbled upon an", "§7unimplemented spell!")),
                builder -> {}
        );
    }

    public void mapItems() {
        for(int i = 0; i < MAPPING_KEYS.length; i++) {
            if(i < items.length) {
                Tuple<ItemBuilder, Consumer<ItemBuilder>> tuple = items[i];
                // Update the builder
                tuple.getB().accept(tuple.getA());
                if(i == 0) {
                    tuple.getA()
                            .addEnchantment(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS);
                }
                circleSpellsInv.map(MAPPING_KEYS[i], ItemContext.of(tuple.getA().build()));
                tuple.getA().getEnchantments().clear();
                tuple.getA().getItemFlags().remove(ItemFlag.HIDE_ENCHANTS);
            }
        }
        circleSpellsInv.apply();
        // Taking the first element and moving it to the end
        Tuple<ItemBuilder, Consumer<ItemBuilder>> temp = items[0];
        for(int i = 1; i < items.length; i++) {
            items[i - 1] = items[i];
        }
        items[items.length - 1] = temp;
    }

    @Override
    public List<ItemContext> getItems() {
        return pageable.getItems();
    }

    @Override
    public Inventory build() {
        return pageable.build();
    }
}
