public class lab1 {
    // returns a random double from [-9.0, 4.0]
    public static double getRandomFromInterval() {
        return Math.random() * 13 - 9;
    }
    // returns the value of a compound function
    public static double function(long[] w, double[] x, int i, int j) {
        double xj = x[j], wi = w[i];
        if (wi == 11) {
            return Math.cos(Math.sin(Math.cbrt(xj)));
        }
        if (wi == 13 || wi == 15 || wi == 19) {
            return Math.pow(4.0 / Math.pow(2 * Math.asin((xj - 2.5) / 13.0), Math.pow(xj / (xj - 3), 2)), 3);
        }
        return 2 * (1.0 / 2) * Math.pow((2.0 / 3 - Math.cbrt(xj)) / Math.PI, 3);
    }
    // prints the final matrix
    public static void showMatrix(double[][] f) {
        for (var i = 0; i < 7; i++) {
            String line = "";
            for (var j = 0; j < 11; j++) {
                line += String.format("%8.3f", f[i][j]);
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
            x[i] = getRandomFromInterval();
        }
        for (var i = 0; i < 7; i++) {
            for (var j = 0; j < 11; j++) {
                f[i][j] = function(w, x, i, j);
            }
        }
        showMatrix(f);
    }
}