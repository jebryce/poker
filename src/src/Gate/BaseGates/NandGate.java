package Gate.BaseGates;


import Gate.Gate;
import Wire.Node.NodeType;
import Wire.Wire;

import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class NandGate extends Gate {

    public NandGate( final Point2D location ) {
        super( location );
        body.append( new Line2D.Float( 0, 0, 75, 0 ), false );
        body.append( new Arc2D.Float( 25, 0, 100, 100, 270, 180, Arc2D.OPEN ), false );
        body.append( new Line2D.Float( 0, 100, 75, 100 ), false );
        body.append( new Line2D.Float( 0, 100, 0, 0 ), false );

        body.append( new Ellipse2D.Float( 125, 40, 20, 20 ), false );

        addWire( NodeType.INPUT, 0, 30 );
        addWire( NodeType.INPUT, 0, 70 );

        addWire( NodeType.OUTPUT, 145, 50 ).setState( true );
    }

    @Override
    public void update() {
        outputWires.getFirst().setState( !inputWires.and() );
    }
}
