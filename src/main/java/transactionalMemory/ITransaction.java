package transactionalMemory;

public interface ITransaction {
	public void begin();
	public void try_to_commit() throws AbortException;
	public boolean isCommited();
	public void addInWrittenSet(IRegister o);
	public void addInReadSet(IRegister o);
	public IRegister<Object> getLocalRegisterCopy(int registerHashCode);
	public Integer getClockValue();
}