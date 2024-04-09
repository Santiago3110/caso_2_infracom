import java.util.List;
import java.util.Map;

public class UpdateThread extends Thread {
    List<Page> swapMemory;
    List<Page> physicalMemory;
    MemoryManager memoryManager;

    Map<Integer, Integer> pageTable;
    Map<Integer, Integer> swapTable;

    int MAX_PHYSICAL_MEMORY_SIZE;

    int reference;
    Page page;
    boolean is_page = false;

    public UpdateThread(MemoryManager memoryManager, int reference) {
        this.memoryManager = memoryManager;
        this.swapMemory = memoryManager.getSwapMemory();
        this.physicalMemory = memoryManager.getPhysicalMemory();
        this.pageTable = memoryManager.getPageTable();
        this.swapTable = memoryManager.getSwapTable();
        this.MAX_PHYSICAL_MEMORY_SIZE = memoryManager.getMAX_PHYSICAL_MEMORY_SIZE();
        this.reference = reference;
    }

    public UpdateThread(MemoryManager memoryManager, int reference, Page page) {
        this(memoryManager, reference);
        this.page = page;
        this.is_page = true;
    }

    @Override
    public void run() {
        if (!is_page)
            load(reference);
        else {
            loadInSwap(reference, page);
            load(reference);
        }

    }

    public void load(int reference) {
        clock();
        synchronized (memoryManager) {
            if (pageTable.containsKey(reference)) {
                Page page = physicalMemory.get(pageTable.get(reference));
                page.setRBit(true);
                memoryManager.setNumHits(memoryManager.getNumHits() + 1);
                return;
            }
            memoryManager.setNumMisses(memoryManager.getNumMisses() + 1);
            int page_swap = swapTable.get(reference);
            Page swap_data = swapMemory.get(page_swap);
            int page_pm = find_free_frame();
            if (page_pm == -1) {
                int page_to_replace_index = find_replace_frame();
                int page_swap_index = swapTable.get(reference);
                swap(page_to_replace_index, page_swap_index);
            } else {
                swapMemory.remove(page_swap);
                swapTable.remove(reference);
                physicalMemory.add(page_pm, swap_data);
                pageTable.put(reference, page_pm);
                swap_data.setRBit(true);
            }
        }
    }

    public void loadInSwap(int reference, Page page) {
        clock();
        synchronized (memoryManager) {
            swapMemory.add(page);
            swapTable.put(reference, swapMemory.size() - 1);
            load(reference);
        }
    }

    private void swap(int page_pm_index, int page_swap_index) {
        synchronized (memoryManager) {
            Page pm_page = physicalMemory.get(page_pm_index);
            Page swap_page = swapMemory.get(page_swap_index);

            physicalMemory.set(page_pm_index, swap_page);
            swapMemory.set(page_swap_index, pm_page);
            pm_page.setRBit(false);
            swap_page.setRBit(true);

            pageTable.put(swap_page.reference, page_pm_index);
            swapTable.put(pm_page.reference, page_swap_index);
            pageTable.remove(pm_page.reference);
            swapTable.remove(swap_page.reference);
        }
    }

    private int find_free_frame() {
        synchronized (memoryManager) {
            if (physicalMemory.size() < this.MAX_PHYSICAL_MEMORY_SIZE)
                return Math.max(physicalMemory.size() - 1, 0);
            return -1;
        }
    }

    private int find_replace_frame() {
        synchronized (memoryManager) {
            for (int i = 0; i < physicalMemory.size(); i++) {
                Page page = physicalMemory.get(i);
                if (!page.getRBit())
                    return i;
            }
            return 0;
        }
    }

    private void clock() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
