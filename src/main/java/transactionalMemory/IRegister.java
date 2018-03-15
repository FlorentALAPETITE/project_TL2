package transactionalMemory;

import java.util.concurrent.atomic.AtomicInteger;

public interface IRegister<T> {
	public T read(ITransaction t) throws AbortException;
	public void write(ITransaction t, T v) throws AbortException;
	public void acquireLock() throws AbortException;
	public void releaseLock() throws AbortException;
	public Integer getDate();
	public void update(T value, Integer date);
	public T getValue();
}