package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class KeyHandler implements KeyListener {
    public  boolean[]    numbersPressed = new boolean[10];
    private boolean      escapePressed, wPressed;
    private boolean      endGamePressed, placeWirePressed;


    @Override
    public void keyTyped( KeyEvent event ) {}

    @Override
    public void keyPressed( KeyEvent event ) {
        int code = event.getKeyCode();
        if ( code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9 ) {
            numbersPressed[code - KeyEvent.VK_0] = true;
        }
        if ( code == KeyEvent.VK_ESCAPE ) {
            escapePressed = true;
        }
        if ( code == KeyEvent.VK_W ) {
            wPressed = true;
        }
        checkKeyBinds();
    }

    @Override
    public void keyReleased( KeyEvent event ) {
        int code = event.getKeyCode();
        if ( code == KeyEvent.VK_ESCAPE ) {
            escapePressed = false;
        }
        if ( code == KeyEvent.VK_W ) {
            wPressed = true;
        }
        checkKeyBinds();
    }

    private void checkKeyBinds() {
        endGamePressed    = escapePressed;
        placeWirePressed  = wPressed;
    }

    public boolean isEndGamePressed() {
        if ( endGamePressed ) {
            endGamePressed = false;
            return true;
        }
        return false;
    }

    public boolean isPlaceWirePressed() {
        if ( placeWirePressed ) {
            placeWirePressed = false;
            return true;
        }
        return false;
    }

    public int getNumberPressed() {
        for( int index = 0; index < 10; index++ ) {
            if ( numbersPressed[index] ) {
                numbersPressed[index] = false;
                return index;
            }
        }
        return -1;
    }
}
