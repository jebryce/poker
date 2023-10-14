package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private static final int        MAX_NUM_KEYS = 256;
    private        final boolean[]  keyPressed   = new boolean[MAX_NUM_KEYS];
    private        final boolean[]  keyPressRead = new boolean[MAX_NUM_KEYS];
    private        final boolean[]  keyHeld      = new boolean[MAX_NUM_KEYS];
    private static       KeyHandler instance     = null;

    private KeyHandler() {

    }

    public static KeyHandler get() {
        if ( instance == null ) {
            instance = new KeyHandler();
        }
        return instance;
    }

    @Override
    public void keyTyped( KeyEvent event ) {}

    @Override
    public void keyPressed( KeyEvent event ) {
        int code = event.getKeyCode();
        assert code >= 0 && code < MAX_NUM_KEYS : "Key [" + code + "] not implemented!";
        if ( !keyPressed[code] ) {
            keyPressed[code]   = true;
            keyPressRead[code] = false;
        }
        keyHeld[code] = true;
    }

    @Override
    public void keyReleased( KeyEvent event ) {
        int code = event.getKeyCode();
        assert code >= 0 && code < MAX_NUM_KEYS : "Key [" + code + "] not implemented!";
        keyPressed[code] = false;
        keyHeld[code] = false;
    }

    public boolean isKeyPressed( final int keyCode ) {
        assert keyCode >= 0 && keyCode < MAX_NUM_KEYS : "Key [" + keyCode + "] not implemented!";
        if ( !keyPressRead[keyCode] && keyPressed[keyCode] ) {
            keyPressRead[keyCode] = true;
            return true;
        }
        return false;
    }

    public boolean isKeyHeld( final int keyCode ) {
        assert keyCode >= 0 && keyCode < MAX_NUM_KEYS : "Key [" + keyCode + "] not implemented!";
        return keyHeld[keyCode];
    }


}
