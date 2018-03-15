package core;

import transactionalMemory.*;

public class Main {
	
    public static void main( String[] args ){
   
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
