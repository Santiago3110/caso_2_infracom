import java.util.Scanner;

public class App {


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String option;

        do {
            // Mostrar el menú
            System.out.println("Menú:");
            System.out.println("1: Generación de Referencias");
            System.out.println("2: Calcular Datos");
            System.out.println("3: salir");
            System.out.print("Seleccione una opción (Digite 1, 2 o 3): ");

            // Leer la opción ingresada por el usuario
            option = scanner.nextLine();

            // Procesar la opción seleccionada
            switch (option) {
                case "1":
                    System.out.println("Seleccionó la opción 1");
                    // Agrega aquí la lógica para la opción 1
                    break;
                case "2":
                    System.out.println("Seleccionó la opción 2");
                    // Agrega aquí la lógica para la opción 2
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
