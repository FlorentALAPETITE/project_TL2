package transactionalMemory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.Comparable;

public class Register<T> implements IRegister<T>, Comparable<IRegister>{

	private T value_;

	private ReentrantLock lock_;

	private volatile Integer date_;

	public Register(T value){
		lock_ = new ReentrantLock(true);
		value_= value;
		date_ = -1;
	}

	public T read(ITransaction t) throws AbortException{
		if(lock_.isLocked()){
			throw new AbortException("Abort mission");
		}
		TL2Transaction.LocalCopy localCopy = t.getLocalRegisterCopy(this.hashCode());

		if (localCopy != null){
			return (T) localCopy.getValue();
		}

		t.addInReadSet(this);

		localCopy = t.getLocalRegisterCopy(this.hashCode());	

		if(localCopy.getDate() >= t.getBirthDate()){
			throw new AbortException("Abort mission");
		}
		else{
			return (T) localCopy.getValue();
		}

	}

	public void write(ITransaction t, T v) throws AbortException{
		if (!t.isInWrittenSet(this.hashCode())){
			t.addInWrittenSet(this,v);
		}
	}

	public void acquireLock(int hashCode) throws AbortException{
		if(lock_.isLocked()){
			throw new AbortException("Abort mission");
		}
		lock_.lock();
	}

	public void releaseLock(int hashCode) throws AbortException{		
		if (lock_.isHeldByCurrentThread()){
			lock_.unlock();
		} else {
			throw new AbortException("Well tried");
		}
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

	// Comparable interface -> usefull to TreeSet
	public int compareTo(IRegister o){
		if (o==null){throw new NullPointerException();}
		return this.hashCode() - o.hashCode();
	}
}