import java.util.List;
import java.util.Map;

public class UpdateThread extends Thread {
    List<Page> swapMemory;
    List<Page> physicalMemory;

    Map<Integer, Integer> pageTable;
    Map<Integer, Integer> swapTable;

    int MAX_PHYSICAL_MEMORY_SIZE;

    int reference;
    Page page;

    public UpdateThread(MemoryManager memoryManager, int reference) {
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
    }

    @Override
    public void run() {
        if (page.equals(null))
            load(reference);
        else
            loadInSwap(reference, page);
    }

    public synchronized void load(int reference) {
        clock();
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
            swap_data.r_bit = true;
        }
    }

    public synchronized void loadInSwap(int reference, Page page) {
        clock();
        swapMemory.add(page);
        swapTable.put(reference, swapMemory.size() - 1);
        load(reference);
    }

    private synchronized void swap(int page_pm_index, int page_swap_index) {
        Page pm_page = physicalMemory.get(page_pm_index);
        Page swap_page = swapMemory.get(page_swap_index);

        physicalMemory.set(page_pm_index, swap_page);
        swapMemory.set(page_swap_index, pm_page);
        pm_page.r_bit = false;
        swap_page.r_bit = true;

        pageTable.put(swap_page.reference, page_pm_index);
        swapTable.put(pm_page.reference, page_swap_index);
    }

    private synchronized int find_free_frame() {
        if (physicalMemory.size() < this.MAX_PHYSICAL_MEMORY_SIZE)
            return Math.max(physicalMemory.size() - 1, 0);
        return -1;
    }

    private synchronized int find_replace_frame() {
        for (int i = 0; i < physicalMemory.size(); i++) {
            Page page = physicalMemory.get(i);
            if (!page.r_bit)
                return i;
        }
        return 0;
    }

    private void clock() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
