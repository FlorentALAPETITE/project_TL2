package core;

import transactionalMemory.*;

public class ChainedList{
  private Register<Integer> value;
  private Register<ChainedList> next;

  public ChainedList(){
    value = new Register<Integer>(0);
    next = new Register<ChainedList>(null);
  }

  public void setValue(int v){
    ITransaction t = new TL2Transaction();
    while (!t.isCommited()){
      try{
        t.begin();
        value.write(t, v);
        t.try_to_commit();
        System.out.println("J'ai mis "+v);
      }catch(AbortException e){
        //e.printStackTrace();
      }
    } 
  }

  public void setNext(ChainedList l){
    ITransaction t = new TL2Transaction();
    while (!t.isCommited()){
      try{
        t.begin();
        next.write(t, l);
        t.try_to_commit();        
      }catch(AbortException e){
        //e.printStackTrace();
      }
    }
  }

  public int getValue(){
    ITransaction t = new TL2Transaction();
    try{
      return value.read(t);
    }catch(AbortException e){
      e.printStackTrace();
      return -1;
    }
  }

  public ChainedList getNext(){
    ITransaction t = new TL2Transaction();
    try{
      return next.read(t);
    }catch(AbortException e){
      e.printStackTrace();
      return null;
    }
  }   
}