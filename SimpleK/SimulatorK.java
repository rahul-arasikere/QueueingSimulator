
class SimulatorK {

    private static double arrival_rate, service_time;
    private static int K;
    public static void simulate(double time) {
       Controller.run(time);
    }

    public static void main(String args[]) {
        double time = Double.parseDouble(args[0]);
        arrival_rate = Double.parseDouble(args[1]);
        service_time = Double.parseDouble(args[2]);
        K = Integer.parseInt(args[3]);
        simulate(time);
    }

    public static int getK() {
        return K;
    }

    public static double getNextBirth() {
		return Exp.getExp(arrival_rate);
	}

	public static double getNextDeath() {
		return Exp.getExp(1/service_time);
	}

	public static double getNextMonitor() {
		return Exp.getExp(arrival_rate);
	}

}