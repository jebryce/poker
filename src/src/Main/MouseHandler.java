package Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseMotionListener, MouseListener {
    public int xPos;
    public int yPos;
    private final boolean[] mouseClicked   = new boolean[4];
    private final boolean[] mouseClickRead = new boolean[4];
    private final boolean[] mouseHeld      = new boolean[4];

    @Override
    public void mouseDragged( MouseEvent e ) {
        xPos = (int) ( e.getX() / Constants.SCREEN_SCALE );
        yPos = (int) ( e.getY() / Constants.SCREEN_SCALE );
    }

    @Override
    public void mouseMoved( MouseEvent e ) {
        xPos = (int) ( e.getX() / Constants.SCREEN_SCALE );
        yPos = (int) ( e.getY() / Constants.SCREEN_SCALE );
    }

    @Override
    public void mouseClicked( MouseEvent e ) {
    }

    @Override
    public void mousePressed( MouseEvent e ) {
        int button = e.getButton();

        if ( !mouseClicked[button] ) {
            mouseClicked[button]   = true;
            mouseClickRead[button] = false;
        }
        mouseHeld[button] = true;
    }

    @Override
    public void mouseReleased( MouseEvent e ) {
        int button = e.getButton();
        mouseHeld[button]    = false;
        mouseClicked[button] = false;
    }

    @Override
    public void mouseEntered( MouseEvent e ) {}

    @Override
    public void mouseExited( MouseEvent e ) { }

    private boolean isMouseClicked( final int button ) {
        if ( !mouseClickRead[button] && mouseClicked[button] ) {
            mouseClickRead[button] = true;
            return true;
        }
        return false;
    }

    public boolean isMouseLeftClicked() {
        return isMouseClicked( 1 );
    }

    public boolean isMouseHeld() {
        return mouseHeld[1];
    }

    public boolean isMouseClickRead() {
        return mouseClickRead[1];
    }

    public boolean isMouseRightClicked() {
        return isMouseClicked( 3 );
    }
}
