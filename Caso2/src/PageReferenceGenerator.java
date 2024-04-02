import java.util.ArrayList;
import java.util.List;

public class PageReferenceGenerator {
    private int[][] mat1;
    private int[][] mat2;
    private int[][] mat3;
    private int pageSize;
    private int matrixRows;
    private int matrixCols;
    private int filterSize = 3; // Tamaño del filtro (3x3)
    private int numPages;
    private List<String> references; // Lista para almacenar las referencias

    public PageReferenceGenerator(int pageSize, int matrixSize){
        this.pageSize = pageSize;
        this.matrixCols = matrixSize;
        this.matrixRows = matrixSize;

        // Calcular el número de páginas virtuales necesarias
        int totalSize = (matrixRows * matrixCols + filterSize * filterSize + matrixRows * matrixCols) * 4; // 4 bytes por entero
        this.numPages = (int) Math.ceil((double) totalSize / pageSize);

        // Inicializar las matrices
        mat1 = new int[matrixRows][matrixCols];
        mat2 = new int[filterSize][filterSize];
        mat3 = new int[matrixRows][matrixCols];

        // Inicializar la lista de referencias
        references = new ArrayList<>();
    }

    public void Filter(){
        for(int i = 1; i < matrixRows - 1; i++){
            for(int j = 0; j < matrixCols - 1; j++){
                int acum = 0;
                for(int a = -1; a <= 1; a++){
                    for(int b = -1; b <= 1; b++){
                        int i2 = i + a;
                        int j2 = j + b;
                        int i3 = 1 + a;
                        int j3 = 1 + b;
                        acum += (mat2[i3][j3]*mat1[i2][j2]);
                    }
                }
                if(acum >= 0 && acum <= 255){
                    mat3[i][j] = 0;
                } else if(acum < 0){
                    mat3[i][j] = 0;
                } else {
                    mat3[i][j] = 255;
                }
            }
        }
        for(int i = 0; i < matrixCols; i++){
            mat3[0][i] = 0;
            mat3[matrixRows-1][i] = 255;
        }
        for(int i=1; i < matrixRows; i++){
            mat3[i][0] = 0;
            mat3[i][matrixCols-1] = 255;
        }
    }
}
