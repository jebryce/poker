package Gate;

import java.awt.*;
import java.awt.geom.Point2D;

import Container.LinkedList;
import Wire.Node.Node;
import Wire.WiresLL;
import Wire.Wires;
import Wire.Wire;

public class Gates extends LinkedList<Gate> {
    private final WiresLL wires = new WiresLL();

    @Override
    public Gate add( final Gate newGate ) {
        super.add( newGate );
        wires.addFromGate( newGate );
        return newGate;
    }

    public Wires findContainingWires( final Point2D location ) {
        Wires returnWires = new Wires( wires.getLength() );
        for ( Wire wire : wires ) {
            if ( wire.isPointWithinBounds( location ) ) {
                returnWires.add( wire );
            }
        }
        return returnWires;
    }

    public void repaint( final Graphics2D graphics2D ) {
        for ( Wire wire : wires ) {
            wire.repaint( graphics2D );
        }
        for ( Gate gate : this ) {
            gate.repaint( graphics2D );
        }
    }

    public void update() {
        for ( Gate gate : this ) {
            gate.update();
        }
    }

    public Gate findContainingGate( final Point2D point ) {
        for ( Gate gate : this ) {
            if ( gate.isPointWithin( point ) ) {
                return gate;
            }
        }
        return null;
    }

    public void connectGates( final Gate gate0, final Gate gate1 ) {
        wires.remove( gate0.connect( gate1 ) );
    }

    public void connectWires( final Wire wire0, final Wire wire1, final Node wire0Node, final Node wire1Node ) {
        wires.remove( wire1 );
        wire0.replaceWire( wire1, wire0Node, wire1Node );
    }
}
