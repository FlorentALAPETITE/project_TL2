package core;

import transactionalMemory.*;

public class Main {
	
    public static void main( String[] args ){
    	System.out.println("=== Testing increment for an Integer Register ===");
   		Register reg = new Register<Integer>();
   		System.out.println("Value of reg : "+reg.read());
   		System.out.println("Incrementing");
   		increment(reg);
   		System.out.println("New value of reg : "+reg.read());
   		System.out.println("=== End ===");
    }

    void increment(IRegister<Integer> X){
    	while (!t.isCommited()){
    		try{
    			t.begin();
    			X.write(t, X.read(t) + 1);
    			t.try_to_commit();
    		}catch(AbortException e){
    			e.getMessage();
    		}
    	}
	}
}
