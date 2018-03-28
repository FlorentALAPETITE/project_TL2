package transactionalMemory;

import java.util.concurrent.atomic.AtomicInteger;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class TL2Transaction implements ITransaction {

	class LocalCopy {
		Object value;
		final Integer date;

		LocalCopy(Object val, Integer date){
			this.date = date;
			this.value = val;
		}

		public Object getValue(){return value;}
		public Integer getDate() {return date;}
	}

	private static AtomicInteger CLOCK_ = new AtomicInteger(0);
	private Integer birthDate_;
	private Integer commitDate_;
	private Set<IRegister> lrst_; // ensemble des variables lues
	private Set<IRegister> lwst_; // ensemble des variables écrite
	private Map<Integer, LocalCopy> lcx_; // ensemble des variables: leur valeur locale

	public TL2Transaction(){
		lrst_ = new HashSet<IRegister>();
		lwst_ = new HashSet<IRegister>();
		lcx_ = new HashMap<Integer, LocalCopy>();
	}

	public void begin(){
		birthDate_ = CLOCK_.get();
		lrst_.clear();
		lwst_.clear();
		lcx_.clear();
	}

	public void try_to_commit() throws AbortException {
		SortedSet<IRegister> allObjects = new TreeSet(lrst_);
		allObjects.addAll(this.lwst_);
		// lock all the objects in lrst, lwst
		for (IRegister o : allObjects){
			try {
				o.acquireLock(this.hashCode()); // throws AbortException
			} catch (AbortException e) {
				for (IRegister ir: allObjects){
					try{ir.releaseLock(this.hashCode());} catch (AbortException ex) {}
				}
				throw e;
			}
		}
		for (IRegister varRead : this.lrst_){
			if (varRead.getDate() >= this.birthDate_){
				for (IRegister o: allObjects){
					o.releaseLock(this.hashCode());
				}
				throw new AbortException("Transaction outdated");
			}
		}

		this.commitDate_ = CLOCK_.getAndIncrement();
		for (IRegister varWritten: this.lwst_){
			varWritten.update(lcx_.get(varWritten.hashCode()).getValue(), this.commitDate_);
		}

		// release all locks
		for (IRegister o : allObjects){
			o.releaseLock(this.hashCode());
		}

	}

	public boolean isCommited(){
		return this.commitDate_ != null;
	}

	// inside package, add and get variables read and written
	public void addInWrittenSet(IRegister o, Object v){
		this.lwst_.add(o);
		if (lcx_.containsKey(o.hashCode())){
			updateLocalRegisterCopy(o.hashCode(), v);
		} else {
			this.lcx_.put(o.hashCode(), new LocalCopy(v, o.getDate()));
		}
	}
	public void addInReadSet(IRegister o){
		this.lrst_.add(o);
		if (!lcx_.containsKey(o.hashCode())){
			this.lcx_.put(o.hashCode(), new LocalCopy(o.getValue(), o.getDate()));
		}
	}
	public void updateLocalRegisterCopy(int hashCode, Object v){
		this.lcx_.get(hashCode).value = v;
	}
	public LocalCopy getLocalRegisterCopy(int hashCode){
		return this.lcx_.get(hashCode);
	}

	// getters
	public Integer getClockValue(){
		return CLOCK_.get();
	}
	public boolean isInWrittenSet(int hashCode){
		return this.lwst_.contains(hashCode);
	}
}