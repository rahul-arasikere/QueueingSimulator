import java.util.LinkedList;

class Monitor {
    LinkedList<Request> requests;
    private int idx;
    public double completed_requests, no_of_requests, no_of_active_requests, dropped, total_time, busy_time;

    public Monitor() {
        idx = -1;
        requests = new LinkedList<Request>();
        no_of_active_requests = 0;
        completed_requests = 0;
        no_of_requests = 0;
        total_time = 0;
        busy_time = 0;
        dropped = 0;
    }

    public int getID() {
        return ++idx;
    }

    public double utility(double run_time) {
        return busy_time / run_time;
    }

    public double averageQueueLength() {
        return no_of_requests / no_of_active_requests;
    }

    public double averageResponseTime() {
        return total_time / completed_requests;
    }

}