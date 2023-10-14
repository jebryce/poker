package Container;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Queue < W extends ListItem > implements Iterable<W> {
    private W head = null;
    private W tail = null;

    public void add( final W newItem ) {
        assert newItem != null;
        if ( head == null ) {
            head = newItem;
            tail = newItem;
        } else {
            head.setPrev( newItem );
            newItem.setNext( head );
            head = newItem;
        }

    }

    public W pop() {
        if ( tail == null ) {
            return null;
        } else if ( head == tail ) {
            W returnItem = tail;
            head = null;
            tail = null;
            return returnItem;
        } else {
            W returnItem = tail;
            tail = (W) tail.getPrev();
            tail.setNext( null );
            return returnItem;
        }
    }

    @Override
    public Iterator< W > iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return tail != null;
            }

            @Override
            public W next() {
                return pop();
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

    public void clear() {
        head = null;
        tail = null;
    }
}
