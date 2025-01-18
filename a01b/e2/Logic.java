package a01b.e2;

public interface Logic {
    
    public enum HitType{
        FIRST,
        SECOND,
        THIRD,
        NOT_VALID;
    }

    HitType hit(final int x, final int y);

    boolean isSelected(final int x, final int y);

    boolean isOver();



}
