
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ParallelStream {
	
	static ExecutorService executorService = Executors.newFixedThreadPool(100);

	public static void main(String[] args) {
		
		long startTime = System.nanoTime();

	//	simulateRequests(() -> methodToTimeSequentially(ParallelStream::longRunningNetworkCall), 50);
	//	simulateRequests(() -> methodToTimeInCommonForkJoinPool(ParallelStream::longRunningNetworkCall), 50);
		//simulateRequests(() -> methodToTimeWithCustomPool(ParallelStream::longRunningNetworkCall), 50);
	//	simulateRequests(() -> methodToTimeWithCustomPool100Threads(ParallelStream::longRunningNetworkCall), 50);

		
	 // simulateRequests(() -> methodToTimeSequentially(ParallelStream::longRunningComptation), 50);
	  simulateRequests(() -> methodToTimeInCommonForkJoinPool(ParallelStream::longRunningComptation), 50);
		//simulateRequests(() -> methodToTimeWithCustomPool(ParallelStream::longRunningComptation), 50);
		//simulateRequests(() -> methodToTimeWithCustomPool100Threads(ParallelStream::longRunningComptation), 50);


		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		System.out.println(duration / 1000000);
	}

	private static void simulateRequests(Runnable task, int numberOfUsers) {
		List<Future> responses = IntStream.rangeClosed(1, numberOfUsers).mapToObj(e -> submitTask(task))
				.collect(Collectors.toList());

		for (Future response : responses) {
			while (!response.isDone()) {
			}
		}

	}

	private static Future<?> submitTask(Runnable task) {
		return executorService.submit(task);
	}
	

	private static void methodToTimeSequentially(IntConsumer consumer) {
		IntStream.rangeClosed(1, 25).forEach(consumer);
	}

	private static void methodToTimeInCommonForkJoinPool(IntConsumer task) {
		IntStream.rangeClosed(1, 25).parallel().forEach(task);
	}

	private static void methodToTimeWithCustomPool(IntConsumer task) {

		ForkJoinPool customThreadPool = new ForkJoinPool(7);
		try {
			customThreadPool.submit(() -> IntStream.rangeClosed(1, 25).parallel().forEach(task)).get();
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("error happened");
		}
	}

	private static void methodToTimeWithCustomPool100Threads(IntConsumer task) {

		  ForkJoinPool customThreadPool = new ForkJoinPool(100);
		try {
			customThreadPool.submit(() -> IntStream.rangeClosed(1, 25).parallel().forEach(task)).get();
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("error happened");
		}
	}

	
	private static void longRunningNetworkCall(int id) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {

		}
	}

	private static void longRunningComptation(int id) {
		IntStream.rangeClosed(1, 999999).sum();
	}

}
