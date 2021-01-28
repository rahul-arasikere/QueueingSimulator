class Simulator {

  private static double arrival_rate, service_time_s0, service_time_s1, service_time_s2, routing_probability_3_out,
      routing_probability_3_1, routing_probability_3_2;
  private static int K2;
  private static int[] routing_outcome_0 = { 1, 2 }, routing_outcome_3 = { 1, 2, 3 };
  private static double[] probabilities, service_times, routing_probability_0, routing_probability_3;

  public static void simulate(double time) {
    Controller.run(time);
  }

  public static void main(String args[]) {
    probabilities = new double[3];
    service_times = new double[3];
    routing_probability_0 = new double[2];
    routing_probability_3 = new double[3];
    double time = Double.parseDouble(args[0]);
    arrival_rate = Double.parseDouble(args[1]);
    service_time_s0 = Double.parseDouble(args[2]);
    service_time_s1 = Double.parseDouble(args[3]);
    service_time_s2 = Double.parseDouble(args[4]);
    service_times[0] = Double.parseDouble(args[5]);
    probabilities[0] = Double.parseDouble(args[6]);
    service_times[1] = Double.parseDouble(args[7]);
    probabilities[1] = Double.parseDouble(args[8]);
    service_times[2] = Double.parseDouble(args[9]);
    probabilities[2] = Double.parseDouble(args[10]);
    K2 = Integer.parseInt(args[11]);
    routing_probability_0[0] = Double.parseDouble(args[12]);
    routing_probability_0[1] = Double.parseDouble(args[13]);
    routing_probability_3[2] = Double.parseDouble(args[14]);
    routing_probability_3[0] = Double.parseDouble(args[15]);
    routing_probability_3[1] = Double.parseDouble(args[16]);
    simulate(time);
  }

  public static int getK2() {
    return K2;
  }

  public static double getNextBirth() {
    return Exp.getExp(arrival_rate);
  }

  public static double getNextDeathS0() {
    return Exp.getExp(1 / service_time_s0);
  }

  public static double getNextDeathS1() {
    return Exp.getExp(1 / service_time_s1);
  }

  public static double getNextDeathS2() {
    return Exp.getExp(1 / service_time_s2);
  }

  public static double getNextDeathS3() {
    return Custom.getCustomDouble(service_times, probabilities);
  }

  public static int getRedirectS0() {
    return Custom.getCustomInt(routing_outcome_0, routing_probability_0);
  }

  public static int getRedirectS3() {
    return Custom.getCustomInt(routing_outcome_3, routing_probability_3);
  }

  public static double getNextMonitor() {
    return Exp.getExp(arrival_rate);
  }

}