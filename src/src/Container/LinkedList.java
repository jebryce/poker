package Container;

import java.util.Iterator;

public class LinkedList< W extends ListItem> implements Iterable<W> {
    private ListItem head = null;

    public Iterator< W > iterator() {
        return new Iterator<>() {
            private ListItem currentItem = null;
            private ListItem nextItem    = head;

            @Override
            public boolean hasNext() {
                return nextItem != null;
            }

            @Override
            public W next() {
                currentItem = nextItem;
                nextItem = nextItem.getNext();
                return (W) currentItem;
            }
        };
    }

    public void add( final W newItem ) {
        newItem.setNext( head);
        head = newItem;
    }

    public void remove( final W itemToRemove ) {
        W previousItem = null;
        for ( W currentItem : this ) {
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
