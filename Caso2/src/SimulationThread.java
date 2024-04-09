public class SimulationThread extends Thread {
    MemoryManager memoryManager;

    public SimulationThread(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    public void flush() {
        synchronized (memoryManager){
            for (Page page : memoryManager.getPhysicalMemory()) {
                page.setRBit(false);
            }
        }
    }

    @Override
    public void run() {
        while (!memoryManager.all_processed) {
            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flush();
        }
    }
}
