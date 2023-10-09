package Gate;

import Main.Colors;
import Wire.Node.NodeType;
import Wire.Wire;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public abstract class IO_Gate extends Gate {
    protected boolean state = false;

    public IO_Gate( final Point2D location, GateType gateType ) {
        super( location, gateType );
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );
    }

    public IO_Gate( final double x, final double y, final GateType gateType ) {
        super( x, y, gateType );
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );
    }

    @Override
    protected Wire addWire( final NodeType nodeType, final double x, final double y ) {
        return super.addWire( nodeType, x, y );
    };

    private void repaintBody( Graphics2D graphics2D ) {
        if ( state ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.fill( body );
        graphics2D.translate( -location.getX(), -location.getY() );
    }

    @Override
    public void repaint( Graphics2D graphics2D ) {
        repaintBody( graphics2D );
        super.repaint(graphics2D);
    }

    @Override
    public void repaintInHand( Graphics2D graphics2D ) {
        repaintBody( graphics2D );
        super.repaintInHand(graphics2D);
    }

}
