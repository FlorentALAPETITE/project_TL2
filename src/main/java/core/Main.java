package core;

import transactionalMemory.*;

public class Main {

	private static final int NBR_OF_THREADS = 3;
	private static final int NBR_OF_INCREMENTS = 10;
	
    public static void main( String[] args ){

 //    	System.out.println("=== Testing increment for an Integer Register ===");
 //   		Register<Integer> reg = new Register<Integer>();
 //   		System.out.println("Value of reg : "+reg.read()+" expecting 0");
 //   		System.out.println("Incrementing");
 //   		increment(reg);
 //   		System.out.println("New value of reg : "+reg.read()+" expecting 1");
 //   		System.out.println("=== End ===\n\n");


 //   		System.out.println("=== Testing for "+NBR_OF_THREADS+" threads to increment "+NBR_OF_INCREMENTS+" times ===");
 //   		reg = new Register<Integer>();
 //   		List<Thread> myThreads = new ArrayList<Thread>();
 //   		System.out.println("Initializing all threads");
 //   		for(int i=0; i<NBR_OF_THREADS; i++){
 //            myThreads.add(new Thread(new MyRunnable(reg)));
 //        }
 //        System.out.println("Launching all threads");
 //        for(Thread thread : myThreads){
 //            thread.start();
 //        }
 //        System.out.println("Waiting for all threads to finish ...");
 //        try{
 //        	for(Thread thread : myThreads){
	//             thread.join();
	//         }
 //        }catch(InterruptedException e){
 //        	e.printStacktrace();
 //        }
 //        System.out.println("All threads finished, reading value of register, expecting : "+NBR_OF_THREADS*NBR_OF_INCREMENTS+" and got : "+reg.read());
 //        System.out.println((reg.read() == NBR_OF_THREADS*NBR_OF_INCREMENTS ? "Nice !" : "Well .. I guess something went wrong ..."));
 //   		System.out.println("=== End ===\n\n");


 //   		System.out.println("=== Testing two transaction to commit at the same time ===");
 //   		reg = new Register<Integer>();
 //   		Transaction t1 = new Transaction();
 //   		Transaction t2 = new Transaction();
 //   		try{
 //   			t1.begin();
 //   			reg.write(t1, 1);
 //   			t2.begin();
 //   			reg.write(t2, 2);
 //   			t1.try_to_commit();
 //   			t2.try_to_commit();
 //   			System.out.println("Uh ... reading this is not expected, an exception should have been raised");
 //   		}catch(AbortException e){
 //   			System.out.println("This Exception was expected : ");
	// 		e.getMessage();
	// 	}
 //   		System.out.println("=== End ===");
 //    }

 //    void increment(IRegister<Integer> X){
 //    	Transaction t = new Transaction();
 //    	while (!t.isCommited()){
 //    		try{
 //    			t.begin();
 //    			X.write(t, X.read(t) + 1);
 //    			t.try_to_commit();
 //    		}catch(AbortException e){
 //    			e.printStacktrace();
 //    		}
 //    	}
	// }

	// private class MyRunnable implements Runnable {
	// 	private Register r;

	// 	public MyRunnable(Register reg) {
	// 	   this.r = reg;
	// 	}

	// 	public void run() {
	// 		for(int i = 0; i>NBR_OF_INCREMENTS; i++){
	// 			increment(r);
	// 		}
	// 	}
	}
}