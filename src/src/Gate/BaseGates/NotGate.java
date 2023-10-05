package Gate.BaseGates;


import Gate.IO_Gate;
import Wire.Node.NodeType;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class NotGate extends IO_Gate {
    public NotGate( final Point2D location ) {
        super( location );
        body.append( new Line2D.Float( 0, 0, 50, 30 ), false );
        body.append( new Line2D.Float( 50, 30, 0, 60 ), false );
        body.append( new Line2D.Float( 0, 60, 0, 0 ), false );

        addWire( NodeType.INPUT, 0, 30 );
        addWire( NodeType.OUTPUT, 50, 30 );

    }

    @Override
    public void update() {
        outputWires.getFirst().setState( !inputWires.getFirst().getState() );
    }
}
