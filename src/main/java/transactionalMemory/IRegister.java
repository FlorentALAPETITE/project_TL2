package transactionalMemory;

public interface ITransaction {
	public void begin();
	public void try_to_commit() throws AbortException;
	public boolean isCommited();
}