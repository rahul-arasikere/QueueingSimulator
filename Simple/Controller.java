import java.util.PriorityQueue;

class Controller {

    public static PriorityQueue<Event> init() {
        PriorityQueue<Event> scheduler = new PriorityQueue<Event>();
        scheduler.add(new Event(Simulator.getNextBirth(), EventState.BIRTH));
        scheduler.add(new Event(Simulator.getNextMonitor(), EventState.MONITOR));
        return scheduler;
    }

    public static void run(double time) {
        PriorityQueue<Event> scheduler = init();
        Monitor monitor = new Monitor();
        double current_time = 0;
        do {
            Event event = scheduler.remove();
            current_time = event.getTime();
            event.update(scheduler, monitor, current_time);
        } while (current_time < time);
        System.out.println("UTIL: " + monitor.utility(time));
        System.out.println("QLEN: " + monitor.averageQueueLength());
        System.out.println("TRESP: " + monitor.averageResponseTime());
    }

}