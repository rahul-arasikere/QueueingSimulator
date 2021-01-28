class Custom {

    public static int getCustomInt(int[] outcomes, double[] probabilities) {
        double rand = Math.random();
        double cdf = 0;
        double answer = 0;
        for (int i = 0; i < probabilities.length; i++) {
            cdf += probabilities[i];
            if (rand <= cdf) {
                return outcomes[i];
            }
        }
        return outcomes[outcomes.length-1]; // Make compiler happy
    }

    public static double getCustomDouble(double[] outcomes, double[] probabilities) {
        double rand = Math.random();
        double cdf = 0;
        double answer = 0;
        for (int i = 0; i < probabilities.length; i++) {
            cdf += probabilities[i];
            if (rand <= cdf) {
                return outcomes[i];
            }
        }
        return outcomes[outcomes.length-1]; // Make compiler happy
    }
}