package Gate;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Main.Colors;
import Wire.Node.NodeType;

public class Chip extends Gate {
    private final Arc2D notch      = new Arc2D.Double( 40, -20, 40, 40, 180, 180, Arc2D.OPEN );
    private final Gates contents   = new Gates();

    public Chip( final Point2D location ) {
        super( location, GateType.CHIP );
        body.append( new Rectangle2D.Double(0, 0, 120, 180), false );

        addWire( NodeType.INPUT, 0, 30 );
        addWire( NodeType.INPUT, 0, 70 );
        addWire( NodeType.INPUT, 0, 110 );
        addWire( NodeType.INPUT, 0, 150 );

        addWire( NodeType.OUTPUT, 120, 30 );
        addWire( NodeType.OUTPUT, 120, 70 );
        addWire( NodeType.OUTPUT, 120, 110 );
        addWire( NodeType.OUTPUT, 120, 150 );
    }

    private void repaintBody( final Graphics2D graphics2D ) {
        graphics2D.translate(location.getX(), location.getY());
        graphics2D.setColor(Colors.GRAY);
        graphics2D.fill(body);
        graphics2D.setColor(Colors.BLACK);
        graphics2D.fill(notch);
        graphics2D.translate(-location.getX(), -location.getY());
    }

    @Override
    public void repaint( final Graphics2D graphics2D ) {
        repaintBody( graphics2D );
        super.repaint( graphics2D );
    }

    @Override
    public void repaintInHand( final Graphics2D graphics2D ) {
        repaintBody( graphics2D );
        super.repaintInHand( graphics2D );
    }

    public Gates getContents() {
        return contents;
    }
}
