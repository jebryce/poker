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

    private Wire findContainingWire( final Point2D location ) {
        for ( Wire wire : wires ) {
            if ( wire.isPointWithinBounds( location ) ) {
                return wire;
            }
        }
        return null;
    }

    public Node findClosestNode( final Point2D location ) {
        Wire containingWire = findContainingWire( location );
        if ( containingWire == null ) {
            return null;
        }
        for ( Node node : containingWire ) {
            if ( node.isPointWithinBounds( location ) ) {
                return node;
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
