# QueueingSimulator

A simple event based Java queue simulator.

This repo has three examples of different queuing simulations.

## Running the simulations

All of them can be run with some variation of the following commands:

- Compile the project first with: `$ javac Simulator.java`
- Run the simulation with: `$ java Simulator [arg1, arg2, ...]

## Simple

A single-server queue, where the time to process a request at the server is modeled using
an exponential distribution, while the arrival process of the requests follows a Poisson distribution

## SimpleK

A dual-server queuing system. A primary server handles nominal traffic, while a secondary server
handles traffic rejected by the first server. Request service times at all the servers are modeled
using an exponential distribution, while the arrival process of the requests follows a Poisson distribution

## Complex

Simulate an entire system with multiple servers. The following is the workflow of the system. First, requests
arrive with rate λ and enter at server S0 which is a single-processor system with average service time Ts0.
From here, the request goes to either S1 or S2 with probability p0,1 and p0,2, respectively. S1 has a single
infinite queue and two processors, each with average service time Ts1. S2 has a single processor and K2 maximum
queue size (this includes the request being currently served). The processor can serve a request in Ts2 time.
Any request that completes processing at S1 or S2 always goes to S3. This is a single-processor system with
service time following a distribution whose PMF is given via 6 parameters t1, p1,t2, p2,t3, p3 where ti is the
time it takes to process a request, and pi is the probability that it will take ti time for a request to be
processed at S3. After a request completes at S3, it is released from the system with probability p3,out, it
goes back to S1 with probability p3,1, or it goes back to S2 with probability p3,2. Assume all service times
for S0 − S2, as well as inter-arrival times of requests from the outside at S0 are exponentially distributed.
