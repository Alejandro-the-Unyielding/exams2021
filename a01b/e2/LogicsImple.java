package a01b.e2;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

public class LogicsImple implements Logic{

    private final int size;

    private Optional<Pair<Integer,Integer>> first = Optional.empty();
    private Optional<Pair<Integer,Integer>> second = Optional.empty();


    private Set<Pair<Integer,Integer>> selected = new HashSet<>();

    public LogicsImple(final int size){
        this.size = size;
    }



    @Override
    public HitType hit(int x, int y) {
        var p = new Pair<>(x, y);
        if(this.first.isEmpty()){
            this.first = Optional.of(p);
            return HitType.FIRST;
        }
        
            if(this.second.isEmpty() && (p.getX() == this.first.get().getX() || p.getY() == this.first.get().getY()) && p != this.first.get()){
                this.second = Optional.of(p);
                return HitType.SECOND;
            }

                if(p != this.first.get() && p != this.second.get() && (p.getX().equals(this.first.get().getX()) || p.getY().equals(this.first.get().getY()))){
                    boolean check = (p.getX() == this.second.get().getX() ?
                                false
                                :
                                (p.getY().equals(this.second.get().getY()) ?
                                false
                                :
                                true)
                                );

                    if(check){
                        updateSelection(this.first.get(),this.second.get(),p);
                        this.first = Optional.empty();
                        this.second = Optional.empty();
                        return HitType.THIRD;
                    }
                }

            return HitType.NOT_VALID;
    }

    private void updateSelection(Pair<Integer,Integer> one,Pair<Integer,Integer> two,Pair<Integer,Integer> three){

        for (var x : range(one.getX(),two.getX())) {
            for (var y : range(one.getY(), two.getY())) {
                this.selected.add(new Pair<>(x, y));
            }
        }

        for (var x : range(one.getX(),three.getX())) {
            for (var y : range(one.getY(), three.getY())) {
                this.selected.add(new Pair<>(x, y));
            }
        }

    }

    private Iterable<Integer> range(int x, int x1){
        return x<=x1 ? ()->IntStream.rangeClosed(x,x1).iterator() : ()->IntStream.rangeClosed(x1, x).iterator();
    }

    @Override
    public boolean isSelected(int x, int y) {
        var t = new Pair<>(x, y);
        return this.selected.contains(t);

    }

    @Override
    public boolean isOver() {
        return this.selected.size() == this.size;
    }

}
