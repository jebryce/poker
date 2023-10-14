package Player;

import Gate.*;
import Gate.BaseGates.*;
import Gate.IOGates.Input;
import Gate.IOGates.Output;
import GsonAdapters.GsonGatesAdapter;
import Main.KeyBinds;
import Main.KeyHandler;
import Main.MouseHandler;
import Wire.Node.Node;
import Wire.Wire;
import Wire.Wires.Wires;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player {
    private final MouseHandler  mouseHandler;
    private final KeyHandler    keyHandler;
    private       Gate          heldGate = null;
    private       Wire          heldWire = null;
    private       Node          heldWireNode = null;
    private final Point2D       playerLocation = new Point2D.Double();
    private       Gates         gates          = new Gates();
    private       PlayerMode    playerMode     = PlayerMode.NORMAL;
    private final Gson          gson;

    public Player( final MouseHandler mouseH, final KeyHandler keyH ) {
        mouseHandler = mouseH;
        keyHandler = keyH;

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter( Gates.class, new GsonGatesAdapter() );
        gson = builder.create();
    }

    public void repaint( final Graphics2D graphics2D  ) {
        gates.repaint( graphics2D );

        if ( heldGate != null ) {
            heldGate.repaintInHand( graphics2D );
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
        if ( keyHandler.isKeyPressed( KeyBinds.delete ) ) {
            gates.deleteAtLocation( playerLocation );
        }

        if ( keyHandler.isKeyPressed( KeyBinds.placeWire ) ) {
            clearHand();
            playerMode = PlayerMode.PLACE_WIRE;
        }

        boolean makeGateNot = keyHandler.isKeyHeld( KeyBinds.makeGateNOT );
        if ( keyHandler.isKeyPressed( KeyBinds.newIOGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new Output(   playerLocation ); }
            else               { heldGate = new Input(    playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newNOTGate ) ) {
            clearHand();
                                 heldGate = new gateNOT(  playerLocation );
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newANDGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateNAND( playerLocation ); }
            else               { heldGate = new gateAND(  playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newORGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateNOR(  playerLocation ); }
            else               { heldGate = new gateOR(   playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newXORGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateXNOR( playerLocation ); }
            else               { heldGate = new gateXOR(  playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newChip ) ) {
            clearHand();
                                 heldGate = new Chip(     playerLocation );
            playerMode = PlayerMode.PLACE_GATE;
        }

        if ( keyHandler.isKeyPressed( KeyBinds.clearHand ) ) {
            clearHand();
        }

        if ( keyHandler.isKeyPressed( KeyBinds.save ) ) {
            String jsonString = gson.toJson( gates );
            System.out.println( jsonString );
        }

        if ( keyHandler.isKeyPressed( KeyBinds.load ) ) {
            String jsonString = "[[{\"gateType\":9,\"point\":[1315.0,1027.0]}],[{\"gateType\":9,\"point\":[1315.0,755.0]}],[{\"gateType\":8,\"point\":[335.0,815.0]},[{\"nodeType\":0,\"index\":0,\"point\":[790.0,720.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[750.0,720.0],\"connectedNodeIndexes\":[0,2]},{\"nodeType\":2,\"index\":2,\"point\":[425.0,840.0],\"connectedNodeIndexes\":[1,3]},{\"nodeType\":1,\"index\":3,\"point\":[385.0,840.0],\"connectedNodeIndexes\":[2]}]],[{\"gateType\":8,\"point\":[370.0,572.0]},[{\"nodeType\":0,\"index\":0,\"point\":[790.0,840.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[750.0,840.0],\"connectedNodeIndexes\":[0,2]},{\"nodeType\":2,\"index\":2,\"point\":[460.0,597.0],\"connectedNodeIndexes\":[1,3]},{\"nodeType\":1,\"index\":3,\"point\":[420.0,597.0],\"connectedNodeIndexes\":[2]}]],[{\"gateType\":10,\"point\":[790.0,690.0],\"chipContents\":[[{\"gateType\":7,\"point\":[1212.0,673.125]},[{\"nodeType\":0,\"index\":0,\"point\":[1775.0,703.125],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[1735.0,703.125],\"connectedNodeIndexes\":[0,2]},{\"nodeType\":2,\"index\":2,\"point\":[1302.0,703.125],\"connectedNodeIndexes\":[1,3]},{\"nodeType\":1,\"index\":3,\"point\":[1262.0,703.125],\"connectedNodeIndexes\":[2]}]],[{\"gateType\":3,\"point\":[604.5,907.0]},[{\"nodeType\":0,\"index\":0,\"point\":[1775.0,1171.875],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[1735.0,1171.875],\"connectedNodeIndexes\":[0,2]},{\"nodeType\":2,\"index\":2,\"point\":[1735.0,957.0],\"connectedNodeIndexes\":[1,5]},{\"nodeType\":0,\"index\":3,\"point\":[1212.0,703.125],\"connectedNodeIndexes\":[4]},{\"nodeType\":2,\"index\":4,\"point\":[1172.0,703.125],\"connectedNodeIndexes\":[3,5]},{\"nodeType\":2,\"index\":5,\"point\":[1140.0,957.0],\"connectedNodeIndexes\":[4,2,6]},{\"nodeType\":2,\"index\":6,\"point\":[769.5,957.0],\"connectedNodeIndexes\":[5,7]},{\"nodeType\":1,\"index\":7,\"point\":[729.5,957.0],\"connectedNodeIndexes\":[6]}]],[{\"gateType\":11,\"point\":[1775.0,1615.625],\"ioDirection\":0}],[{\"gateType\":11,\"point\":[1775.0,1146.875],\"ioDirection\":0}],[{\"gateType\":11,\"point\":[1775.0,678.125],\"ioDirection\":0}],[{\"gateType\":11,\"point\":[1775.0,209.375],\"ioDirection\":0}],[{\"gateType\":11,\"point\":[50.0,1615.625],\"ioDirection\":1},[{\"nodeType\":0,\"index\":0,\"point\":[649.5,977.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[609.5,977.0],\"connectedNodeIndexes\":[0,2]},{\"nodeType\":2,\"index\":2,\"point\":[140.0,1640.625],\"connectedNodeIndexes\":[1,3]},{\"nodeType\":1,\"index\":3,\"point\":[100.0,1640.625],\"connectedNodeIndexes\":[2]}]],[{\"gateType\":11,\"point\":[50.0,1146.875],\"ioDirection\":1},[{\"nodeType\":2,\"index\":0,\"point\":[140.0,1171.875],\"connectedNodeIndexes\":[1]},{\"nodeType\":1,\"index\":1,\"point\":[100.0,1171.875],\"connectedNodeIndexes\":[0]}]],[{\"gateType\":11,\"point\":[50.0,678.125],\"ioDirection\":1},[{\"nodeType\":2,\"index\":0,\"point\":[140.0,703.125],\"connectedNodeIndexes\":[1]},{\"nodeType\":1,\"index\":1,\"point\":[100.0,703.125],\"connectedNodeIndexes\":[0]}]],[{\"gateType\":11,\"point\":[50.0,209.375],\"ioDirection\":1},[{\"nodeType\":1,\"index\":0,\"point\":[100.0,234.375],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[140.0,234.375],\"connectedNodeIndexes\":[2,0]},{\"nodeType\":2,\"index\":2,\"point\":[609.5,937.0],\"connectedNodeIndexes\":[3,1]},{\"nodeType\":0,\"index\":3,\"point\":[649.5,937.0],\"connectedNodeIndexes\":[2]}]]]},[{\"nodeType\":2,\"index\":0,\"point\":[950.0,720.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":1,\"index\":1,\"point\":[910.0,720.0],\"connectedNodeIndexes\":[0]}],[{\"nodeType\":0,\"index\":0,\"point\":[1315.0,780.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[1275.0,780.0],\"connectedNodeIndexes\":[0,2]},{\"nodeType\":2,\"index\":2,\"point\":[950.0,760.0],\"connectedNodeIndexes\":[1,3]},{\"nodeType\":1,\"index\":3,\"point\":[910.0,760.0],\"connectedNodeIndexes\":[2]}],[{\"nodeType\":1,\"index\":0,\"point\":[910.0,800.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[950.0,800.0],\"connectedNodeIndexes\":[2,0]},{\"nodeType\":2,\"index\":2,\"point\":[1275.0,1052.0],\"connectedNodeIndexes\":[3,1]},{\"nodeType\":0,\"index\":3,\"point\":[1315.0,1052.0],\"connectedNodeIndexes\":[2]}],[{\"nodeType\":2,\"index\":0,\"point\":[950.0,840.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":1,\"index\":1,\"point\":[910.0,840.0],\"connectedNodeIndexes\":[0]}]]]";
            gates = gson.fromJson( jsonString, Gates.class );
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
