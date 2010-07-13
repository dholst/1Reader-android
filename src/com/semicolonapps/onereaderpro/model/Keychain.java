package com.semicolonapps.onereaderpro.model;

import com.semicolonapps.onepassword.Item;
import com.semicolonapps.onepassword.OnePasswordKeychain;

import java.util.*;

public class Keychain {
    private static final String LOGINS = "Logins";
    private static final String IDENTITIES = "Identities";
    private static final String NOTES = "Notes";
    private static final String SOFTWARE = "Software";
    private static final String WALLET = "Wallet";
    private static final String PASSWORDS = "Passwords";
    private static final String TRASH = "Trash";
    private static final List<String> GROUPS = Arrays.asList(LOGINS, IDENTITIES, NOTES, SOFTWARE, WALLET, PASSWORDS, TRASH);

    private static OnePasswordKeychain keychain;

    public static void setInstance(OnePasswordKeychain keychain) {
        Keychain.keychain = keychain;
    }

    public static OnePasswordKeychain getInstance() {
        return keychain;
    }

    public static List<String> groups() {
        return GROUPS;
    }

    public static List groupFor(String name) {
        if(name.equals(LOGINS)) return sort(getInstance().logins());
        if(name.equals(IDENTITIES)) return sort(getInstance().identities());
        if(name.equals(NOTES)) return sort(getInstance().notes());
        if(name.equals(SOFTWARE)) return sort(getInstance().licenses());
        if(name.equals(WALLET)) return sort(getInstance().walletItems());
        if(name.equals(PASSWORDS)) return sort(getInstance().passwords());
        if(name.equals(TRASH)) return sort(getInstance().trash());
        throw new RuntimeException("Unknown group - " + name);
    }

    public static List search(String query) {
        List items = new ArrayList();
        search(items, getInstance().logins(), query);
        search(items, getInstance().identities(), query);
        search(items, getInstance().notes(), query);
        search(items, getInstance().licenses(), query);
        search(items, getInstance().walletItems(), query);
        search(items, getInstance().passwords(), query);
        return sort(items);
    }

    private static void search(List results, List items, String query) {
        for(Iterator it = items.iterator(); it.hasNext();) {
            Item item = (Item) it.next();

            if(item.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(item);
            }
        }
    }

    private static List sort(List list) {
        Collections.sort(list, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return item1.getName().toLowerCase().compareTo(item2.getName().toLowerCase());
            }
        });

        return list;
    }
}
