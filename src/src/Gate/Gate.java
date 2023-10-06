package Gate;

import Main.Colors;
import Main.Constants;
import Wire.Node.Node;
import Wire.Node.NodeType;
import Wire.Wire;
import Wire.Wires;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Container.ListItem;

public abstract class Gate extends ListItem {
    protected final Path2D  body         = new Path2D.Double();
    protected final Point2D location     = new Point2D.Double( 0, 0 );
    protected final Wires   inputWires   = new Wires( 4 );
    protected final Wires   outputWires  = new Wires( 4 );

    public Gate( final Point2D location ) {
        this.location.setLocation( location );
    }

    public Gate place() {
        for ( Wire wire : inputWires ) {
            wire.move( location );
        }
        for ( Wire wire : outputWires ) {
            wire.move( location );
        }
        return this;
    }

    public Wire connect( final Gate gateToConnect ) {
        Wire input = gateToConnect.inputWires.getFirst();
        Wire output = outputWires.getFirst();

        gateToConnect.replaceWire( input, output );
        output.replaceWire( input, output.getHead(), input.getHead());
        return input;
    }

    public void setLocation( final double x, final double y ) {
        location.setLocation( x, y );
    }

    public Point2D getCenterOffset() {
        return new Point2D.Double( body.getBounds().getCenterX() , body.getBounds().getCenterY() );
    }

    public Point2D getLocation() {
        return location;
    }

    public boolean isPointWithin( final Point2D point2D ) {
        return body.contains( point2D.getX() - location.getX(), point2D.getY() - location.getY() );
    }

    public boolean isPointNear( final Point2D point2D ) {
        double x = point2D.getX() - location.getX();
        double y = point2D.getY() - location.getY();
        Rectangle2D bounds = body.getBounds2D();
        final double maxX = bounds.getMaxX() + Constants.MIN_LINE_LENGTH;
        final double minX = bounds.getMinX() - Constants.MIN_LINE_LENGTH;
        final double maxY = bounds.getMaxY() + Constants.MIN_LINE_LENGTH;
        final double minY = bounds.getMinY() - Constants.MIN_LINE_LENGTH;

        if ( x > maxX ) {
            return false;
        }
        if ( x < minX ) {
            return false;
        }
        if ( y > maxY ) {
            return false;
        }
        if ( y < minY ) {
            return false;
        }
        return true;
    }

    public Point2D getCenter() {
        Point2D centerOffset = getCenterOffset();
        return new Point2D.Double(
                location.getX() + centerOffset.getX(), location.getY() + centerOffset.getY()
        );
    }

    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.setColor( Colors.BLACK );
        graphics2D.draw(body);
        graphics2D.translate( -location.getX(), -location.getY() );
    }

    public void repaintInHand( final Graphics2D graphics2D ) {
        graphics2D.translate( location.getX(), location.getY() );
        inputWires.repaint( graphics2D );
        outputWires.repaint( graphics2D );
        graphics2D.setColor( Colors.BLACK );
        graphics2D.draw(body);
        graphics2D.translate( -location.getX(), -location.getY() );
    }

    public void update() {};

    protected Wire addWire( final NodeType nodeType, final int x, final int y ) {
        assert nodeType == NodeType.INPUT || nodeType == NodeType.OUTPUT :
                "Cannot add a wire to a gate that isn't an input or output";
        if ( nodeType == NodeType.INPUT ) {
            return inputWires.add(  new Wire( nodeType, x, y ) );
        }
        else {
            return outputWires.add( new Wire( nodeType, x, y ) );
        }
    }

    public Wires getInputWires() {
        return inputWires;
    }

    public Wires getOutputWires() {
        return outputWires;
    }

    public void replaceWire( Wire oldWire, Wire newWire ) {
        if ( inputWires.remove( oldWire ) ) {
            inputWires.add( newWire );
            return;
        }
        if ( outputWires.remove( oldWire ) ) {
            outputWires.add( newWire );
            return;
        }
        assert false : oldWire + " is not attached to this gate.";
    }

    public boolean contains( final Wire wire ) {
        return inputWires.contains( wire ) || outputWires.contains( wire );
    }
}
