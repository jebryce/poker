package Gate;

import Main.Colors;
import Wire.Node.NodeType;
import Wire.Wire;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class IO_Gate extends Gate {
    private Wire wire = null;

    public IO_Gate( final Point2D location ) {
        super( location );
    }

    @Override
    protected void addWire( final NodeType nodeType, final int x, final int y ) {
        super.addWire( nodeType, x, y );
        if ( nodeType == NodeType.INPUT ) {
            wire = inputWires.getFirst();
        }
        if ( nodeType == NodeType.OUTPUT ) {
            wire = outputWires.getFirst();
        }
    }

    @Override
    public void repaint( Graphics2D graphics2D ) {
        graphics2D.translate( location.getX(), location.getY() );
        if ( wire != null && wire.getState() ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }
        graphics2D.fill( body );
        graphics2D.translate( -location.getX(), -location.getY() );
        super.repaint(graphics2D);
    }
}
