import java.util.PriorityQueue;

enum EventStateK {
    BIRTH, MONITOR, DEATH, REDIRECT;
}

class EventK implements Comparable<EventK> {
    private double time;
    private EventStateK event_state;

    public EventK(double event_time, EventStateK state) {
        time = event_time;
        event_state = state;
    }

    public double getTime() {
        return time;
    }

    public EventStateK getStatus() {
        return event_state;
    }

    public void update(PriorityQueue<EventK> scheduler, MonitorK monitor, double time) {
        Request current_request = null;
        switch (this.getStatus()) {
            case BIRTH:
                current_request = new Request(monitor.getID());
                current_request.setArrivalTime(time);
                System.out.println("R" + current_request.getId() + " ARR: " + current_request.getArrivalTime());
                if (monitor.requests_1.size() == Simulator.getK()) {
                    monitor.dropped += 1;
                    System.out.println("R" + current_request.getId() + " REDIR: " + time);
                    monitor.requests_2.add(current_request);
                    if (monitor.requests_2.size() == 1) {
                        current_request.setStartTime(time);
                        scheduler.add(new EventK(time + Simulator.getNextDeathTwo(), EventStateK.REDIRECT));
                        System.out.println("R" + current_request.getId() + " START 1: "+time);
                    }
                } else {
                    monitor.requests_1.add(current_request);
                    if (monitor.requests_1.size() == 1) {
                        current_request.setStartTime(time);
                        scheduler.add(new EventK(time + Simulator.getNextDeathOne(), EventStateK.DEATH));
                        System.out.println("R" + current_request.getId() + " START 0: " + time);
                    }
                }
                scheduler.add(new EventK(time + Simulator.getNextBirth(), EventStateK.BIRTH));
                break;

            case MONITOR:
                monitor.no_of_active_requests += 1;
                monitor.no_of_requests_1 += monitor.requests_1.size();
                monitor.no_of_requests_2 += monitor.requests_2.size();
                scheduler.add(new EventK(time + Simulator.getNextMonitor(), EventStateK.MONITOR));
                break;

            case DEATH:
                current_request = monitor.requests_1.remove();
                current_request.setFinishTime(time);
                monitor.completed_requests += 1;
                monitor.total_time += current_request.latency();
                monitor.busy_time_1 += current_request.serviceTime();
                System.out.println("R" + current_request.getId() + " DONE 0: " + time);

                if (monitor.requests_1.size() > 0) {
                    Request next_request = monitor.requests_1.peek();
                    next_request.setStartTime(time);
                    System.out.println("R" + next_request.getId() + " START 0: " + time);
                    scheduler.add(new EventK(time + Simulator.getNextDeathOne(), EventStateK.DEATH));
                }
                break;

            case REDIRECT:
                current_request = monitor.requests_2.remove();
                current_request.setFinishTime(time);
                monitor.completed_requests += 1;
                monitor.total_time += current_request.latency();
                monitor.busy_time_2 += current_request.serviceTime();
                System.out.println("R" + current_request.getId() + " DONE 1: " + time);

                if (monitor.requests_2.size() > 0) {
                    Request next_request = monitor.requests_2.peek();
                    next_request.setStartTime(time);
                    System.out.println("R" + next_request.getId() + " START 1: " + time);
                    scheduler.add(new EventK(time + Simulator.getNextDeathTwo(), EventStateK.REDIRECT));
                }
                break;

            default:
                System.out.println("This shouldn't have happened...");
        }
    }

    @Override
    public int compareTo(EventK o) {
        if (this.time > o.getTime())
            return 1;
        else if (this.time < o.getTime())
            return -1;
        else
            return 0;
    }
}