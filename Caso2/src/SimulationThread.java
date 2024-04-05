public class SimulationThread extends Thread {
    MemoryManager memoryManager;

    public SimulationThread(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    public void flush() {
        try {
            Thread.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Page page : memoryManager.getPhysicalMemory()) {
            page.r_bit = false;
        }
    }
    
    @Override
    public void run() {
        flush();
    }
}
