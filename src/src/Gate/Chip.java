package Gate;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Gate.IOGates.ChipIO;
import Gate.IOGates.IO_Direction;
import Main.Colors;
import Main.Constants;
import Wire.Node.NodeType;
import Wire.Wires;
import Wire.Wire;

public class Chip extends Gate {
    private final Arc2D notch      = new Arc2D.Double( 40, -20, 40, 40, 180, 180, Arc2D.OPEN );
    private final Gates contents   = new Gates();
    private final Wires ioWires    = new Wires(Constants.MAX_NUM_IO);

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

    private void addChipIO( final ChipIO newChipIO ) {
        contents.add( newChipIO );
        newChipIO.place();
        ioWires.add( newChipIO.getOutputWires().getFirst() );
    }

    @Override
    public void update() {
        contents.update();
        int halfIO = Constants.MAX_NUM_IO/2;
        for ( int index = 0; index < halfIO; index++ ) {
            ioWires.getIndex( index ).setState( inputWires.getIndex( index ).getState() );
            outputWires.getIndex( index ).setState( ioWires.getIndex( index + halfIO ).getState() );
        }
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
