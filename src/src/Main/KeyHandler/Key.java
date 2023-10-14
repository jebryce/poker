package Main.KeyHandler;

import Container.ListItem;

public class Key extends ListItem {
    private final char key;

    public Key( final char key ) {
        this.key = key;
    }

    public char getKey() {
        return key;
    }
}
