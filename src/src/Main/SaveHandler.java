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
    private        boolean       selected = false;

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
        graphics2D.setColor( Colors.DARK_EGGSHELL );
        graphics2D.fillRect( 0, 0, Constants.SCREEN_WIDTH, Constants.NAME_SPACE_HEIGHT );

        int fontHeight = (int) ( 35 / Constants.SCREEN_SCALE );
        int offset = (int) ( 12 / Constants.SCREEN_SCALE );

        graphics2D.setFont( new Font( Font.MONOSPACED, Font.PLAIN, fontHeight ) );
        graphics2D.setColor( Colors.GRAY );

        if ( selected ) {
            graphics2D.drawString( chipName  + "_", offset, Constants.NAME_SPACE_HEIGHT - offset );
            updateChipName();
        } else {
            graphics2D.drawString( String.valueOf( chipName ), offset, Constants.NAME_SPACE_HEIGHT - offset );
        }
    }

    private void updateChipName() {
        for ( Key key : KeyHandler.get().getKeysTyped() ) {
            if ( key.getKey() == '\b' ) {
                if ( !chipName.isEmpty() ) {
                    chipName.deleteCharAt(chipName.length() - 1);
                }
            } else if ( key.getKey() == '\n' ) {
                if ( !chipName.isEmpty() && contents.getLength() > 0 ) {
                    writeContents();
                }
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
        assert contents.getLength() > 0;
        try ( FileWriter fileWriter = new FileWriter( Constants.SAVE_PATH + chipName + ".jeb" ) ){
            fileWriter.write( gson.toJson( contents ) );
            resetChipName();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void resetChipName() {
        chipName.delete( 0, chipName.length() );
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }
}
