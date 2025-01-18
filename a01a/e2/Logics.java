package a01a.e2;

import java.util.Set;

@SuppressWarnings("unused")
public interface Logics{
	
	enum HitType {
		FIRST, SECOND
	}
	
	HitType hit(int x, int y);
	
	boolean isSelected(int x, int y);
	
	boolean isOver();
    
}
