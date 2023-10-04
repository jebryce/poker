package Gate.Chip;

import Gate.Gate;
import Gate.IOGates.Input;
import Gate.IOGates.Output;
import Main.Colors;
import Main.Constants;
import Node.NodeType;
import Player.PlacedObjects;
import Wire.Wire;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

public class Chip extends Gate {
    private final Arc2D         notch      = new Arc2D.Double( 40, -20, 40, 40, 180, 180, Arc2D.OPEN );
    private final PlacedObjects contents   = new PlacedObjects();
    private final Gate[]        inputs     = new Gate[Constants.MAX_NUM_IO/2];
    private final Gate[]        outputs    = new Gate[Constants.MAX_NUM_IO/2];


    public Chip( int x, int y ) {
        super(x, y);
        body.append( new Rectangle2D.Double(0, 0, 120, 180), false );

        addNode( NodeType.INPUT, 0, 30 );
        addNode( NodeType.INPUT, 0, 70 );
        addNode( NodeType.INPUT, 0, 110 );
        addNode( NodeType.INPUT, 0, 150 );

        addNode( NodeType.OUTPUT, 120, 30 );
        addNode( NodeType.OUTPUT, 120, 70 );
        addNode( NodeType.OUTPUT, 120, 110 );
        addNode( NodeType.OUTPUT, 120, 150 );
    }

    private void connectIO( final Gate[] IOs, final Gate IOGate ) {
        for ( int index = 0; index < Constants.MAX_NUM_IO/2; index++ ) {
            if ( IOs[index] != null ) {
                continue;
            }
            IOs[index] = IOGate;
        }
    }

    @Override
    protected void addNode( final NodeType nodeType, final int x, final int y ) {
        super.addNode( nodeType, x, y );
        if ( nodeType == NodeType.INPUT ) {
            Input newInput = new Input( 50, y * 10 );
            connectIO( inputs, newInput );
            contents.placeGate( newInput );
        }
        else if ( nodeType == NodeType.OUTPUT ) {
            Output newOutput = new Output( 1775, (y * 10) + 12 );
            connectIO( outputs, newOutput );
            contents.placeGate( newOutput );
        }
    }

    @Override
    public void update() {
        int index = 0;
        for ( Gate input : inputs ) {
            input.getWires()[0].setState( wires[index++].getState() );
        }
        index = 4;
        for ( Gate output : outputs ) {
            wires[index++].setState( output.getWires()[0].getState() );
        }
        contents.update();
    }

    @Override
    public PlacedObjects select() {
        return contents;
    }

    @Override
    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.setColor( Colors.GRAY );
        graphics2D.fill( body );
        graphics2D.setColor( Colors.BLACK );
        graphics2D.fill( notch );
        graphics2D.translate( -location.getX(), -location.getY() );
        super.repaint( graphics2D );
    }
}
