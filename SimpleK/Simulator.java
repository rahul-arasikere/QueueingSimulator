class Simulator {

  private static double arrival_rate, service_time_1, service_time_2;
  private static int K;

  public static void simulate(double time) {
    ControllerK.run(time);
  }

  public static void main(String args[]) {
    double time = Double.parseDouble(args[0]);
    arrival_rate = Double.parseDouble(args[1]);
    service_time_1 = Double.parseDouble(args[2]);
    K = Integer.parseInt(args[3]);
    service_time_2 = Double.parseDouble(args[4]);
    simulate(time);
  }

  public static int getK() {
    return K;
  }

  public static double getNextBirth() {
    return Exp.getExp(arrival_rate);
  }

  public static double getNextDeathOne() {
    return Exp.getExp(1 / service_time_1);
  }

  public static double getNextDeathTwo() {
    return Exp.getExp(1 / service_time_2);
  }

  public static double getNextMonitor() {
    return Exp.getExp(arrival_rate);
  }

}
