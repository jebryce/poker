package Main;

import Gate.Gates;
import GsonAdapters.GsonGatesAdapter;
import Wire.Node.NodeType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;

public class LoadHandler {
    private static LoadHandler   instance  = null;
    private final  Gson          gson;
    private final  Path2D        chipBody  = new Path2D.Double();
    private final  Arc2D         chipNotch = new Arc2D.Double( 40, -20, 40, 40, 180, 180, Arc2D.OPEN );

    private LoadHandler() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Gates.class, new GsonGatesAdapter());
        gson = builder.create();
        initChipBody();
    }

    private void initChipBody() {
        chipBody.append( new Rectangle2D.Double( 0, 0, 120, 180 ), false );

        chipBody.append( new Line2D.Double( 0, 30, -Constants.MIN_LINE_LENGTH, 30 ), false );
        chipBody.append( new Line2D.Double( 0, 70, -Constants.MIN_LINE_LENGTH, 70 ), false );
        chipBody.append( new Line2D.Double( 0, 110, -Constants.MIN_LINE_LENGTH, 110 ), false );
        chipBody.append( new Line2D.Double( 0, 150, -Constants.MIN_LINE_LENGTH, 150 ), false );

        chipBody.append( new Line2D.Double( 120, 30,  120 + Constants.MIN_LINE_LENGTH, 30 ), false );
        chipBody.append( new Line2D.Double( 120, 70,  120 + Constants.MIN_LINE_LENGTH, 70 ), false );
        chipBody.append( new Line2D.Double( 120, 110, 120 + Constants.MIN_LINE_LENGTH, 110 ), false );
        chipBody.append( new Line2D.Double( 120, 150, 120 + Constants.MIN_LINE_LENGTH, 150 ), false );
    }

    public static LoadHandler get() {
        if ( instance == null ) {
            instance = new LoadHandler();
        }
        return instance;
    }

    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.DARK_EGGSHELL );
        graphics2D.fillRect(
                0, Constants.SCREEN_HEIGHT - Constants.CHIP_SPACE_HEIGHT, Constants.SCREEN_WIDTH, Constants.CHIP_SPACE_HEIGHT
        );

        int yOffset = ( Constants.CHIP_SPACE_HEIGHT - 180 ) / 2;
        int xOffset = (int) ( Constants.MIN_LINE_LENGTH * 1.5 );
        graphics2D.setColor( Colors.BLACK );
        graphics2D.translate( xOffset, Constants.SCREEN_HEIGHT - Constants.CHIP_SPACE_HEIGHT + yOffset );


        int fontHeight = (int) ( 20 / Constants.SCREEN_SCALE );
        graphics2D.setFont( new Font( Font.MONOSPACED, Font.PLAIN, fontHeight ) );

        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int textYOffset = ( fontMetrics.getAscent() - fontMetrics.getDescent() + 180 ) / 2;

        int totalOffset = 0;
        int offset = 120 + 2*Constants.MIN_LINE_LENGTH + xOffset;
        File savePath = new File( Constants.FULL_SAVE_PATH );
        for ( String fileName : Objects.requireNonNull( savePath.list() ) ) {
            String chipName = fileName.substring(0, fileName.length()-4);
            int textXOffset = ( 120 - fontMetrics.stringWidth( chipName ) ) / 2;
            drawChip( graphics2D );
            graphics2D.setColor( Colors.WHITE );
            graphics2D.drawString( chipName, textXOffset, textYOffset );
            graphics2D.translate( offset, 0 );
            totalOffset += offset;
        }

        graphics2D.translate( -totalOffset, 0 );
        graphics2D.translate( -xOffset, - Constants.SCREEN_HEIGHT + Constants.CHIP_SPACE_HEIGHT - yOffset );
    }

    private void drawChip( final Graphics2D graphics2D ) {
        graphics2D.setColor(Colors.GRAY);
        graphics2D.fill( chipBody );
        graphics2D.setColor(Colors.BLACK);
        graphics2D.fill( chipNotch );
        graphics2D.draw( chipBody );
    }
}
