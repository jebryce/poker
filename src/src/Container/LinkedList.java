package Container;

import java.util.Iterator;

public class LinkedList< W extends ListItem> implements Iterable<W> {
    private ListItem head = null;

    public Iterator< W > iterator() {
        return new Iterator<>() {
            private ListItem currentItem = head;

            @Override
            public boolean hasNext() {
                if ( currentItem == null ) {
                    return false;
                }
                return currentItem.hasNext();
            }

            @Override
            public W next() {
                return (W) ( currentItem = currentItem.getNext() );
            }
        };
    }

    public void add( final W newItem ) {
        newItem.setNext( head);
        head = newItem;
    }

    public void remove( final W itemToRemove ) {
        Iterator<W> thisIterator = iterator();
        ListItem currentItem;
        ListItem previousItem = null;
        while ( thisIterator.hasNext() ) {
            currentItem = thisIterator.next();
            assert currentItem != null;
            if ( currentItem == itemToRemove ) {
                if ( previousItem == null ) {
                    head = currentItem.getNext();
                } else {
                    previousItem.setNext( currentItem.getNext() );
                }
                return;
            }
            previousItem = currentItem;
        }
    }
}
