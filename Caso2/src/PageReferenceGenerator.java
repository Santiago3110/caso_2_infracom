public class PageReferenceGenerator {
    private int[][] mat1;
    private int[][] mat2;
    private int[][] mat3;
    private int pageSize;
    private int matrixRows;
    private int matrixCols;

    public PageReferenceGenerator(int pageSize, int matrixRows, int matrixCols){
        this.pageSize = pageSize;
        this.matrixCols = matrixCols;
        this.matrixRows = matrixRows;
    }

    public void Filter(){
        for(int i = 1; i < n - f; i++){
            for(int j = 0; j < nc - 1; j++){
                int acum = 0;
                for(int a = -1; a <= 1; a++){
                    for(int b = -1; b <= 1; b++){
                        int i2 = 1 + a;
                        int j2 = j + b;
                        int j3 = 1 + a;
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
        for(int i = 0; i < nc; i++){
            mat3[0][i] = 0;
            mat3[nf-1][i] = 255;
        }
        for(int i=1; i < nf; i++){
            mat3[i][0] = 0;
            mat3[i][nc-1] = 255;
        }
    }
}
