# ParallelStreams

This code is to benchmark user requests that need computational-heavy or I/O tasks running on sequential streams vs parallel stream.  
1- code run on a sequential stream  
2- code run on a parallel stream using common fork/join pool. 
3- code run on parallel stream using custom pool same size as the common pool for each user requests. 
4- code run on parallel stream using custom pool of 100 thread for each user request. 
