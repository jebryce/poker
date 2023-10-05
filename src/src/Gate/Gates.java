package Gate;

import java.awt.*;
import java.awt.geom.Point2D;

import Container.LinkedList;
import Wire.Node.Node;
import Wire.WiresLL;
import Wire.Wire;

public class Gates extends LinkedList<Gate> {
    private final WiresLL wires = new WiresLL();

    @Override
    public Gate add( final Gate newGate ) {
        super.add( newGate );
        wires.addFromGate( newGate );
        return newGate;
    }

    public Node findNearestNode( final Point2D location ) {
        for ( Wire wire : wires ) {
            System.out.println( wire.isPointWithinBounds( location ) );
            if ( wire.isPointWithinBounds( location ) ) {
                return null;
            }
        }
        return null;
    }

    public void repaint( final Graphics2D graphics2D ) {
        for ( Gate gate : this ) {
            gate.repaint( graphics2D );
        }
    }

    public void update() {
        for ( Gate gate : this ) {
            gate.update();
        }
    }
}
