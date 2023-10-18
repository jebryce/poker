package Player;

import Gate.*;
import Gate.BaseGates.*;
import Gate.IOGates.Input;
import Gate.IOGates.Output;
import Main.Constants;
import Main.KeyHandler.KeyBinds;
import Main.KeyHandler.KeyHandler;
import Main.LoadHandler;
import Main.MouseHandler;
import Main.SaveHandler;
import Wire.Node.Node;
import Wire.Wire;
import Wire.Wires.Wires;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Player {
    private final MouseHandler  mouseHandler;
    private       Gate          heldGate       = null;
    private       Wire          heldWire       = null;
    private       Node          heldWireNode   = null;
    private final Point2D       playerLocation = new Point2D.Double();
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

        if ( playerMode == PlayerMode.SAVE_MENU ) {
            if ( SaveHandler.get().repaint( graphics2D ) ) {
                clearHand();
            }
        } else if ( playerMode == PlayerMode.LOAD_MENU ) {

            clearHand();
//            LoadHandler.get().repaint( graphics2D );
        }
    }

    public void update() {
        gates.update();

        playerLocation.setLocation( mouseHandler.xPos, mouseHandler.yPos );

        checkKeyPresses();

        switch ( playerMode ) {
            case NORMAL        -> updateNORMAL();
            case PLACE_GATE    -> updatePLACE_GATE();
            case PLACE_WIRE    -> updatePLACE_WIRE();
        }
    }

    private void checkKeyPresses() {
        if ( KeyHandler.get().isKeyPressed( KeyBinds.clearHand ) ) {
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

        if ( KeyHandler.get().isKeyPressed( KeyBinds.save ) ) {
            SaveHandler.get().setContents( gates );
            KeyHandler.get().resetKeysTyped();
            SaveHandler.get().resetChipName();
            clearHand();
            playerMode = PlayerMode.SAVE_MENU;
        }

        if ( KeyHandler.get().isKeyPressed( KeyBinds.load ) ) {
            clearHand();
            playerMode = PlayerMode.LOAD_MENU;
        }
    }

    private void clearHand() {
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
            heldGate.setLocation( snappedLocation.getX() - centerOffset.getX(), snappedLocation.getY() - centerOffset.getY() );
            if ( mouseHandler.isMouseLeftClicked() ) {
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
}
