import java.util.PriorityQueue;

class ControllerK {

  public static PriorityQueue<EventK> init() {
    PriorityQueue<EventK> scheduler = new PriorityQueue<EventK>();
    scheduler.add(new EventK(Simulator.getNextBirth(), EventStateK.BIRTH));
    scheduler.add(new EventK(Simulator.getNextMonitor(), EventStateK.MONITOR));
    return scheduler;
  }

  public static void run(double time) {
    PriorityQueue<EventK> scheduler = init();
    MonitorK monitor = new MonitorK();
    double current_time = 0;
    do {
      EventK event = scheduler.remove();
      current_time = event.getTime();
      event.update(scheduler, monitor, current_time);
    } while (current_time < time);
    System.out.println("UTIL 0: " + monitor.utilityOne(time));
    System.out.println("UTIL 1: " + monitor.utilityTwo(time));
    System.out.println("QLEN 0: " + monitor.averageQueueLengthOne());
    System.out.println("QLEN 1: " + monitor.averageQueueLengthTwo());
    System.out.println("TRESP: " + monitor.averageResponseTime());
    System.out.println("REDIRECTED: " + monitor.dropped);
  }

}
