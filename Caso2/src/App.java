import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    private static List<String> references;
    private static String matrixSize;
    private static String pageSize;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String option;

        do {
            System.out.println("Menú:");
            System.out.println("1: Generación de Referencias");
            System.out.println("2: Calcular Datos");
            System.out.println("3: salir");
            System.out.print("Seleccione una opción (Digite 1, 2 o 3): ");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Seleccionó la opción 1");
                    System.out.print("Ingrese el tamanio de la página: ");
                    pageSize = scanner.nextLine();
                    System.out.print("Ingrese el tamanio de la matriz de datos: ");
                    matrixSize = scanner.nextLine();
                    int pageSizeParameter = Integer.parseInt(pageSize);
                    int matrixSizeParameter = Integer.parseInt(matrixSize);
                    PageReferenceGenerator generator = new PageReferenceGenerator(pageSizeParameter,
                            matrixSizeParameter);
                    generator.filter();
                    references = generator.getReferences();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("referencias.txt"))) {
                        writer.write("TP=" + pageSizeParameter);
                        writer.newLine();
                        writer.write("NF=" + matrixSizeParameter);
                        writer.newLine();
                        writer.write("NC=" + matrixSizeParameter);
                        writer.newLine();
                        writer.write("NF_NC_Filtro=3");
                        writer.newLine();
                        writer.write("NR=" + generator.getNumReferences());
                        writer.newLine();
                        writer.write("NP=" + generator.getNumPages());
                        writer.newLine();
                        for (String reference : references) {
                            writer.write(reference);
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        System.err.println("Error al escribir en el archivo referencias.txt: " + e.getMessage());
                    }
                    break;

                case "2":
                    System.out.println("Seleccionó la opción 2");
                    System.out.println("Ingrese el numero de marcos de pagina");
                    int numFrames = Integer.parseInt(scanner.nextLine());
                    System.out.println("Ingrese el nombre del archivo de referencias");
                    String fileName = scanner.nextLine();
                    // Read the file
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    reader.readLine();
                    reader.readLine();
                    reader.readLine();
                    reader.readLine();
                    String nr = reader.readLine().split("=")[1];
                    int[] calls = new int[Integer.parseInt(nr)];
                    Map<Integer, Page> pages = new HashMap<Integer, Page>();
                    reader.readLine();
                    for (int i = 0; i < Integer.parseInt(nr); i++) {
                        String[] line = reader.readLine().split(",");
                        int reference = Integer.parseInt(line[1]);
                        calls[i] = reference;
                        Page page = new Page(reference);
                        pages.put(reference, page);
                    }
                    reader.close();
                    MemoryManager memoryManager = new MemoryManager(numFrames, pages, calls);

                    System.out.println("Physical" + memoryManager.getPhysicalMemory());
                    System.out.println("Swap" + memoryManager.getSwapMemory());
                    System.out.println("Physical table" + memoryManager.getPageTable());
                    System.out.println("Swap" + memoryManager.getSwapTable());
                    System.out.println("Hits: " + memoryManager.getNumHits());
                    System.out.println("Misses: " + memoryManager.getNumMisses());
                    System.out.println("Hits %: "
                            + (memoryManager.getNumHits()
                                    / (double) (memoryManager.getNumHits() + memoryManager.getNumMisses())));
                    System.out.println("Misses %: " + (memoryManager.getNumMisses()
                            / (double) (memoryManager.getNumHits() + memoryManager.getNumMisses())));
                    System.out.println("Total: " + (memoryManager.getNumHits() + memoryManager.getNumMisses()));
                    break;
                case "3":
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                    break;
            }

        } while (!option.equals("3"));

        scanner.close();
    }
    
}
