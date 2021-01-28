class Exp {
  public static double getExp(double lambda) {
    double value = -Math.log(Math.random()) / lambda; // -ln(U)/lambda
    return value;
  }
}
