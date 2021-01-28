import java.util.LinkedList;

class MonitorK {
    LinkedList<Request> requests_1, requests_2;
    private int idx;
    public double completed_requests, no_of_requests_1, no_of_requests_2, no_of_active_requests, dropped, total_time, busy_time_1, busy_time_2;

    public MonitorK() {
        idx = -1;
        requests_1 = new LinkedList<Request>();
        requests_2 = new LinkedList<Request>();
        no_of_active_requests = 0;
        completed_requests = 0;
        no_of_requests_1 = 0;
        no_of_requests_2 = 0;
        total_time = 0;
        busy_time_1 = 0;
        busy_time_2 = 0;
        dropped = 0;
    }

    public int getID() {
        return ++idx;
    }

    public double utilityOne(double run_time) {
        return busy_time_1 / run_time;
    }

    public double utilityTwo(double run_time) {
        return busy_time_2 / run_time;
    }

    public double averageQueueLengthOne() {
        return no_of_requests_1 / no_of_active_requests;
    }

    public double averageQueueLengthTwo() {
        return no_of_requests_2 / no_of_active_requests;
    }

    public double averageResponseTime() {
        return total_time / completed_requests;
    }

}