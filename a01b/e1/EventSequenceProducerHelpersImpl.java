package a01b.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class EventSequenceProducerHelpersImpl implements EventSequenceProducerHelpers{

    @Override
    public <E> EventSequenceProducer<E> fromIterator(Iterator<Pair<Double, E>> iterator) {

        return ()->{
            if(!iterator.hasNext()){
                throw new NoSuchElementException();
            }
            return iterator.next();
        };

    }

    @Override
    public <E> List<E> window(EventSequenceProducer<E> sequence, double fromTime, double toTime) {
        List<E> s = new ArrayList<>();
        while(true){
            Pair<Double,E> p = sequence.getNext();
            double time = p.get1();
            if(time >= fromTime && time<= toTime){
                s.add(p.get2());
            } 
            if(time > toTime) break;
        }
        return s;

    }

    @Override
    public <E> Iterable<E> asEventContentIterable(EventSequenceProducer<E> sequence) {

        return ()-> new Iterator<E>() {
            private Pair<Double,E> nextEvent;
            private boolean nextFetched = false;


            public boolean hasNext() {
                if (!nextFetched) {
                    try {
                        nextEvent = sequence.getNext();
                        nextFetched = true;
                    } catch (NoSuchElementException e) {
                        nextEvent = null;
                    }
                }
                return nextEvent != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                nextFetched = false;
                return nextEvent.get2();
            }
        };

    }

    @Override
    public <E> Optional<Pair<Double, E>> nextAt(EventSequenceProducer<E> sequence, double time) {
        Optional<Pair<Double, E>> r = Optional.empty();
        while(true){
            try{
                var s = sequence.getNext();
            if(s.get1() > time){
                r = Optional.of(s);
                break;
            }}
            catch(NoSuchElementException e){
                break;
            }

        }
        return r;
    }

    @Override
    public <E> EventSequenceProducer<E> filter(EventSequenceProducer<E> sequence, Predicate<E> predicate) {
        return ()->{

            while(true){
                var tmp = sequence.getNext();
                if(predicate.test(tmp.get2())){
                    return tmp;
                }
            }

        };
    }

}
