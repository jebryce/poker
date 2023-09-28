package Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseMotionListener, MouseListener {
    public int xPos;
    public int yPos;
    private boolean mouseClicked, mouseHeld;

    @Override
    public void mouseDragged( MouseEvent e ) {
    }

    @Override
    public void mouseMoved( MouseEvent e ) {
        xPos = (int) (e.getX() / Constants.SCREEN_SCALE);
        yPos = (int) (e.getY() / Constants.SCREEN_SCALE);
    }

    @Override
    public void mouseClicked( MouseEvent e ) {
        mouseClicked = true;
    }

    @Override
    public void mousePressed( MouseEvent e ) {
        mouseHeld = true;
    }

    @Override
    public void mouseReleased( MouseEvent e ) { mouseHeld = false; }

    @Override
    public void mouseEntered( MouseEvent e ) {}

    @Override
    public void mouseExited( MouseEvent e ) { }

    public boolean isMouseClicked() {
        if ( mouseClicked ) {
            mouseClicked = false;
            return true;
        }
        return false;
    }

    public boolean isMouseHeld() {
        return mouseHeld;
    }
}
