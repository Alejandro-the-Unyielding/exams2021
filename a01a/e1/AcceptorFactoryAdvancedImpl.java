package a01a.e1;

import java.lang.classfile.ClassFile.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AcceptorFactoryAdvancedImpl implements AcceptorFactory{

    @Override
    public Acceptor<String, Integer> countEmptyStringsOnAnySequence() {
        return new Acceptor<String,Integer>() {

            private int count = 0;

            @Override
            public boolean accept(String e) {
                if(e.length() == 0){
                    count++;
                }
                return true;
            }

            @Override
            public Optional<Integer> end() {
                
                return Optional.of(count);

            }
            
        };

    }

    @Override
    public Acceptor<Integer, String> showAsStringOnlyOnIncreasingSequences() {

        return new Acceptor<Integer,String>() {

            private Optional<Integer> last = Optional.empty();
            private String string = "";

			@Override
			public boolean accept(Integer e) {
				if (this.string.length()>0 && (this.last.isEmpty() || e <= this.last.get())) {
					this.last = Optional.empty();
					return false;
				}
				this.last = Optional.of(e);
				string = string + (string.length()>0 ? ":" : "") + e;
				return true;
			}

			@SuppressWarnings("unused")
			@Override
			public Optional<String> end() {
				return this.last.map(i -> string);
			}
            
        };
    }

    @Override
    public Acceptor<Integer, Integer> sumElementsOnlyInTriples() {

        return new Acceptor<Integer,Integer>() {
            private List<Integer> num = new ArrayList<Integer>();
            private int count = 0;

            @Override
            public boolean accept(Integer e) {
                if(this.count < 3){
                this.num.add(count++, e);
                return true;
                }
                count++;
                return false;

            }

            @Override
            public Optional<Integer> end() {
                if(this.count == 3){
                    return Optional.of(this.num.stream().reduce((a,b) -> a+b).get());
                }
                return Optional.empty();
            }
            
        };
    }

    @Override
    public <E, O1, O2> Acceptor<E, Pair<O1, O2>> acceptBoth(Acceptor<E, O1> a1, Acceptor<E, O2> a2) {

        return new Acceptor<E,Pair<O1,O2>>() {

			@Override
			public boolean accept(E e) {
				return a1.accept(e) && a2.accept(e);
			}

			@Override
			public Optional<Pair<O1, O2>> end() {
				return a1.end().flatMap(o1 -> a2.end().map( o2 -> new Pair<>(o1,o2)));
			}
            
        };

    }

    @Override
	public <E, O, S> Acceptor<E, O> generalised(S initial, BiFunction<E, S, Optional<S>> stateFunction,
			Function<S, Optional<O>> outputFunction) {
		return new Acceptor<>() {
			private Optional<S> state = Optional.of(initial);

			@Override
			public boolean accept(E e) {
				if (state.isEmpty()) {
					return false;
				}
				state = stateFunction.apply(e, state.get());
				return state.isPresent();
			}

			@Override
			public Optional<O> end() {
				if (state.isEmpty()) {
					return Optional.empty();
				}
				return outputFunction.apply(state.get());
			}
		};
	}

}
