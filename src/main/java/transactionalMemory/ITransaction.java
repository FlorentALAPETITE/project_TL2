package transactionalMemory;

public interface IRegister<T> {
	public T read(ITransaction t) throws AbortException;
	public void write(ITransaction t, T v) throws AbortException;
}