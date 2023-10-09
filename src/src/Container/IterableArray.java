package Container;

import Wire.Wire;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class IterableArray<W> implements Iterable<W>{
    private final int maxLength;
    private final Object[] contents;
    private       int currentLength = 0;

    protected IterableArray( final int maxLength ) {
        this.maxLength = maxLength;
        contents = new Object[maxLength];
    }

    public W getIndex( final int index ) {
        assert index < maxLength;
        return (W) contents[index];
    }


    public W add( final W newItem) {
        if ( currentLength < maxLength ) {
            contents[currentLength++] = newItem;
            return newItem;
        }
        return newItem;
    }

    public boolean remove( final W itemToRemove ) {
        boolean isItemRemoved = false;
        for( int index = 0; index < currentLength; index++ ) {
            if ( contents[index] == itemToRemove ) {
                isItemRemoved = true;
                currentLength--;
            }
            if ( isItemRemoved ) {
                contents[index] = contents[index + 1];
            }
        }
        return isItemRemoved;
    }

    public void removeAll() {
        Arrays.fill( contents, null );
        currentLength = 0;
    }

    public W getFirst() {
        return (W) contents[0];
    }

    @Override
    public Iterator< W > iterator() {
        return new Iterator<>() {
            private int nextIndex = 0;

            @Override
            public boolean hasNext() {
                if ( nextIndex < maxLength ) {
                    return contents[nextIndex] != null;
                }
                return false;
            }

            @Override
            public W next() {
                return (W) contents[nextIndex++];
            }
        };
    }

    @Override
    public void forEach( Consumer< ? super W > action ) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator< W > spliterator() {
        return Iterable.super.spliterator();
    }

    public boolean contains( final W item ) {
        for ( Object content : contents ) {
            if ( content == item ) {
                return true;
            }
        }
        return false;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
