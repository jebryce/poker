package Main;

import Gate.Gates;
import GsonAdapters.GsonGatesAdapter;
import Main.KeyHandler.Key;
import Main.KeyHandler.KeyHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class SaveHandler {
    private final  StringBuilder chipName = new StringBuilder();
    private        Gates         contents = null;
    private static SaveHandler   instance = null;
    private final  Gson          gson;


    private SaveHandler() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter( Gates.class, new GsonGatesAdapter() );
        gson = builder.create();
    }

    public static SaveHandler get() {
        if ( instance == null ) {
            instance = new SaveHandler();
        }
        return instance;
    }

    public void repaint( final Graphics2D graphics2D ) {
        updateChipName();

        int width  = (int) ( 400 / Constants.SCREEN_SCALE );
        int height = (int) ( 200 / Constants.SCREEN_SCALE );
        int x      = (int) ( Constants.SCREEN_WIDTH  / Constants.SCREEN_SCALE - width  ) / 2;
        int y      = (int) ( Constants.SCREEN_HEIGHT / Constants.SCREEN_SCALE - height ) / 2;


        graphics2D.setColor( Colors.DARK_EGGSHELL );
        graphics2D.fillRect( x, y, width, height );
        graphics2D.setColor( Colors.GRAY );
        graphics2D.drawRect( x, y, width, height );

        graphics2D.setFont( new Font( Font.MONOSPACED, Font.PLAIN, (int) (12 / Constants.SCREEN_SCALE) ) );
        graphics2D.drawString( "Enter a name for this chip you are saving:",
                x + (int) (2 / Constants.SCREEN_SCALE), y + (int) (12 / Constants.SCREEN_SCALE)
        );

        graphics2D.setColor( Colors.BLACK );
        graphics2D.setFont( new Font( Font.MONOSPACED, Font.PLAIN, (int) (18 / Constants.SCREEN_SCALE) ) );
        graphics2D.drawString( chipName + "_",
                x + (int) (2 / Constants.SCREEN_SCALE), y + (int) (30 / Constants.SCREEN_SCALE)
        );
    }

    private void updateChipName() {
        for ( Key key : KeyHandler.get().getKeysTyped() ) {
            if ( key.getKey() == '\b' && !chipName.isEmpty() ) {
                chipName.deleteCharAt( chipName.length() - 1 );
            } else if ( key.getKey() == '\n' && !chipName.isEmpty() ) {
                writeContents();
            } else {
                chipName.append( key.getKey() );
            }
        }
    }

    public void setContents( final Gates gates ) {
        contents = gates;
    }

    private void writeContents() {
        assert !chipName.isEmpty() : "Cannot create a file without a name.";
        try ( FileWriter fileWriter = new FileWriter( Constants.SAVE_PATH + chipName + ".jeb" ) ){
            fileWriter.write( gson.toJson( contents ) );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void resetChipName() {
        chipName.delete( 0, chipName.length() );
    }
}
