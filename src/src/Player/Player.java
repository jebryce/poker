package Player;

import Gate.*;
import Gate.BaseGates.*;
import Gate.IOGates.Input;
import Gate.IOGates.Output;
import Main.KeyBinds;
import Main.KeyHandler;
import Main.MouseHandler;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player {
    private final MouseHandler  mouseHandler;
    private final KeyHandler    keyHandler;
    private       Gate          heldGate;
    private final Point2D       playerLocation = new Point2D.Double();

    public Player( final MouseHandler mouseH, final KeyHandler keyH ) {
        mouseHandler = mouseH;
        keyHandler = keyH;

//        Gate in1  = placedObjects.placeGate( new Input( 100, 100 ) );
//        Gate in2  = placedObjects.placeGate( new Input( 100, 500 ) );
//        Gate and  = placedObjects.placeGate( new AndGate( 400, 400 ) );
//        Gate out1 = placedObjects.placeGate( new Output( 800, 600 ) );
//        Gate out2 = placedObjects.placeGate( new Output( 850, 700 ) );
//        Gate out3 = placedObjects.placeGate( new Output( 800, 200 ) );
//        Gate out4 = placedObjects.placeGate( new Output( 850, 500 ) );
//        placedObjects.attachWireToNode( in1.getWires()[0], and.getNodes()[0] );
//        placedObjects.attachWireToNode( in2.getWires()[0], and.getNodes()[1] );
//        placedObjects.attachWireToNode( and.getWires()[2], out1.getNodes()[0] );
//        placedObjects.attachWireToNode( and.getWires()[2], out2.getNodes()[0] );
//        placedObjects.attachWireToNode( and.getWires()[2], out3.getNodes()[0] );
//        placedObjects.attachWireToNode( and.getWires()[2], out4.getNodes()[0] );
    }

    public void repaint( final Graphics2D graphics2D  ) {
        if ( heldGate != null ) {
            Point2D centerOffset = heldGate.getCenterOffset();
            heldGate.setLocation( playerLocation.getX() - centerOffset.getX(), playerLocation.getY() - centerOffset.getY() );
            heldGate.repaint( graphics2D );
        }
    }

    public void update() {

        playerLocation.setLocation( mouseHandler.xPos, mouseHandler.yPos );

        checkKeyPresses();

    }

    private void checkKeyPresses() {
        boolean makeGateNot = keyHandler.isKeyHeld( KeyBinds.makeGateNOT );
        if ( keyHandler.isKeyPressed( KeyBinds.newIOGate ) ) {
            if ( makeGateNot ) { heldGate = new Output( playerLocation ); }
            else               { heldGate = new Input( playerLocation ); }
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newNOTGate ) ) {
                                 heldGate = new NotGate( playerLocation );
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newANDGate ) ) {
            if ( makeGateNot ) { heldGate = new NandGate( playerLocation ); }
            else               { heldGate = new AndGate( playerLocation ); }
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newORGate ) ) {
            if ( makeGateNot ) { heldGate = new NorGate( playerLocation ); }
            else               { heldGate = new OrGate( playerLocation ); }
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newXORGate ) ) {
            if ( makeGateNot ) { heldGate = new XNorGate( playerLocation ); }
            else               { heldGate = new XorGate( playerLocation ); }
        }

        if ( keyHandler.isKeyPressed( KeyBinds.clearHand ) ) {
            heldGate = null;
        }
    }

}
