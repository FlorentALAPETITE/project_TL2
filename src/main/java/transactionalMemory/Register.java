package transactionalMemory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Register<T> implements IRegister<T>{

	private T value_;

	private ReentrantLock lock_;

	private Integer date_;

	public Register(T value){
		lock_ = new ReentrantLock();
		value_= value;
		date_ = -1;
	}

	public T read(ITransaction t) throws AbortException{
		Register<T> localCopy = (Register<T>) t.getLocalRegisterCopy(this.hashCode());

		if (localCopy != null){
			return localCopy.getValue();
		}

		t.addInReadSet(this);

		localCopy = (Register<T>) t.getLocalRegisterCopy(this.hashCode());	

		if(localCopy.getDate() > t.getClockValue()){
			throw new AbortException("Abort mission");
		}
		else{
			return localCopy.value_;
		}

	}

	public void write(ITransaction t, T v) throws AbortException{
		if(lock_.isLocked()){
			throw new AbortException("Abort mission");
		}	

		t.addInWrittenSet(this);
	
		
	}

	public void acquireLock() throws AbortException{
		if(lock_.isLocked()){
			throw new AbortException("Abort mission");
		}
		lock_.lock();
	}

	public void releaseLock() throws AbortException{
		lock_.unlock();
	}

	public Integer getDate(){
		return date_;
	}

	public T getValue(){
		return value_;
	}

	public void update(T value, Integer date){
		value_ = value;
		date_ = date;
	}
}