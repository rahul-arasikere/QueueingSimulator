import java.util.PriorityQueue;

enum EventState {
  BIRTH, MONITOR, DONE_S0, REDIRECT_S1_1, REDIRECT_S1_2, REDIRECT_S2, REDIRECT_S3;
}

class Event implements Comparable<Event> {
  private double time;
  private EventState event_state;

  public Event(double event_time, EventState state) {
    time = event_time;
    event_state = state;
  }

  public double getTime() {
    return time;
  }

  public EventState getStatus() {
    return event_state;
  }

  public void update(PriorityQueue<Event> scheduler, Monitor monitor, double time) {
    Request current_request = null;
    switch (this.getStatus()) {
      case BIRTH:
        current_request = new Request(monitor.getID());
        current_request.setSystemArrival(time);
        current_request.setArrivalTime(time);
        monitor.requests_s0.add(current_request);
        System.out.println("R" + current_request.getId() + " ARR: " + current_request.getArrivalTime());

        if (monitor.requests_s0.size() == 1) {
          current_request.setStartTime(time);
          scheduler.add(new Event(time + Simulator.getNextDeathS0(), EventState.DONE_S0));
          System.out.println("R" + current_request.getId() + " START S0: " + time);
        }

        scheduler.add(new Event(time + Simulator.getNextBirth(), EventState.BIRTH));
        break;

      case MONITOR:
        monitor.no_of_monitor_requests += 1;
        monitor.no_of_requests_s0 += monitor.requests_s0.size();
        monitor.no_of_requests_s1 += monitor.requests_s1.size();
        if (monitor.request_s1_1 != null) {
          monitor.no_of_requests += 1;
          monitor.no_of_requests_s1 += 1;
        }
        if (monitor.request_s1_2 != null) {
          monitor.no_of_requests += 1;
          monitor.no_of_requests_s1 += 1;
        }
        monitor.no_of_requests_s2 += monitor.requests_s2.size();
        monitor.no_of_requests_s3 += monitor.requests_s3.size();
        monitor.no_of_requests += monitor.requests_s0.size() + monitor.requests_s1.size() + monitor.requests_s2.size()
            + monitor.requests_s3.size();
        scheduler.add(new Event(time + Simulator.getNextMonitor(), EventState.MONITOR));
        break;

      case DONE_S0:
        current_request = monitor.requests_s0.remove();
        current_request.setFinishTime(time);
        monitor.response_time_s0 += current_request.latency();
        monitor.util_time_s0 += current_request.serviceTime();
        monitor.no_of_completed_requests_s0 += 1;
        System.out.println("R" + current_request.getId() + " DONE S0: " + time);
        switch (Simulator.getRedirectS0()) {
          case 1:
            System.out.println("R" + current_request.getId() + " FROM S0 TO S1: " + time);
            current_request.setArrivalTime(time);
            monitor.requests_s1.add(current_request);
            if (monitor.request_s1_1 == null && monitor.request_s1_2 == null) {
              if (monitor.getNextCPU()) {
                monitor.request_s1_1 = monitor.requests_s1.remove();
                System.out.println("R" + current_request.getId() + " START S1,1: " + time);
                current_request.setStartTime(time);
                scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_1));
              } else {
                monitor.request_s1_2 = monitor.requests_s1.remove();
                System.out.println("R" + current_request.getId() + " START S1,2: " + time);
                current_request.setStartTime(time);
                scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_2));
              }
            } else if (monitor.request_s1_1 == null && monitor.requests_s1.size() > 0) {
              monitor.request_s1_1 = monitor.requests_s1.remove();
              System.out.println("R" + current_request.getId() + " START S1,1: " + time);
              current_request.setStartTime(time);
              scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_1));
            } else if (monitor.request_s1_2 == null) {
              monitor.request_s1_2 = monitor.requests_s1.remove();
              System.out.println("R" + current_request.getId() + " START S1,2: " + time);
              current_request.setStartTime(time);
              scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_2));
            }
            break;
          case 2:
            System.out.println("R" + current_request.getId() + " FROM S0 TO S2: " + time);
            current_request.setArrivalTime(time);
            if (monitor.requests_s2.size() == Simulator.getK2()) {
              monitor.dropped_s2 += 1;
              System.out.println("R" + current_request.getId() + " DROP S2: " + time);
            } else {
              monitor.requests_s2.add(current_request);
              if (monitor.requests_s2.size() == 1) {
                current_request.setStartTime(time);
                scheduler.add(new Event(time + Simulator.getNextDeathS2(), EventState.REDIRECT_S2));
                System.out.println("R" + current_request.getId() + " START S2: " + time);
              }
            }
            break;
        }
        if (monitor.requests_s0.size() > 0) {
          Request next_request = monitor.requests_s0.peek();
          next_request.setStartTime(time);
          System.out.println("R" + next_request.getId() + " START S0: " + time);
          scheduler.add(new Event(time + Simulator.getNextDeathS0(), EventState.DONE_S0));
        }
        break;

      case REDIRECT_S1_1:
        current_request = monitor.request_s1_1;
        monitor.no_of_completed_requests_s1 += 1;
        current_request.setFinishTime(time);
        monitor.response_time_s1 += current_request.latency();
        monitor.util_time_s1_1 += current_request.serviceTime();
        System.out.println("R" + current_request.getId() + " DONE S1,1: " + time);
        System.out.println("R" + current_request.getId() + " FROM S1 TO S3: " + time);
        current_request.setArrivalTime(time);
        monitor.requests_s3.add(current_request);
        if (monitor.requests_s3.size() == 1) {
          current_request.setStartTime(time);
          scheduler.add(new Event(time + Simulator.getNextDeathS3(), EventState.REDIRECT_S3));
          System.out.println("R" + current_request.getId() + " START S3:" + time);
        }
        if (monitor.requests_s1.size() > 0) {
          monitor.request_s1_1 = monitor.requests_s1.remove();
          monitor.request_s1_1.setStartTime(time);
          System.out.println("R" + monitor.request_s1_1.getId() + " START S1,1: " + time);
          scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_1));
        } else {
          monitor.request_s1_1 = null;
        }
        break;

      case REDIRECT_S1_2:
        current_request = monitor.request_s1_2;
        current_request.setFinishTime(time);
        monitor.no_of_completed_requests_s1 += 1;
        monitor.response_time_s1 += current_request.latency();
        monitor.util_time_s1_2 += current_request.serviceTime();
        System.out.println("R" + current_request.getId() + " DONE S1,2: " + time);
        System.out.println("R" + current_request.getId() + " FROM S1 TO S3: " + time);
        current_request.setArrivalTime(time);
        monitor.requests_s3.add(current_request);
        if (monitor.requests_s3.size() == 1) {
          current_request.setStartTime(time);
          scheduler.add(new Event(time + Simulator.getNextDeathS3(), EventState.REDIRECT_S3));
          System.out.println("R" + current_request.getId() + " START S3:" + time);
        }
        if (monitor.requests_s1.size() > 0) {
          monitor.request_s1_2 = monitor.requests_s1.remove();
          monitor.request_s1_2.setStartTime(time);
          System.out.println("R" + monitor.request_s1_2.getId() + " START S1,2: " + time);
          scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_2));
        } else {
          monitor.request_s1_2 = null;
        }
        break;

      case REDIRECT_S2:
        current_request = monitor.requests_s2.remove();
        monitor.no_of_completed_requests_s2 += 1;
        current_request.setFinishTime(time);
        monitor.response_time_s2 += current_request.latency();
        monitor.util_time_s2 += current_request.serviceTime();
        System.out.println("R" + current_request.getId() + " DONE S2: " + time);
        System.out.println("R" + current_request.getId() + " FROM S2 TO S3: " + time);
        current_request.setArrivalTime(time);
        monitor.requests_s3.add(current_request);
        if (monitor.requests_s3.size() == 1) {
          current_request.setStartTime(time);
          scheduler.add(new Event(time + Simulator.getNextDeathS3(), EventState.REDIRECT_S3));
          System.out.println("R" + current_request.getId() + " START S3: " + time);
        }
        if (monitor.requests_s2.size() > 0) {
          Request next_request = monitor.requests_s2.peek();
          next_request.setStartTime(time);
          System.out.println("R" + next_request.getId() + " START S2: " + time);
          scheduler.add(new Event(time + Simulator.getNextDeathS2(), EventState.REDIRECT_S2));
        }
        break;

      case REDIRECT_S3:
        current_request = monitor.requests_s3.remove();
        monitor.no_of_completed_requests_s3 += 1;
        current_request.setFinishTime(time);
        monitor.response_time_s3 += current_request.latency();
        monitor.util_time_s3 += current_request.serviceTime();
        System.out.println("R" + current_request.getId() + " DONE S3: " + time);
        switch (Simulator.getRedirectS3()) {
          case 1:
            System.out.println("R" + current_request.getId() + " FROM S3 TO S1: " + time);
            current_request.setArrivalTime(time);
            monitor.requests_s1.add(current_request);
            if (monitor.request_s1_1 == null && monitor.request_s1_2 == null) {
              if (monitor.getNextCPU()) {
                monitor.request_s1_1 = monitor.requests_s1.remove();
                System.out.println("R" + current_request.getId() + " START S1,1: " + time);
                current_request.setStartTime(time);
                scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_1));
              } else {
                monitor.request_s1_2 = monitor.requests_s1.remove();
                System.out.println("R" + current_request.getId() + " START S1,2: " + time);
                current_request.setStartTime(time);
                scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_2));
              }
            } else if (monitor.request_s1_1 == null) {
              monitor.request_s1_1 = monitor.requests_s1.remove();
              System.out.println("R" + current_request.getId() + " START S1,1: " + time);
              current_request.setStartTime(time);
              scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_1));
            } else if (monitor.request_s1_2 == null) {
              monitor.request_s1_2 = monitor.requests_s1.remove();
              System.out.println("R" + current_request.getId() + " START S1,2: " + time);
              current_request.setStartTime(time);
              scheduler.add(new Event(time + Simulator.getNextDeathS1(), EventState.REDIRECT_S1_2));
            }
            break;

          case 2:
            System.out.println("R" + current_request.getId() + " FROM S3 TO S2: " + time);
            current_request.setArrivalTime(time);
            if (monitor.requests_s2.size() == Simulator.getK2()) {
              monitor.dropped_s2 += 1;
              System.out.println("R" + current_request.getId() + " DROP S2: " + time);
            } else {
              monitor.requests_s2.add(current_request);
              if (monitor.requests_s2.size() == 1) {
                current_request.setStartTime(time);
                scheduler.add(new Event(time + Simulator.getNextDeathS2(), EventState.REDIRECT_S2));
                System.out.println("R" + current_request.getId() + " START S2: " + time);
              }
            }
            break;

          case 3:
            System.out.println("R" + current_request.getId() + " FROM S3 TO OUT: " + time);
            monitor.no_of_completed_requests += 1;
            current_request.setFinishTime(time);
            monitor.response_time += current_request.systemTime();
            break;
        }
        if (monitor.requests_s3.size() > 0) {
          Request next_request = monitor.requests_s3.peek();
          System.out.println("R" + next_request.getId() + " START S3: " + time);
          next_request.setStartTime(time);
          scheduler.add(new Event(time + Simulator.getNextDeathS3(), EventState.REDIRECT_S3));
        }
        break;

      default:
        System.out.println("This shouldn't have happened...");
    }

  }

  @Override
  public int compareTo(Event o) {
    if (this.time > o.getTime())
      return 1;
    else if (this.time < o.getTime())
      return -1;
    else
      return 0;
  }
}
