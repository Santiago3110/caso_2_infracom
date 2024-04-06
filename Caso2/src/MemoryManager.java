import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemoryManager {
    List<Page> swapMemory;
    List<Page> physicalMemory;
    Map<Integer, Integer> pageTable;
    Map<Integer, Integer> swapTable;
    int numHits;
    int numMisses;
    int MAX_PHYSICAL_MEMORY_SIZE;
    boolean all_processed = false;

    public MemoryManager(int NumPageFrames, Map<Integer, Page> pages, int[] calls) {
        this.swapMemory = new ArrayList<Page>();
        this.physicalMemory = new ArrayList<Page>(NumPageFrames);
        this.pageTable = new HashMap<Integer, Integer>();
        this.swapTable = new HashMap<Integer, Integer>();
        this.MAX_PHYSICAL_MEMORY_SIZE = NumPageFrames;
        this.numHits = 0;
        this.numMisses = 0;

        Set<Page> registeredPages = new HashSet<Page>();
        flush();
        for (int call : calls) {
            Page page = pages.get(call);
            if (!registeredPages.contains(page)) {
                loadReference(call, page);
                registeredPages.add(page);
            } else {
                loadReference(call);
            }
        }
        all_processed = true;
    }

    public void loadReference(int reference) {
        UpdateThread updateThread = new UpdateThread(this, reference);
        updateThread.start();
        try {
            updateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadReference(int reference, Page page) {
        UpdateThread updateThread = new UpdateThread(this, reference, page);
        updateThread.start();
        try {
            updateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        SimulationThread simulationThread = new SimulationThread(this);
        simulationThread.start();
    }

    public synchronized List<Page> getSwapMemory() {
        return swapMemory;
    }

    public synchronized List<Page> getPhysicalMemory() {
        return physicalMemory;
    }

    public synchronized Map<Integer, Integer> getPageTable() {
        return pageTable;
    }

    public synchronized Map<Integer, Integer> getSwapTable() {
        return swapTable;
    }

    public int getNumHits() {
        return numHits;
    }

    public void setNumHits(int numHits) {
        this.numHits = numHits;
    }

    public int getNumMisses() {
        return numMisses;
    }

    public void setNumMisses(int numMisses) {
        this.numMisses = numMisses;
    }

    public int getMAX_PHYSICAL_MEMORY_SIZE() {
        return MAX_PHYSICAL_MEMORY_SIZE;
    }

}