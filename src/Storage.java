import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Storage {

    // file names
    public static String USERS_FILE = "users.txt";
    public static String ITEMS_FILE = "items_.txt";
    public static String SHOPPING_FILE = "shopping_lists.txt";

    // Ensure all needed files exist.
    public static void ensureFiles() {
        try {
            File u = new File(USERS_FILE); if (!u.exists()) u.createNewFile();
            File i = new File(ITEMS_FILE); if (!i.exists()) i.createNewFile();
            File s = new File(SHOPPING_FILE); if (!s.exists()) s.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Read all users from users.txt into a list.
    public static ArrayList<Housekeeper> readUsers() {
        ArrayList<Housekeeper> list = new ArrayList<Housekeeper>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(USERS_FILE));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    Housekeeper u = new Housekeeper(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    list.add(u);
                }
            }
            br.close();
        } catch (IOException e) {
            // Ignore read errors here for simplicity. <--- ??????????
        }
        return list;
    }

    // Write all users to users.txt (overwrites file).
    public static void writeUsers(ArrayList<Housekeeper> users) {
        try {
            // TODO: CHANGE buffered writer to FileWriter or PrintWriter
            BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE));
            for (Housekeeper u : users) {
                String row = u.getId() + "," + u.getName() + "," + u.getEmail() + "," + u.getPhone() + "," + u.getPassword();
                bw.write(row);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<PantryItem> readItems() {
        ArrayList<PantryItem> items = new ArrayList<PantryItem>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(ITEMS_FILE));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // Format: itemId,name,category,quantity,unit,threshold,expiry
                String[] p = line.split(",", -1);
                if (p.length >= 7) {
                    String id = p[0];
                    String name = p[1];
                    String cat = p[2];
                    int qty = toInt(p[3]);
                    String unit = p[4];
                    int th = toInt(p[5]);
                    String exp = p[6];
                    if (exp != null && exp.length() > 0) {
                        LocalDate d = LocalDate.parse(exp);
                        PerishableItem it = new PerishableItem(id, name, cat, qty, unit, th, d);
                        items.add(it);
                    } else {
                        PantryItem it = new PantryItem(id, name, cat, qty, unit, th);
                        items.add(it);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            // Ignore errors for simplicity.
        }
        return items;
    }

    // Write all items to items.txt.
    public static void writeItems(ArrayList<PantryItem> items) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(ITEMS_FILE));
            for (PantryItem it : items) {
                String exp = "";
                if (it instanceof PerishableItem) {
                    PerishableItem p = (PerishableItem) it;
                    if (p.getExpiryDate() != null) {
                        exp = p.getExpiryDate().toString();
                    }
                }
                String row = it.getId() + "," + it.getName() + "," + it.getCategory() + "," + it.getQuantity() + "," + it.getUnit() + "," + it.getThreshold() + "," + exp;
                bw.write(row);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Read all shopping lists from shopping_lists.txt.
    public static ArrayList<ShoppingList> readShoppingLists() {
        ArrayList<ShoppingList> lists = new ArrayList<ShoppingList>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(SHOPPING_FILE));
            String line;
            ShoppingList current = null; // Keep track of the list we are filling
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // Lines look like: LIST|listId|yyyy-MM-dd  OR  ENTRY|name|qty|unit|status
                String[] parts = line.split(",", -1);
                if (parts[0].equals("LIST")) {
                    String id = parts[1];
                    LocalDate date = LocalDate.parse(parts[2]);
                    current = new ShoppingList(id, date);
                    lists.add(current);
                } else if (parts[0].equals("ENTRY")) {
                    if (current != null) {
                        String name = parts[1];
                        int qty = toInt(parts[2]);
                        String unit = parts[3];
                        String status = parts[4];
                        ShoppingList.Entry e = new ShoppingList.Entry(name, qty, unit);
                        e.status = status;
                        current.getEntries().add(e);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            // Ignore errors for simplicity.
        }
        return lists;
    }

    // Write all shopping lists to shopping_lists.txt.
    public static void writeShoppingLists(ArrayList<ShoppingList> lists) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(SHOPPING_FILE));
            for (ShoppingList l : lists) {
                bw.write("LIST|" + l.getId() + "|" + l.getDateCreated());
                bw.newLine();
                ArrayList<ShoppingList.Entry> es = l.getEntries();
                for (ShoppingList.Entry e : es) {
                    bw.write("ENTRY|" + e.itemName + "|" + e.quantity + "|" + e.unit + "|" + e.status);
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper: convert String to int safely.
    public static int toInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    // Make a short ID using a random UUID.
    public static String makeId(String prefix) {
        String uuid = java.util.UUID.randomUUID().toString();
        String shortId = uuid.substring(0, 8);
        return prefix + "-" + shortId;
    }
}
