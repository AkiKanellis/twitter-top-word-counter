package utilities.generalutils;

/**
 * Template class for creating a pair of two objects.
 *
 * @param <L> the first object
 * @param <R> the first object
 */
public class Pair<L, R> {

    /**
     * Constructor which creates the pair of the two objects
     *
     * @param left the first object
     * @param right the first object
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     *
     * @return the hashcode of the two objects
     */
    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }

    /**
     * Compares two Pair objects for equality by checking if both left and right
     * are equal with the left and right of the other object.
     *
     * @param o the other object
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair pairo = (Pair) o;
        return this.left.equals(pairo.left)
                && this.right.equals(pairo.right);
    }

    public final L left;
    public final R right;
}
