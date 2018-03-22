package core;

import transactionalMemory.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.InterruptedException;

public class Main {

	private static final int NBR_OF_THREADS = 3;
	private static final int NBR_OF_INCREMENTS = 100;
	
    public static void main( String[] args ){

    	System.out.println("=== Testing increment for an Integer Register ===");
   		Register<Integer> reg = new Register<Integer>(0);
   		ITransaction t = new TL2Transaction();
   		try{
   			System.out.println("Value of reg : "+reg.read(t)+" expecting 0");
   		}catch(AbortException e){
   			e.printStackTrace();
   		}
   		System.out.println("Incrementing");
   		increment(reg);
   		t = new TL2Transaction();
   		try{
   			System.out.println("New value of reg : "+reg.read(t)+" expecting 1");
   		}catch(AbortException e){
   			e.printStackTrace();
   		}
   		System.out.println("=== End ===\n\n");

   		System.out.println("=== Testing for "+NBR_OF_THREADS+" threads to increment "+NBR_OF_INCREMENTS+" times ===");
   		reg = new Register<Integer>(0);
   		List<Thread> myThreads = new ArrayList<Thread>();
   		System.out.println("Initializing all threads");
   		for(int i=0; i<NBR_OF_THREADS; i++){
            myThreads.add(new Thread(new MyRunnable(reg)));
        }
        System.out.println("Launching all threads");
        for(Thread thread : myThreads){
            thread.start();
        }
        System.out.println("Waiting for all threads to finish ...");
        try{
        	for(Thread thread : myThreads){
	            thread.join();
	        }
        }catch(InterruptedException e){
        	e.printStackTrace();
        }
        t = new TL2Transaction();
        try{
        	System.out.println("All threads finished, reading value of register, expecting : "+NBR_OF_THREADS*NBR_OF_INCREMENTS+" and got : "+reg.read(t));
        	System.out.println((reg.read(t) == NBR_OF_THREADS*NBR_OF_INCREMENTS ? "Nice !" : "Well .. I guess something went wrong ..."));
   		}catch(AbortException e){
   			e.printStackTrace();
   		}
   		System.out.println("=== End ===\n\n");


   		System.out.println("=== Testing two transaction to commit at the same time ===");
   		reg = new Register<Integer>(0);
   		ITransaction t1 = new TL2Transaction();
   		ITransaction t2 = new TL2Transaction();
   		try{
   			t1.begin();
   			reg.write(t1, reg.read(t1) + 1);
   			t2.begin();
   			reg.write(t2, 2);
   			t2.try_to_commit();
            t1.try_to_commit();
   			System.out.println("Uh ... reading this is not expected, an exception should have been raised");
   		}catch(AbortException e){
   			System.out.println("This Exception was expected : ");
			e.printStackTrace();
		}
        System.out.println("valeur : "+reg.getValue());
   		System.out.println("=== End ===");
    }

    static void increment(IRegister<Integer> X){
    	ITransaction t = new TL2Transaction();
        int onatrouve = -1;
        int onalaisse = -1;
        int date = -1;
        while (!t.isCommited()){
            try{
		        t.begin();
                onatrouve = X.read(t);
                X.write(t, onatrouve + 1);
                onalaisse = X.read(t);
                date = X.getDate();
    			t.try_to_commit();
    		}catch(AbortException e){
    			//e.printStackTrace();
    		}
    	}
        System.out.println("Sucess ! on a trouvé : " + onatrouve + " on a laissé : "+onalaisse+" à la date :"+date+" thread id : "+Thread.currentThread().getId());
	}

	private static class MyRunnable implements Runnable {
		private Register<Integer> r;

		public MyRunnable(Register<Integer> reg) {
		   this.r = reg;
		}

		public void run() {
			for(int i = 0; i<NBR_OF_INCREMENTS; i++){
                increment(r);
                try{
                    Thread.sleep(10);
                } catch (InterruptedException e){}
    		}
		}
	}
}