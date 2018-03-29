package core;

import transactionalMemory.*;

public class AwesomeObject{
	private Register<ChainedList> root;

	public AwesomeObject(){
		root = new Register<ChainedList>(new ChainedList());
	}

	public ChainedList getListRoot(){
		ITransaction t = new TL2Transaction();
	    try{
	      return root.read(t);
	    }catch(AbortException e){
	      e.printStackTrace();
	      return null;
	    }
	}
}