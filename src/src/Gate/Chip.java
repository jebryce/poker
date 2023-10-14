package Gate;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Gate.IOGates.ChipIO.ChipIO;
import Gate.IOGates.ChipIO.IO_Direction;
import Gate.IOGates.ChipIO.IO_Type;
import Main.Colors;
import Main.Constants;
import Wire.Node.Node;
import Wire.Wire;
import Wire.Node.NodeType;
import Wire.Wires.Wires;

public class Chip extends Gate {
    private final Arc2D    notch    = new Arc2D.Double( 40, -20, 40, 40, 180, 180, Arc2D.OPEN );
    private final Gates    contents;
    private final Wires    gateIO   = new Wires( Constants.MAX_NUM_IO );
    private final ChipIO[] chipIO   = new ChipIO[Constants.MAX_NUM_IO];

    public Chip( final Point2D location ) {
        super( location, GateType.CHIP );
        contents = new Gates();
        setBody();

        double yDiv = Constants.SCREEN_HEIGHT / Constants.SCREEN_SCALE / 8;
        double width = Constants.SCREEN_WIDTH / Constants.SCREEN_SCALE;

        addChipIO( new ChipIO( 50, yDiv*1 - 25, IO_Direction.RIGHT ) );
        addChipIO( new ChipIO( 50, yDiv*3 - 25, IO_Direction.RIGHT ) );
        addChipIO( new ChipIO( 50, yDiv*5 - 25, IO_Direction.RIGHT ) );
        addChipIO( new ChipIO( 50, yDiv*7 - 25, IO_Direction.RIGHT ) );
        addChipIO( new ChipIO( width - 100, yDiv*1 - 25, IO_Direction.LEFT  ) );
        addChipIO( new ChipIO( width - 100, yDiv*3 - 25, IO_Direction.LEFT  ) );
        addChipIO( new ChipIO( width - 100, yDiv*5 - 25, IO_Direction.LEFT  ) );
        addChipIO( new ChipIO( width - 100, yDiv*7 - 25, IO_Direction.LEFT  ) );
    }

    public Chip( final Point2D location, final Gates contents ) {
        super( location, GateType.CHIP );
        assert contents != null;
        this.contents = contents;
        setBody();
        setChipIO();
    }

    private void setBody() {
        body.append( new Rectangle2D.Double(0, 0, 120, 180), false );

        addGateWire( NodeType.INPUT, 0, 30 );
        addGateWire( NodeType.INPUT, 0, 70 );
        addGateWire( NodeType.INPUT, 0, 110 );
        addGateWire( NodeType.INPUT, 0, 150 );
        addGateWire( NodeType.OUTPUT, 120, 30 );
        addGateWire( NodeType.OUTPUT, 120, 70 );
        addGateWire( NodeType.OUTPUT, 120, 110 );
        addGateWire( NodeType.OUTPUT, 120, 150 );
    }

    private void setChipIO() {
        for ( Gate gate : contents ) {
            if ( gate instanceof ChipIO io ) {
//                if ( io.getIO_Direction() == IO_Direction.LEFT ) {
//                    io.setIO_Type( IO_Type.INPUT );
//                } else {
//                    io.setIO_Type( IO_Type.OUTPUT );
//                }
                for ( int i = 0; i < Constants.MAX_NUM_IO; i++ ) {
                    if ( chipIO[i] != null ) {
                        continue;
                    }
                    chipIO[i] = io;
                    break;
                }
            }
        }
    }

    @Override
    public void place() {
        for ( Wire wire : gateIO ) {
            wire.move( location );
        }
    }

    private void addGateWire( final NodeType nodeType, final double x, final double y ) {
        gateIO.addFirst( new Wire( nodeType, x, y ) );
    }

    private void addChipIO( final ChipIO newChipIO ) {
        contents.add( newChipIO );
        newChipIO.place();
        for ( int i = 0; i < Constants.MAX_NUM_IO; i++ ) {
            if ( chipIO[i] != null ) {
                continue;
            }
            chipIO[i] = newChipIO;
            break;
        }
    }

    public Wires getGateIO() {
        return gateIO;
    }

    @Override
    public void update() {
        contents.update();
        for ( int i = 0; i < Constants.MAX_NUM_IO; i++ ) {
            if ( chipIO[i].getIO_Type() == IO_Type.INPUT ) {
                gateIO.getIndex( i ).setState( chipIO[i].getState() );
            }
            else {
                chipIO[i].setState( gateIO.getIndex( i ).getState() );
            }
        }
    }

    private void repaintBody( final Graphics2D graphics2D ) {
        graphics2D.setColor(Colors.GRAY);
        graphics2D.fill(body);
        graphics2D.setColor(Colors.BLACK);
        graphics2D.fill(notch);
    }

    @Override
    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.translate(location.getX(), location.getY());
        repaintBody( graphics2D );
        graphics2D.translate(-location.getX(), -location.getY());
        super.repaint( graphics2D );
    }

    @Override
    public void repaintInHand( final Graphics2D graphics2D ) {
        graphics2D.translate(location.getX(), location.getY());
        for ( Wire wire : gateIO ) {
            wire.repaint( graphics2D );
        }
        repaintBody( graphics2D );
        graphics2D.translate(-location.getX(), -location.getY());
        super.repaintInHand( graphics2D );
    }

    @Override
    public boolean contains( final Wire wire ) {
        return gateIO.contains( wire );
    }

    @Override
    public void replaceWire( Wire oldWire, Wire newWire ) {
        if ( gateIO.removeFirst( oldWire ) ) {
            gateIO.addFirst( newWire );
            return;
        }
        assert false : oldWire + " is not attached to this gate.";
    }

    public Gates getContents() {
        return contents;
    }

    @Override
    public void moveInputs() {
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            if ( chipIO[index].getIO_Type() == IO_Type.OUTPUT ) {
                gateIO.getIndex( index ).move( location );
            }
        }
    }

    @Override
    public void removeAllOutputs() {
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            if ( chipIO[index].getIO_Type() == IO_Type.INPUT ) {
                gateIO.setIndexToNull( index );
            }
        }
    }

    @Override
    public void addOutputWire( final Wire newWire ) {
        gateIO.addFirst( newWire );
    }

    @Override
    public Wires getOutputWires() {
        Wires returnWires = new Wires( Constants.MAX_NUM_IO );
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            if ( chipIO[index].getIO_Type() == IO_Type.INPUT ) {
                returnWires.add( gateIO.getIndex( index ) );
            }
        }

        return returnWires;
    }

    @Override
    public void replaceInputWireAtNode( Wire newWire, Node node ) {
        gateIO.replaceWireAtNode( newWire, node );
    }
}
