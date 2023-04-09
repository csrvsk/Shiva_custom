public class AvoidANDSymbol {
    public void testMethod() {
        int a = 5;
        int b = 7;
        int c = a & b; // // Noncompliant [[sc=17;ec=22]] {{Java-101: Avoid using the symbol "&", as it can lead to unexpected behavior.}}

        int d = a | b;
    }
}