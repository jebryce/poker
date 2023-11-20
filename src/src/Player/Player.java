package Player;

import Gate.*;
import Gate.BaseGates.*;
import Gate.IOGates.ChipIO.ChipIO;
import Gate.IOGates.ChipIO.IO_Direction;
import Gate.IOGates.Input;
import Gate.IOGates.Output;
import Main.Constants;
import Main.KeyHandler.KeyBinds;
import Main.KeyHandler.KeyHandler;
import Main.MouseHandler;
import Main.SaveHandler;
import Wire.Node.Node;
import Wire.Wire;
import Wire.Wires.Wires;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class Player {
    private final MouseHandler  mouseHandler;
    private       Gate          heldGate       = null;
    private       Wire          heldWire       = null;
    private       Node          heldWireNode   = null;
    private final Point2D       cursorLocation = new Point2D.Double(); // cursorLocation = location of cursor in window
    private final Point2D       playerLocation = new Point2D.Double(); // playerLocation = location of cursor in playableArea
    private       Gates         gates          = new Gates();
    private       PlayerMode    playerMode     = PlayerMode.NORMAL;

    public Player( final MouseHandler mouseH ) {
        mouseHandler = mouseH;
    }

    public void repaint( final Graphics2D graphics2D  ) {
        gates.repaint( graphics2D );

        if ( heldGate != null ) {
            heldGate.repaintInHand( graphics2D );
        }
    }

    public void update() {
        gates.update();

        cursorLocation.setLocation( mouseHandler.xPos, mouseHandler.yPos );
        playerLocation.setLocation(
                mouseHandler.xPos,
                Math.min( Math.max( mouseHandler.yPos, Constants.NAME_SPACE_HEIGHT ), Constants.END_PLAYABLE_AREA )
        );


        checkKeyPresses();

        switch ( playerMode ) {
            case NORMAL        -> updateNORMAL();
            case PLACE_GATE    -> updatePLACE_GATE();
            case PLACE_WIRE    -> updatePLACE_WIRE();
            case SAVE_MENU     -> updateSAVE_MENU();
            case LOAD_MENU     -> updateLOAD_MENU();
        }
    }

    private void checkKeyPresses() {
        if ( KeyHandler.get().isKeyPressed( KeyBinds.clearHand ) ) {
            clearHand();
        }
        if ( KeyHandler.get().isKeyPressed( KeyBinds.clearHandALT ) ) {
            clearHand();
        }

        if ( playerMode == PlayerMode.SAVE_MENU ) {
            return;
        }

        if ( KeyHandler.get().isKeyPressed( KeyBinds.delete ) ) {
            gates.deleteAtLocation( playerLocation );
        }

        if ( KeyHandler.get().isKeyPressed( KeyBinds.placeWire ) ) {
            clearHand();
            playerMode = PlayerMode.PLACE_WIRE;
        }

        boolean makeGateNot = KeyHandler.get().isKeyHeld( KeyBinds.makeGateNOT );
        if ( KeyHandler.get().isKeyPressed( KeyBinds.newIOGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new Output(   playerLocation ); }
            else               { heldGate = new Input(    playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( KeyHandler.get().isKeyPressed( KeyBinds.newNOTGate ) ) {
            clearHand();
                                 heldGate = new gateNOT(  playerLocation );
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( KeyHandler.get().isKeyPressed( KeyBinds.newANDGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateNAND( playerLocation ); }
            else               { heldGate = new gateAND(  playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( KeyHandler.get().isKeyPressed( KeyBinds.newORGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateNOR(  playerLocation ); }
            else               { heldGate = new gateOR(   playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( KeyHandler.get().isKeyPressed( KeyBinds.newXORGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateXNOR( playerLocation ); }
            else               { heldGate = new gateXOR(  playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( KeyHandler.get().isKeyPressed( KeyBinds.newChip ) ) {
            clearHand();
                                 heldGate = new Chip(     playerLocation );
            playerMode = PlayerMode.PLACE_GATE;
        }
    }

    private void clearHand() {
        SaveHandler.get().deselect();
        heldGate = null;
        heldWire = null;
        if ( heldWireNode != null ) {
            heldWireNode.clearPlayerNode();
        }
        heldWireNode = null;
        playerMode = PlayerMode.NORMAL;
    }

    private void updateNORMAL() {
        if ( mouseHandler.isMouseLeftClicked() ) {
            if ( setMenuState() ) {
                return;
            }
            Gate gate = gates.findContainingGate( playerLocation );
            if ( gate instanceof Input ) {
                ((Input) gate).flipState();
            }
            if ( gate instanceof Chip ) {
                gates = ((Chip) gate).getContents().setPrevious( gates );
            }
        }
        if ( mouseHandler.isMouseRightClicked() ) {
            gates = gates.getPrevious();
        }
    }

    private void updatePLACE_GATE() {
        if ( heldGate != null ) {
            Point2D centerOffset = heldGate.getCenterOffset();
            Point2D snappedLocation = gates.snap( playerLocation );
            double y = Math.max( snappedLocation.getY() - centerOffset.getY(), Constants.NAME_SPACE_HEIGHT );
            y = Math.min( y,
                    Constants.SCREEN_HEIGHT - Constants.CHIP_SPACE_HEIGHT - heldGate.getHeight()
            );
            heldGate.setLocation( snappedLocation.getX() - centerOffset.getX(), y );
            if ( mouseHandler.isMouseLeftClicked() ) {
                if ( setMenuState() ) {
                    return;
                }
                heldGate.place();
                gates.add( heldGate );
                heldGate = null;
                playerMode = PlayerMode.NORMAL;
            }
        }
    }

    private void updatePLACE_WIRE() {
        if ( heldWireNode != null ) {
            Point2D snappedLocation = gates.snapToNode( playerLocation );
            heldWireNode.updatePlayerNode( snappedLocation );
        }
        if ( !mouseHandler.isMouseLeftClicked() ) {
            return;
        }
        if ( setMenuState() ) {
            return;
        }

        Wires containingWires = gates.findContainingWires( playerLocation );
        Node containingNode;

        for ( Wire wire : containingWires ) {
            if ( wire == heldWire ) {
                continue;
            }
            containingNode = wire.findContainingNode( playerLocation );
            if ( heldWireNode == null ) {
                if ( containingNode == null ) {
                    continue;
                }
                heldWireNode = containingNode;
                heldWireNode.setPlayerNode( playerLocation );
                heldWire = wire;
                return;
            }
            if ( containingNode != null ) {
                gates.connectWires( heldWire, wire, heldWireNode, containingNode );
                clearHand();
                return;
            }
        }
        if ( heldWireNode != null ) {
            heldWireNode = heldWire.placePlayerNode( heldWireNode );
            heldWireNode.setPlayerNode( playerLocation );
        }
    }

    private boolean setMenuState() {
        if ( playerLocation.getY() < Constants.NAME_SPACE_HEIGHT) {
            clearHand();
            playerMode = PlayerMode.SAVE_MENU;
            SaveHandler.get().select();
            KeyHandler.get().resetKeysTyped();
            SaveHandler.get().setContents( gates );
            return true;
        }
        else if ( playerLocation.getY() > Constants.SCREEN_HEIGHT - Constants.CHIP_SPACE_HEIGHT ) {
            clearHand();
            playerMode = PlayerMode.LOAD_MENU;
            return true;
        }
        return false;
    }

    private void updateSAVE_MENU() {
        if ( mouseHandler.isMouseLeftClicked() ) {
            if ( playerLocation.getY() > Constants.SCREEN_HEIGHT - Constants.CHIP_SPACE_HEIGHT ) {
                clearHand();
                playerMode = PlayerMode.LOAD_MENU;
            } else if ( playerLocation.getY() > Constants.NAME_SPACE_HEIGHT ) {
                clearHand();
            }
        }
    }

    private void updateLOAD_MENU() {
        if ( mouseHandler.isMouseLeftClicked() ) {
            if ( playerLocation.getY() < Constants.NAME_SPACE_HEIGHT) {
                clearHand();
                playerMode = PlayerMode.SAVE_MENU;
                SaveHandler.get().select();
                KeyHandler.get().resetKeysTyped();
                SaveHandler.get().setContents( gates );
            } else if ( playerLocation.getY() < Constants.SCREEN_HEIGHT - Constants.CHIP_SPACE_HEIGHT ) {
                clearHand();
            }
        }
    }
}
