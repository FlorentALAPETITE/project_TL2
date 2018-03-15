package transactionalMemory;

import java.util.concurrent.atomic.AtomicInteger;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

class TL2Transaction implements ITransaction {

	private static AtomicInteger CLOCK_ = new AtomicInteger(0);
	private Integer birthDate_;
	private Integer commitDate_;
	private Set<IRegister> lrst_; // ensemble des variables lues
	private Set<IRegister> lwst_; // ensemble des variables écrite
	private Map<Integer, IRegister> lcx_; // ensemble des variables: leur valeur locale

	public TL2Transaction(){
		lrst_ = new HashSet<IRegister>();
		lwst_ = new HashSet<IRegister>();
		lcx_ = new HashMap<Integer, IRegister>();
	}

	public void begin(){
		birthDate_ = CLOCK_.get();
	}

	public void try_to_commit() throws AbortException {
		Set<IRegister> allObjects = new HashSet(lrst_);
		allObjects.addAll(this.lwst_);
		// lock all the objects in lrst, lwst
		for (IRegister o: allObjects){
			o.acquireLock(); // throws AbortException
		}

		for (IRegister varRead: this.lrst_){
			if (varRead.getDate() > this.birthDate_){
				for (IRegister o: allObjects){
					o.releaseLock();
				}
				throw new AbortException("Transaction outdated");
			}
		}

		this.commitDate_ = CLOCK_.getAndIncrement();


		for (IRegister varWritten: this.lwst_){
			varWritten.update(lcx_.get(varWritten.hashCode()).getValue(), this.commitDate_);
		}

		// release all locks
		for (IRegister o: allObjects){
			o.releaseLock();
		}

	}

	public boolean isCommited(){
		return this.commitDate_ != null;
	}

	// inside package, add and get variables read and written
	public void addInWrittenSet(IRegister o){
		this.lwst_.add(o);
		this.lcx_.put(o.hashCode(), o);
	}
	public void addInReadSet(IRegister o){
		this.lrst_.add(o);
		this.lcx_.put(o.hashCode(), o);
	}
	public IRegister getLocalRegisterCopy(int hashCode){
		return this.lcx_.get(hashCode);
	}

	// getters
	public Integer getClockValue(){
		return CLOCK_.get();
	}
}