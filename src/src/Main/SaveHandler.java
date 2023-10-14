package Main;

import Gate.Gates;

import java.awt.*;

public class SaveHandler {
    private static Gates contents = null;

    public static void repaint( final Graphics2D graphics2D ) {
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



    }

    public static void setContents( final Gates gates ) {
        contents = gates;
    }
}
