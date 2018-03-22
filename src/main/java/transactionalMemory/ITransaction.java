package transactionalMemory;

public interface ITransaction {
	public void begin();
	public void try_to_commit() throws AbortException;
	public boolean isCommited();
	public void addInWrittenSet(IRegister o, Object v);
	public boolean isInWrittenSet(int hashCode);
	public void addInReadSet(IRegister o);
	public void updateLocalRegisterCopy(int hashCode, Object v);
	public TL2Transaction.LocalCopy getLocalRegisterCopy(int registerHashCode);
	public Integer getClockValue();
}