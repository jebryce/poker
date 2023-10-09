package Gate;

import Main.Colors;
import Wire.Node.NodeType;
import Wire.Wire;
import Wire.Wires;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Container.ListItem;

public abstract class Gate extends ListItem {
    protected final Path2D   body         = new Path2D.Double();
    protected final Point2D  location     = new Point2D.Double( 0, 0 );
    protected final Wires    inputWires   = new Wires( 4 );
    protected final Wires    outputWires  = new Wires( 4 );
    protected final GateType gateType;

    public Gate( final Point2D location, final GateType gateType ) {
        this.location.setLocation( location );
        this.gateType = gateType;
    }

    public Gate( final double x, final double y, final GateType gateType ) {
        this.location.setLocation( x, y );
        this.gateType = gateType;
    }

    public Gate place() {
        moveInputs();
        for ( Wire wire : outputWires ) {
            wire.move( location );
        }
        return this;
    }

    public void moveInputs() {
        for ( Wire wire : inputWires ) {
            wire.move( location );
        }
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

    public boolean isPointNear( final Point2D point2D, final int radius ) {
        double x = point2D.getX() - location.getX();
        double y = point2D.getY() - location.getY();
        Rectangle2D bounds = body.getBounds2D();
        final double maxX = bounds.getMaxX() + radius;
        final double minX = bounds.getMinX() - radius;
        final double maxY = bounds.getMaxY() + radius;
        final double minY = bounds.getMinY() - radius;

        return ( x < maxX ) && ( x > minX ) && ( y < maxY ) && ( y > minY );
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

    protected Wire addWire( final NodeType nodeType, final double x, final double y ) {
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

    public GateType getGateType() {
        return gateType;
    }
}
