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
    int MAX_PHYSICAL_MEMORY_SIZE;

    public static void main(String[] args) {
        Map<Integer, Page> pages = new HashMap<Integer, Page>(Map.of(
                0, new Page(0),
                1, new Page(1),
                2, new Page(2),
                3, new Page(3),
                4, new Page(4),
                5, new Page(5),
                6, new Page(6),
                7, new Page(7)));

        int[] calls = { 5, 1, 2, 4, 0, 6, 7, 7, 3, 1, 7, 0, 2, 6, 4, 2, 3, 6, 1, 7, 5, 1, 7, 5, 2, 0, 4, 3, 3, 1, 5, 0,
                4, 6, 4, 6, 3, 7, 5, 2, 2, 0, 1, 6, 0, 5, 3, 7, 4, 2, 6, 0, 7, 1, 5, 2, 3, 6, 4, 1, 3, 4, 2, 7, 6, 5 };

        Set<Page> registeredPages = new HashSet<Page>();

        MemoryManager memoryManager = new MemoryManager(4);

        for (int call : calls) {
            Page page = pages.get(call);
            if (!registeredPages.contains(page)) {
                memoryManager.loadReference(call, page);
                registeredPages.add(page);
            } else {
                memoryManager.loadReference(call);
            }
            System.out.println("Loaded reference: " + call);
            memoryManager.flush();
        }

        System.out.println(memoryManager.getPhysicalMemory());
        System.out.println(memoryManager.getSwapMemory());

    }

    public MemoryManager(int NumPageFrames) {
        this.swapMemory = new ArrayList<Page>();
        this.physicalMemory = new ArrayList<Page>(NumPageFrames);
        this.pageTable = new HashMap<Integer, Integer>();
        this.swapTable = new HashMap<Integer, Integer>();
        this.MAX_PHYSICAL_MEMORY_SIZE = NumPageFrames;
    }

    public synchronized void loadReference(int reference) {
        if (!pageTable.containsKey(reference)) {
            UpdateThread updateThread = new UpdateThread(this, reference);
            updateThread.run();
        }
    }

    public synchronized void loadReference(int reference, Page page) {
        UpdateThread updateThread = new UpdateThread(this, reference, page);
        updateThread.run();
    }

    public synchronized void flush() {
        SimulationThread simulationThread = new SimulationThread(this);
        simulationThread.run();

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

    public synchronized int getMAX_PHYSICAL_MEMORY_SIZE() {
        return MAX_PHYSICAL_MEMORY_SIZE;
    }

}
