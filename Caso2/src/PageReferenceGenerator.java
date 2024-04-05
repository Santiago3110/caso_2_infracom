import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PageReferenceGenerator {
    private int[][] mat1;
    private int[][] mat2 ={{0, -1, 0},
                           {-1, 5, -1},
                           {0, -1, 0}};
    private int[][] mat3;
    private int mat1Offset;
    private int mat3Offset;
    private int pageSize;
    private int matrixRows;
    private int matrixCols;
    private int filterSize = 3; // Tamaño del filtro (3x3)
    private int numPages;
    private int numReferences;
    private List<String> references; // Lista para almacenar las referencias

    public PageReferenceGenerator(int pageSize, int matrixSize){
        this.pageSize = pageSize;
        this.matrixCols = matrixSize;
        this.matrixRows = matrixSize;

        // Calcular el número de páginas virtuales necesarias
        int totalSize = (matrixRows * matrixCols + filterSize * filterSize + matrixRows * matrixCols) * 4; // 4 bytes por entero
        this.numPages = (int) Math.ceil((double) totalSize / pageSize);

        // Inicializar las matrices2026
        mat1 = new int[matrixRows][matrixCols];
        mat3 = new int[matrixRows][matrixCols];

        //calculo de los offsets
        mat1Offset =  filterSize * filterSize * 4;
        mat3Offset =  (filterSize * filterSize * 4) + (matrixRows * matrixCols * 4);

        // Inicializar la lista de referencias
        references = new ArrayList<>();

        Random rand = new Random();
        for(int i = 0; i<matrixSize;i++)
        {
            for(int j=0;j<matrixSize;j++)
            {
                int numeroAleatorio = rand.nextInt(256);
                mat1[i][j] = numeroAleatorio;
            }
        }
    }

    public void filter(){
        for(int i = 1; i < matrixRows - 1; i++){
            for(int j = 1; j < matrixCols - 1; j++){
                int acum = 0;
                for(int a = -1; a <= 1; a++){
                    for(int b = -1; b <= 1; b++){
                        int i2 = i + a;
                        int j2 = j + b;
                        int i3 = 1 + a;
                        int j3 = 1 + b;
                        acum += (mat2[i3][j3]*mat1[i2][j2]);

                        // Generar referencias de lectura para el filtro y la matriz de datos
                        generateReference(mat1, i2, j2, "R");
                        generateReference(mat2, i3, j3, "R");
                    }
                }
                if(acum >= 0 && acum <= 255){
                    mat3[i][j] = 0;
                } else if(acum < 0){
                    mat3[i][j] = 0;
                } else {
                    mat3[i][j] = 255;
                }

                generateReference(mat3, i, j, "W");
            }
        }
        for(int i = 0; i < matrixCols; i++){
            mat3[0][i] = 0;
            mat3[matrixRows-1][i] = 255;

            generateReference(mat3, 0, i, "W");
            generateReference(mat3, matrixRows - 1, i, "W");
        }
        for(int i=1; i < matrixRows; i++){
            mat3[i][0] = 0;
            mat3[i][matrixCols-1] = 255;

            generateReference(mat3, i, 0, "W");
            generateReference(mat3, i, matrixCols - 1, "W");
        }
        System.out.println("El documento con las referencias fue generado exitosamente.");
    }

    private void generateReference(int[][] matrix, int row, int col, String operation) {
        int matrixOffset;
        int matCol;

        if (matrix == mat1) 
        { 
            matrixOffset = mat1Offset;
            matCol = matrixCols;
        } 
        else if (matrix == mat2) 
        { 
            matrixOffset = 0;
            matCol = 3;
        } 
        else 
        {
            matrixOffset = mat3Offset;
            matCol = matrixCols;
        }

        int offset = matrixOffset + (row * matCol + col) * 4;
        int pageNum = offset / pageSize;
        int offsetInPage = offset % pageSize;
    
        String reference = String.format("%s[%d][%d],%d,%d,%s",getMatrixName(matrix), row, col, pageNum, offsetInPage, operation);
        references.add(reference);
        numReferences+=1;
    }

    
    private String getMatrixName(int[][] matrix) {
        if (matrix == mat1) {
            return "M"; // Matriz de datos
        } else if (matrix == mat2) {
            return "F"; // Filtro
        } else {
            return "R"; // Matriz resultante
        }
    }

    public List<String> getReferences() {
        return references;
    }

    public int getNumReferences() {
        return numReferences;
    }

    public int getNumPages() {
        return numPages;
    }
}
