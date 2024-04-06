public class Page {
    int reference;
    boolean r_bit;

    public Page(int reference) {
        this.reference = reference;
        this.r_bit = false;
    }

    public synchronized boolean getRBit() {
        return r_bit;
    }

    public synchronized void setRBit(boolean r_bit) {
        this.r_bit = r_bit;
    }

    @Override
    public String toString() {
        return "reference: " + reference;
    }

}
