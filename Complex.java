import java.util.Locale;

public final class Complex {

    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public Complex add(Complex other) {
        return new Complex(this.re + other.re, this.im + other.im);
    }

    public Complex subtract(Complex other) {
        return new Complex(this.re - other.re, this.im - other.im);
    }

    public Complex multiply(Complex other) {
        return new Complex(this.re * other.re - this.im * other.im,
                           this.re * other.im + this.im * other.re);
    }

    public Complex divide(Complex other) {
        double d = other.re * other.re + other.im * other.im;
        if (d == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        return new Complex((this.re * other.re + this.im * other.im) / d,
                           (this.im * other.re - this.re * other.im) / d);
    }

    public double abs() {
        return Math.sqrt(re * re + im * im);
    }

    public boolean isZero() {
        return abs() < 1e-10;
    }

    public static Complex parse(String text) {
        String s = text.replace(" ", "");
        try {
            if (!s.endsWith("i")) {
                return new Complex(Double.parseDouble(s), 0);
            }
            String body = s.substring(0, s.length() - 1);
            int pos = Math.max(body.lastIndexOf('+'), body.lastIndexOf('-'));
            if (pos <= 0) {
                return new Complex(0, coefficient(body));
            }
            double re = Double.parseDouble(body.substring(0, pos));
            double im = coefficient(body.substring(pos));
            return new Complex(re, im);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Неверное число: " + text);
        }
    }

    private static double coefficient(String s) {
        if (s.isEmpty() || s.equals("+")) {
            return 1;
        }
        if (s.equals("-")) {
            return -1;
        }
        return Double.parseDouble(s);
    }

    private static String format(double x) {
        String s = String.format(Locale.US, "%.4f", x);
        while (s.endsWith("0")) {
            s = s.substring(0, s.length() - 1);
        }
        if (s.endsWith(".")) {
            s = s.substring(0, s.length() - 1);
        }
        if (s.equals("-0")) {
            s = "0";
        }
        return s;
    }

    @Override
    public String toString() {
        String reStr = format(re);
        String imStr = format(Math.abs(im));
        if (imStr.equals("0")) {
            return reStr;
        }
        String imPart = (imStr.equals("1") ? "" : imStr) + "i";
        if (reStr.equals("0")) {
            return (im < 0 ? "-" : "") + imPart;
        }
        return reStr + (im < 0 ? "-" : "+") + imPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complex)) {
            return false;
        }
        Complex other = (Complex) o;
        return Double.compare(re, other.re) == 0 && Double.compare(im, other.im) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }
}
