public class Lab1 {
    public static double getRandom() {
        while (true) {
            double x = Math.random() * 10;
            int sign = (int) (Math.random() * 2);
            if (sign == 0) x *= -1;
            if (-9.0 <= x & x <= 4.0) return x;
        }
    }
    public static double function(long[] w, double[] x, int i, int j) {
        double xj = x[j], wi = w[i];
        if (wi == 11) {
            return Math.cos(Math.sin(Math.cbrt(xj)));
        }
        if (wi == 13 | wi == 15 | wi == 19) {
            return Math.pow(4 / Math.pow(2 * Math.asin((xj - 2.5) / 13), Math.pow(xj / (xj - 3), 2)), 3);
        }
        return 2 * (1 / 2) * Math.pow((2 / 3 - Math.cbrt(xj)) / Math.PI, 3);
    }
    public static void showMatrix(double[][] f) {
        for (var i = 0; i < 7; i++) {
            String line = "";
            for (var j = 0; j < 11; j++) {
                line += String.format("%.3f", f[i][j]);
	            line += " ";
            }
            System.out.println(line);
        } 
    }
    public static void main(String[] args) {
        long[] w = {19, 17, 15, 13, 11, 9, 7};
        double[] x = new double[11];
        double[][] f = new double[7][11];
        for (var i = 0; i < 11; i++) {
            x[i] = getRandom();
        }
        for (var i = 0; i < 7; i++) {
            for (var j = 0; j < 11; j++) {
                f[i][j] = function(w, x, i, j);
            }
        }
        showMatrix(f);
    }
}