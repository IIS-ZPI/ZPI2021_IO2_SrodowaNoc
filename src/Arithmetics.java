public class Arithmetics implements IArithmeticsAdd, IArithmeticsDiff, IArithmeticsMult, IArithmeticsDiv {
    @Override
    public double Addition(double A, double B) {
        return A + B;
    }
    //powinno być z małych, ale to nie mój problem ¯\_(ツ)_/¯
    @Override
    public double Difference(double A, double B) {
        return A - B;
    }

    @Override
    public double Division(double A, double B) {
        if (B == 0) {
            System.out.println("Nie można dzilić przez 0");
            throw new IllegalArgumentException();
        }
        return A / B;
    }

    @Override
    public double Multiplication(double a, double b) {
        return a * b;
    }
}
