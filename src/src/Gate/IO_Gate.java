package Gate;

import Main.Colors;
import Wire.Node.NodeType;
import Wire.Wire;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class IO_Gate extends Gate {
    public IO_Gate( final Point2D location ) {
        super( location );
    }

    @Override
    protected Wire addWire( final NodeType nodeType, final int x, final int y ) {
        return super.addWire( nodeType, x, y );
    }

    @Override
    public void repaint( Graphics2D graphics2D ) {
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.fill( body );
        graphics2D.translate( -location.getX(), -location.getY() );
        super.repaint(graphics2D);
    }

    @Override
    public void repaintInHand( Graphics2D graphics2D ) {
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.fill( body );
        graphics2D.translate( -location.getX(), -location.getY() );
        super.repaintInHand(graphics2D);
    }

}
