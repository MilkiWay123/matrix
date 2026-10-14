public class ComplexMatrix {

    private final int rows;
    private final int cols;
    private final Complex[][] data;

    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = new Complex(0, 0);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Complex get(int i, int j) {
        return data[i][j];
    }

    public void set(int i, int j, Complex value) {
        data[i][j] = value;
    }

    public boolean isSquare() {
        return rows == cols;
    }

    public ComplexMatrix add(ComplexMatrix other) throws MatrixException {
        if (rows != other.rows || cols != other.cols) {
            throw new MatrixException("Сложение невозможно: размеры матриц не совпадают");
        }
        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = data[i][j].add(other.data[i][j]);
            }
        }
        return result;
    }

    public ComplexMatrix multiply(ComplexMatrix other) throws MatrixException {
        if (cols != other.rows) {
            throw new MatrixException("Умножение невозможно: число столбцов первой матрицы "
                    + "не равно числу строк второй");
        }
        ComplexMatrix result = new ComplexMatrix(rows, other.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < cols; k++) {
                    sum = sum.add(data[i][k].multiply(other.data[k][j]));
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    public ComplexMatrix divide(ComplexMatrix other) throws MatrixException {
        return this.multiply(other.inverse());
    }

    public ComplexMatrix transpose() {
        ComplexMatrix result = new ComplexMatrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[j][i] = data[i][j];
            }
        }
        return result;
    }

    public Complex determinant() throws MatrixException {
        if (!isSquare()) {
            throw new MatrixException("Определитель есть только у квадратной матрицы");
        }
        int n = rows;
        Complex[][] a = copyData();
        Complex det = new Complex(1, 0);

        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int row = col + 1; row < n; row++) {
                if (a[row][col].abs() > a[pivot][col].abs()) {
                    pivot = row;
                }
            }
            if (a[pivot][col].isZero()) {
                return new Complex(0, 0);
            }
            if (pivot != col) {
                Complex[] tmp = a[pivot];
                a[pivot] = a[col];
                a[col] = tmp;
                det = det.multiply(new Complex(-1, 0));
            }
            det = det.multiply(a[col][col]);

            for (int row = col + 1; row < n; row++) {
                Complex factor = a[row][col].divide(a[col][col]);
                for (int k = col; k < n; k++) {
                    a[row][k] = a[row][k].subtract(factor.multiply(a[col][k]));
                }
            }
        }
        return det;
    }

    public ComplexMatrix inverse() throws MatrixException {
        if (!isSquare()) {
            throw new MatrixException("Обратная матрица есть только у квадратной матрицы");
        }
        int n = rows;
        Complex[][] a = copyData();
        Complex[][] inv = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = (i == j) ? new Complex(1, 0) : new Complex(0, 0);
            }
        }

        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int row = col + 1; row < n; row++) {
                if (a[row][col].abs() > a[pivot][col].abs()) {
                    pivot = row;
                }
            }
            if (a[pivot][col].isZero()) {
                throw new MatrixException("Матрица вырождена (определитель равен 0), обратной нет");
            }
            Complex[] tmp = a[pivot];
            a[pivot] = a[col];
            a[col] = tmp;
            tmp = inv[pivot];
            inv[pivot] = inv[col];
            inv[col] = tmp;

            Complex p = a[col][col];
            for (int k = 0; k < n; k++) {
                a[col][k] = a[col][k].divide(p);
                inv[col][k] = inv[col][k].divide(p);
            }

            for (int row = 0; row < n; row++) {
                if (row != col) {
                    Complex factor = a[row][col];
                    for (int k = 0; k < n; k++) {
                        a[row][k] = a[row][k].subtract(factor.multiply(a[col][k]));
                        inv[row][k] = inv[row][k].subtract(factor.multiply(inv[col][k]));
                    }
                }
            }
        }

        ComplexMatrix result = new ComplexMatrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.data[i][j] = inv[i][j];
            }
        }
        return result;
    }

    private Complex[][] copyData() {
        Complex[][] copy = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = data[i][j];
            }
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%-12s", data[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
