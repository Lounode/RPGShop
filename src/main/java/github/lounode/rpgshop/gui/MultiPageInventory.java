package github.lounode.rpgshop.gui;

import github.lounode.rpgshop.gui.events.GUIOpenEvent;
import github.lounode.rpgshop.i18n.RPGI18N;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MultiPageInventory extends GUI {
    private int page;
    private ItemStack prevBtnSkin;
    private ItemStack nextBtnSkin;
    private List<Slot> functionSlots;
    private final boolean forceEnableFunctionSlots;

    public MultiPageInventory(GUIManager manager, int size, String title, boolean forceEnableFunctionSlots) {
        super(manager, size, title);
        this.functionSlots = new ArrayList<>();
        resetFuncSlots();
        this.page = 1;
        this.forceEnableFunctionSlots = forceEnableFunctionSlots;

        setDefaultButtonSkins();
        addListener(this::openCallbackAddPageButtons);
    }


    private boolean openCallbackAddPageButtons(GUIOpenEvent event) {
        if (isSinglePage()) {
            return true;
        }

        functionSlots.set(0, new Slot());
        functionSlots.set(8, new Slot());
        if (page > 1) {
            Button button = getPrevButton();
            setFunctionButton(0, button);
        }
        //Next
        if (getSize() - (page * 45) > 0) {
            Button button = getNextButton();
            setFunctionButton(8, button);
        }

        return true;
    }
    private void setDefaultButtonSkins () {
        //Prev
        ItemStack prevSkin = new ItemStack(Material.ARROW);
        ItemMeta prevButtonMeta = prevSkin.getItemMeta();
        prevButtonMeta.setDisplayName(RPGI18N.BUTTON_PREV.get());
        prevSkin.setItemMeta(prevButtonMeta);

        prevBtnSkin = prevSkin;
        //Next
        ItemStack nextButton = new ItemStack(Material.ARROW);
        ItemMeta nextButtonMeta = nextButton.getItemMeta();
        nextButtonMeta.setDisplayName(RPGI18N.BUTTON_NEXT.get());
        nextButton.setItemMeta(nextButtonMeta);

        nextBtnSkin = nextButton;
    }

    @Override
    public void open(Player player) {
        onOpen();
        if (isSinglePage()) {
            inventory = Bukkit.createInventory(getHolder(), getNextMultipleOfNine(getSize()), getTitle());

            for(int i = 0; i < getSize(); i++) {
                Slot slot = getSlots().get(i);
                ItemStack item = slot.getItemStack();
                if (item != null) {
                    inventory.setItem(i, item);
                }
            }

            player.openInventory(inventory);
            return;
        }
        //Multi
        inventory = Bukkit.createInventory(getHolder(), 54, getTitle());
        int startIndex = (page - 1) * 45;
        int endIndex = Math.min(startIndex + 45, getSize());

        for (int i = startIndex; i < endIndex; i++) {
            Slot slot = getSlots().get(i);
            ItemStack item = slot.getItemStack();
            if (item != null) {
                int index = i % 45;

                inventory.setItem(index, item);
            }
        }
        for(int i = 0; i < 9; i++) {
            Button button = getFuncSlot(i).getButton();
            if (button != null) {
                getInventory().setItem(45 + i, button.getSkin());
            }
        }

        player.openInventory(inventory);
    }

    public boolean btnEventNextPage(ButtonClickEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if (getSize() - (page * 45) > 0) {
            page++;
            open(player);
            return true;
        }
        return false;
    }
    public boolean btnEventPrevPage(ButtonClickEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if (page > 1) {
            page--;
            open(player);
            return true;
        }
        return false;
    }

    public boolean setFunctionButton(int index, Button button) {
        if (index > 8) {
            throw new IndexOutOfBoundsException("Invalid slot: " + index);
        }
        Slot slot = getFuncSlot(index);
        slot.setButton(button);

        return true;
    }

    public Slot getFuncSlot(int index) {
        return functionSlots.get(index);
    }
    public void setPrevButtonSkin(ItemStack itemStack) {
        this.prevBtnSkin = itemStack;
    }
    public void setNextButtonSkin(ItemStack itemStack) {
        this.nextBtnSkin = itemStack;
    }

    private Button getPrevButton() {
        Button prev = new Button(prevBtnSkin);
        prev.addClickListener(this::btnEventPrevPage);
        return prev;
    }
    private Button getNextButton() {
        Button next = new Button(nextBtnSkin);
        next.addClickListener(this::btnEventNextPage);
        return next;
    }

    public boolean isSinglePage() {
        if (forceEnableFunctionSlots) {
            return false;
        }
        return getSize() <= 54;
    }
    public void resetFuncSlots() {
        functionSlots.clear();
        for(int i = 1; i<= 9;i++) {
            this.functionSlots.add(new Slot());
        }
    }


    public Slot getSlot(int index) {
        int rawSlot = (page - 1) * 45 + index;
        if (index >= 45 && !isSinglePage()) {
            return getFuncSlot(index - 45);
        }

        try{
            Slot slot = getSlots().get(rawSlot);
            return slot;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    public int getPage () {
        return page;
    }
    public void setPage (int page) {
        this.page = page;
    }
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.isCancelled() && getHolder().equals(event.getInventory().getHolder())) {
            GUIHolder holder = (GUIHolder) event.getInventory().getHolder();
            MultiPageInventory gui = (MultiPageInventory) holder.getGUI();
            //event.getView().getPlayer().sendMessage(event.getInventory().getTitle()+": " + String.valueOf(event.getInventory().hashCode()));
            if (denyAnyClick) {
                event.setCancelled(true);
            }

            int rawSlot = event.getRawSlot();
            if (!isSinglePage() && rawSlot <= 53 && rawSlot >= 45) {
                event.setCancelled(true);
            }
            if (rawSlot > 53) {
                return;
            }


            Slot slot = gui.getSlot(rawSlot);
            if (slot == null) {
                return;
            }
            Button button = slot.getButton();
            if (button == null) {
                return;
            }

            event.setCancelled(true);
            ButtonClickEvent btnEvent = new ButtonClickEvent(event, this, button);
            button.click(btnEvent);
        }
    }
    @EventHandler
    void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof GUIHolder)) {
            return;
        }
        onInventoryClick(event);
    }


    public void onInventoryClose(InventoryCloseEvent event) {
        if (getHolder().equals(event.getInventory().getHolder())) {
            onClose();
        }
    }


    @EventHandler
    void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof GUIHolder)) {
            return;
        }
        onInventoryClose(event);
    }
}
