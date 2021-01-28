class Request {
  private int idx;
  private double system_entry_time, arrival_time, start_time, finish_time;

  public Request(int id) {
    idx = id;
  }

  public double latency() {
    return finish_time - arrival_time;
  }

  public double serviceTime() {
    return finish_time - start_time;
  }

  public double systemTime() {
    return finish_time - system_entry_time;
  }

  public int getId() {
    return idx;
  }

  public void setSystemArrival(double time) {
    system_entry_time = time;
  }

  public void setArrivalTime(double time) {
    arrival_time = time;
  }

  public void setStartTime(double time) {
    start_time = time;
  }

  public void setFinishTime(double time) {
    finish_time = time;
  }

  public double getArrivalTime() {
    return arrival_time;
  }

  public double getFinishTime() {
    return finish_time;
  }

  public double getStartTime() {
    return start_time;
  }
}
