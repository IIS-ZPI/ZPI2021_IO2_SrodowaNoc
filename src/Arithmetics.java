public class Arithmetics implements IArithmeticsAdd, IArithmeticsDiff, IArithmeticsMult, IArithmeticsDiv {
    @Override
    public double Addition(double A, double B) {
        return A + B;
    }

    @Override
    public double Difference(double A, double B) {
        return A - B;
    }

    @Override
    public double Division(double A, double B) {
        return A / B;
    }

    @Override
    public double Multiplication(double a, double b) {
        return a * b;
    }
}
