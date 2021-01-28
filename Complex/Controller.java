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
    System.out.println("S0 UTIL: " + monitor.getUtilS0(time));
    System.out.println("S0 QLEN: " + monitor.averageQueueLengthS0());
    System.out.println("S0 TRESP: " + monitor.averageResponseTimeS0());

    System.out.println("S1,1 UTIL: " + monitor.getUtilS1_1(time));
    System.out.println("S1,2 UTIL: " + monitor.getUtilS1_2(time));
    System.out.println("S1 QLEN: " + monitor.averageQueueLengthS1());
    System.out.println("S1 TRESP: " + monitor.averageResponseTimeS1());

    System.out.println("S2 UTIL: " + monitor.getUtilS2(time));
    System.out.println("S2 QLEN: " + monitor.averageQueueLengthS2());
    System.out.println("S2 TRESP: " + monitor.averageResponseTimeS2());
    System.out.println("S2 DROPPED: " + monitor.dropped_s2);

    System.out.println("S3 UTIL: " + monitor.getUtilS3(time));
    System.out.println("S3 QLEN: " + monitor.averageQueueLengthS3());
    System.out.println("S3 TRESP: " + monitor.averageResponseTimeS3());

    System.out.println("QTOT: " + monitor.averageQueueLength());
    System.out.println("TRESP: " + monitor.averageResponseTime());
  }

}
