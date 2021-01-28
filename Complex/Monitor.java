import java.util.LinkedList;

class Monitor {
    LinkedList<Request> requests_s0, requests_s1, requests_s2, requests_s3;
    Request request_s1_1, request_s1_2;
    private int idx;
    private boolean cpu;
    double util_time_s0, util_time_s1_1, util_time_s1_2, util_time_s2, util_time_s3, no_of_requests, no_of_requests_s0,
            no_of_requests_s1, no_of_requests_s2, no_of_requests_s3, no_of_monitor_requests, response_time,
            response_time_s0, response_time_s1, response_time_s2, response_time_s3, dropped_s2,
            no_of_completed_requests, no_of_completed_requests_s0, no_of_completed_requests_s1,
            no_of_completed_requests_s2, no_of_completed_requests_s3;

    public Monitor() {
        idx = -1;
        cpu = false;
        request_s1_1 = null;
        request_s1_2 = null;
        requests_s0 = new LinkedList<Request>();
        requests_s1 = new LinkedList<Request>();
        requests_s2 = new LinkedList<Request>();
        requests_s3 = new LinkedList<Request>();
        util_time_s0 = 0;
        util_time_s1_1 = 0;
        util_time_s1_2 = 0;
        util_time_s2 = 0;
        util_time_s3 = 0;
        no_of_monitor_requests = 0;
        no_of_requests = 0;
        no_of_requests_s0 = 0;
        no_of_requests_s1 = 0;
        no_of_requests_s2 = 0;
        no_of_requests_s3 = 0;
        response_time = 0;
        response_time_s0 = 0;
        response_time_s1 = 0;
        response_time_s2 = 0;
        response_time_s3 = 0;
        dropped_s2 = 0;
        no_of_completed_requests = 0;
        no_of_completed_requests_s0 = 0;
        no_of_completed_requests_s1 = 0;
        no_of_completed_requests_s2 = 0;
        no_of_completed_requests_s3 = 0;
    }

    public int getID() {
        return ++idx;
    }

    public double getUtilS0(double time) {
        return util_time_s0 / time;
    }

    public double getUtilS1_1(double time) {
        return util_time_s1_1 / time;
    }

    public double getUtilS1_2(double time) {
        return util_time_s1_2 / time;
    }

    public double getUtilS2(double time) {
        return util_time_s2 / time;
    }

    public double getUtilS3(double time) {
        return util_time_s3 / time;
    }

    public double averageResponseTimeS0() {
        return response_time_s0 / no_of_completed_requests_s0;
    }

    public double averageResponseTimeS1() {
        return response_time_s1 / no_of_completed_requests_s1;
    }

    public double averageResponseTimeS2() {
        return response_time_s2 / no_of_completed_requests_s2;
    }

    public double averageResponseTimeS3() {
        return response_time_s3 / no_of_completed_requests_s3;
    }

    public double averageResponseTime() {
        return response_time / no_of_completed_requests;
    }

    public double averageQueueLengthS0() {
        return no_of_requests_s0 / no_of_monitor_requests;
    }

    public double averageQueueLengthS1() {
        return no_of_requests_s1 / no_of_monitor_requests;
    }

    public double averageQueueLengthS2() {
        return no_of_requests_s2 / no_of_monitor_requests;
    }

    public double averageQueueLengthS3() {
        return no_of_requests_s3 / no_of_monitor_requests;
    }

    public double averageQueueLength() {
        return no_of_requests / no_of_monitor_requests;
    }

    public boolean getNextCPU() {
        cpu = !cpu;
        return cpu;
    }
}