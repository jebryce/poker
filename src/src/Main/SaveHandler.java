package Main;

import Gate.Gates;
import Main.KeyHandler.Key;
import Main.KeyHandler.KeyHandler;

import java.awt.*;

public class SaveHandler {
    private static final StringBuilder chipName = new StringBuilder();
    private static       Gates  contents = null;

    public static void repaint( final Graphics2D graphics2D ) {
        updateChipName();

        int width  = (int) ( 400 / Constants.SCREEN_SCALE );
        int height = (int) ( 200 / Constants.SCREEN_SCALE );
        int x      = (int) ( Constants.SCREEN_WIDTH  / Constants.SCREEN_SCALE - width  ) / 2;
        int y      = (int) ( Constants.SCREEN_HEIGHT / Constants.SCREEN_SCALE - height ) / 2;


        graphics2D.setColor( Colors.DARK_EGGSHELL );
        graphics2D.fillRect( x, y, width, height );
        graphics2D.setColor( Colors.BLACK );
        graphics2D.drawRect( x, y, width, height );


        graphics2D.drawString( "Enter a name for this chip you are saving:",
                x + (int) (2 / Constants.SCREEN_SCALE), y + (int) (12 / Constants.SCREEN_SCALE)
        );

        graphics2D.drawString( chipName.toString(),
                x + (int) (2 / Constants.SCREEN_SCALE), y + (int) (24 / Constants.SCREEN_SCALE)
        );
    }

    private static void updateChipName() {
        for ( Key key : KeyHandler.get().getKeysTyped() ) {
            if ( key.getKey() == '\b' && chipName.length() > 0 ) {
                chipName.deleteCharAt( chipName.length() - 1 );
            } else {
                chipName.append( key.getKey() );
            }
        }
    }

    public static void setContents( final Gates gates ) {
        contents = gates;
    }
}
