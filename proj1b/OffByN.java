public class OffByN implements CharacterComparator {

    private int mOffset;

    public OffByN(int N) {
        mOffset = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == mOffset ? true : false;
    }
}
