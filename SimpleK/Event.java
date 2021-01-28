import java.util.PriorityQueue;

enum EventState {
  BIRTH, MONITOR, DEATH;
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
        current_request.setArrivalTime(time);
        System.out.println("R" + current_request.getId() + " ARR: " + current_request.getArrivalTime());
        if (monitor.requests.size() == SimulatorK.getK()) {
          monitor.dropped += 1;
          System.out.println("R" + current_request.getId() + " DROP: " + time);
        } else {
          monitor.requests.add(current_request);
          if (monitor.requests.size() == 1) {
            current_request.setStartTime(time);
            scheduler.add(new Event(time + SimulatorK.getNextDeath(), EventState.DEATH));
            System.out.println("R" + current_request.getId() + " START: " + time);
          }
        }
        scheduler.add(new Event(time + SimulatorK.getNextBirth(), EventState.BIRTH));
        break;

      case MONITOR:
        monitor.no_of_active_requests += 1;
        monitor.no_of_requests += monitor.requests.size();
        scheduler.add(new Event(time + SimulatorK.getNextMonitor(), EventState.MONITOR));
        break;

      case DEATH:
        current_request = monitor.requests.remove();
        current_request.setFinishTime(time);
        monitor.completed_requests += 1;
        monitor.total_time += current_request.latency();
        monitor.busy_time += current_request.serviceTime();
        System.out.println("R" + current_request.getId() + " DONE: " + time);

        if (monitor.requests.size() > 0) {
          Request next_request = monitor.requests.peek();
          next_request.setStartTime(time);
          System.out.println("R" + next_request.getId() + " START: " + time);
          scheduler.add(new Event(time + SimulatorK.getNextDeath(), EventState.DEATH));
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
