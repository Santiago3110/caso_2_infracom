public class Page {
    int reference;
    boolean r_bit;

    public Page(int reference) {
        this.reference = reference;
        this.r_bit = false;
    }

    @Override
    public String toString() {
        return "reference: " + reference;
    }

}
