package Gate.IOGates;


import Gate.GateType;
import Gate.IO_Gate;
import Main.Colors;
import Wire.Node.NodeType;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Output extends IO_Gate {
    public Output( final Point2D location ) {
        super( location, GateType.OUTPUT );

        addWire( NodeType.INPUT, 0, 25 );
    }

    @Override
    public void update() {
        state = inputWires.getFirst().getState();
    }
}
