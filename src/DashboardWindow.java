import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DashboardWindow extends JFrame {
    private Housekeeper currentUser;    // Logged-in user
    private JTable table;               // Table UI
    private DefaultTableModel model;    // Data model for table
    private JTextField searchField;     // Search text field

    public DashboardWindow(Housekeeper user) {
        super("Smart Pantry – " + user.getName());
        this.currentUser = user;         // Save user
        buildUI();                       // Build UI
        setSize(1000, 500);
        setLocationRelativeTo(null);     // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        refreshAllItems();               // Load items into table
    }

    // Build the panels and buttons
    private void buildUI() {
        JPanel top = new JPanel();
        top.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        JButton btnAll = new JButton("All");
        JButton btnExpiring = new JButton("About to expire (15d)");
        JButton btnLow = new JButton("Low stock");
        top.add(searchField);
        top.add(btnSearch);
        top.add(btnAll);
        top.add(btnExpiring);
        top.add(btnLow);

        String[] cols = {"ID","Name","Category","Quantity","Unit","Threshold","Expiry"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);

        JPanel bottom = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnConsume = new JButton("Consume");
        JButton btnRestock = new JButton("Restock");
        JButton btnGenList = new JButton("Generate Shopping List");
        JButton btnMarkPurchased = new JButton("Mark Purchased (by name)");
        bottom.add(btnAdd);
        bottom.add(btnDelete);
        bottom.add(btnConsume);
        bottom.add(btnRestock);
        bottom.add(btnGenList);
        bottom.add(btnMarkPurchased);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Button actions using anonymous classes (beginner-friendly)
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        });

        btnAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshAllItems();
            }
        });

        btnExpiring.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showExpiring();
            }
        });

        btnLow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLowStock();
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelected();
            }
        });

        btnConsume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consumeSelected();
            }
        });

        btnRestock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restockSelected();
            }
        });

        btnGenList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateShoppingList();
            }
        });

        btnMarkPurchased.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markPurchasedByName();
            }
        });
    }

    // Load all items and show them
    private void refreshAllItems() {
        ArrayList<PantryItem> items = Storage.readItems();
        showItems(items);
    }

    // Display a list of items in the table
    private void showItems(ArrayList<PantryItem> items) {
        model.setRowCount(0);
        for (PantryItem it : items) {
            String exp = "";
            if (it instanceof PerishableItem) {
                PerishableItem p = (PerishableItem) it;
                if (p.getExpiryDate() != null) {
                    exp = p.getExpiryDate().toString();
                }
            }
            Object[] row = new Object[]{it.getId(), it.getName(), it.getCategory(), it.getQuantity(),
                    it.getUnit(), it.getThreshold(), exp};
            model.addRow(row);
        }
    }

    // Search items by name or category
    private void doSearch() {
        String q = searchField.getText().trim().toLowerCase();
        ArrayList<PantryItem> items = Storage.readItems();
        ArrayList<PantryItem> results = new ArrayList<PantryItem>();
        for (PantryItem it : items) {
            String n = it.getName().toLowerCase();
            String c = it.getCategory().toLowerCase();
            if (n.contains(q) || c.contains(q)) {
                results.add(it);
            }
        }
        showItems(results);
    }

    // Show items about to expire within 15 days
    private void showExpiring() {
        ArrayList<PantryItem> items = Storage.readItems();
        ArrayList<PantryItem> results = new ArrayList<PantryItem>();
        for (PantryItem it : items) {
            if (it instanceof PerishableItem) {
                PerishableItem p = (PerishableItem) it;
                if (p.isAboutToExpire(15)) {
                    results.add(it);
                }
            }
        }
        showItems(results);
    }

    // Show items that are below threshold
    private void showLowStock() {
        ArrayList<PantryItem> items = Storage.readItems();
        ArrayList<PantryItem> results = new ArrayList<PantryItem>();
        for (PantryItem it : items) {
            if (it.isLowStock()) {
                results.add(it);
            }
        }
        showItems(results);
    }

    // Add a new item using simple popups
    private void addItem() {
        String name = JOptionPane.showInputDialog(this, "Item name:");
        if (name == null || name.trim().isEmpty()) return;
        String cat = JOptionPane.showInputDialog(this, "Category:");
        if (cat == null) cat = "";
        String unit = JOptionPane.showInputDialog(this, "Unit (e.g., pcs, kg):");
        if (unit == null) unit = "pcs";
        int qty = askInt("Quantity (>=0):", 0);
        int th = askInt("Threshold (>=0):", 0);
        String exp = JOptionPane.showInputDialog(this, "Expiry (yyyy-MM-dd) or blank:");

        ArrayList<PantryItem> items = Storage.readItems();
        String id = Storage.makeId("I");
        if (exp != null && !exp.trim().isEmpty()) {
            LocalDate d = LocalDate.parse(exp.trim());
            PerishableItem it = new PerishableItem(id, name, cat, qty, unit, th, d);
            items.add(it);
        } else {
            PantryItem it = new PantryItem(id, name, cat, qty, unit, th);
            items.add(it);
        }
        Storage.writeItems(items);
        refreshAllItems();
    }

    // Delete selected item in table
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = (String) model.getValueAt(row, 0);
        ArrayList<PantryItem> items = Storage.readItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) { items.remove(i); break; }
        }
        Storage.writeItems(items);
        refreshAllItems();
    }

    // Consume some quantity from the selected item
    private void consumeSelected() {
        int row = table.getSelectedRow(); if (row < 0) return;
        String id = (String) model.getValueAt(row, 0);
        int amt = askInt("Consume amount:", 1);
        ArrayList<PantryItem> items = Storage.readItems();
        for (PantryItem it : items) {
            if (it.getId().equals(id)) {
                it.consume(amt);
                break;
            }
        }
        Storage.writeItems(items);
        refreshAllItems();
    }

    // Restock the selected item
    private void restockSelected() {
        int row = table.getSelectedRow(); if (row < 0) return;
        String id = (String) model.getValueAt(row, 0);
        int amt = askInt("Restock amount:", 1);
        ArrayList<PantryItem> items = Storage.readItems();
        for (PantryItem item : items) {
            if (item.getId().equals(id)) {
                item.restock(amt);
                break;
            }
        }
        Storage.writeItems(items);
        refreshAllItems();
    }

    // Generate a shopping list from low-stock items
    private void generateShoppingList() {
        ArrayList<PantryItem> items = Storage.readItems();
        ArrayList<ShoppingList> lists = Storage.readShoppingLists();
        String listId = Storage.makeId("L");
        ShoppingList list = new ShoppingList(listId, LocalDate.now());
        for (PantryItem item : items) {
            if (item.isLowStock()) {
                int needed = item.getThreshold() - item.getQuantity();
                if (needed > 0) {
                    ShoppingList.Entry e = new ShoppingList.Entry(item.getName(), needed, item.getUnit());
                    list.getEntries().add(e);
                }
            }
        }
        lists.add(list);
        Storage.writeShoppingLists(lists);
        JOptionPane.showMessageDialog(this, "Shopping List created: " + list.getId() + " (" + list.getEntries().size() + " entries)");
    }

    // Mark an item as purchased (by name) and restock pantry
    private void markPurchasedByName() {
        String name = JOptionPane.showInputDialog(this, "Enter item name purchased:");
        if (name == null || name.trim().isEmpty()) return;
        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity purchased:");
        int qty = 0; try { qty = Integer.parseInt(qtyStr); } catch (Exception e) { qty = 0; }
        if (qty <= 0) { JOptionPane.showMessageDialog(this, "Quantity must be > 0"); return; }

        // Mark entry purchased in latest shopping list, if present
        ArrayList<ShoppingList> lists = Storage.readShoppingLists();
        if (!lists.isEmpty()) {
            ShoppingList last = lists.get(lists.size() - 1);
            ArrayList<ShoppingList.Entry> es = last.getEntries();
            for (ShoppingList.Entry e : es) {
                if (e.itemName.equalsIgnoreCase(name)) {
                    e.status = "PURCHASED";
                    break;
                }
            }
            Storage.writeShoppingLists(lists);
        }

        // Restock pantry or add a new item if missing
        ArrayList<PantryItem> items = Storage.readItems();
        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            PantryItem it = items.get(i);
            if (it.getName().equalsIgnoreCase(name)) { it.restock(qty); found = true; break; }
        }
        if (!found) {
            String id = Storage.makeId("I");
            PantryItem it = new PantryItem(id, name, "Misc", qty, "pcs", 0);
            items.add(it);
        }
        Storage.writeItems(items);
        refreshAllItems();
    }

    // Ask the user for an integer using a popup, keep asking until valid
    private int askInt(String prompt, int def) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, prompt, def);
            if (s == null) return def;
            try {
                int v = Integer.parseInt(s);
                if (v < 0) { JOptionPane.showMessageDialog(this, "Please enter a non-negative integer."); }
                else { return v; }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            }
        }
    }
}
