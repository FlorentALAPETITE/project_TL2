package core;

import transactionalMemory.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.InterruptedException;

public class Main {

	private static final int NBR_OF_THREADS = 10;
	private static final int NBR_OF_INCREMENTS = 1000;
	
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
   		}catch(Exception e){
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
   		System.out.println("=== End ===");


        System.out.println("=== Testing multiple write ===");
        reg = new Register<Integer>(0);
        t1 = new TL2Transaction();
        t2 = new TL2Transaction();
        t1.begin();
        t2.begin();
        try{
            reg.read(t1);
            reg.write(t2, 2);
            t2.try_to_commit();
            reg.write(t1, 6);
            t1.try_to_commit();
            System.out.println("First phase : KO...");
        }catch(AbortException e){System.out.println("First phase : OK!");}
        try{
            t1.begin();
            reg.write(t1, 2);
            reg.write(t1, 3);
            reg.write(t1, 4);
            t1.try_to_commit();
            if (reg.read(t) == 4){
                System.out.println("Second phase : OK!");           
            } else {
                System.out.println("Second phase : KO...");
            }
        }catch(AbortException e){
            System.out.println("Oops !");
        }
        System.out.println("=== End ===");


        // NOT WORKING
        System.out.println("=== Testing two transaction with two objects ===");
        reg = new Register<Integer>(0);
        Register<Integer> reg2 = new Register<Integer>(100);
        t1 = new TL2Transaction();
        t2 = new TL2Transaction();
        try{
            t1.begin();
            t2.begin();            
            reg.write(t1, 2);
            reg2.write(t2,5);
            reg.write(t2, 7);
            reg2.write(t1,9);
            reg.write(t1,10);
            t1.try_to_commit();
            t2.try_to_commit();
            System.out.println("Registre 1 : "+ reg.read(t1));
            System.out.println("Registre 2 : "+ reg2.read(t2));            
        }catch(AbortException e){
          e.printStackTrace();
        }

    }

    static void increment(IRegister<Integer> X){
    	ITransaction t = new TL2Transaction();
        while (!t.isCommited()){
            try{
		        t.begin();
            X.write(t, X.read(t) + 1);
    			t.try_to_commit();
    		}catch(AbortException e){
    			//e.printStackTrace();
    		}
    	}
	}

	private static class MyRunnable implements Runnable {
		private Register<Integer> r;

		public MyRunnable(Register<Integer> reg) {
		   this.r = reg;
		}

		public void run() {
			for(int i = 0; i<NBR_OF_INCREMENTS; i++){
                increment(r);
    		}
		}
	}
}