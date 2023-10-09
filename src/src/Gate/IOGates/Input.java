package Gate.IOGates;


import Gate.GateType;
import Gate.IO_Gate;
import Wire.Node.NodeType;
import Main.Colors;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Input extends IO_Gate {
    public Input( final Point2D location ) {
        super( location, GateType.INPUT );

        addWire( NodeType.OUTPUT, 50, 25 );
    }

    public void flipState() {
        state = outputWires.getFirst().flipState();
    }
}
