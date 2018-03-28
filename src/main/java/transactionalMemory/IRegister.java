package transactionalMemory;

import java.util.concurrent.atomic.AtomicInteger;

public interface IRegister<T> {
	public T read(ITransaction t) throws AbortException;
	public void write(ITransaction t, T v) throws AbortException;
	public void acquireLock(int hashCode) throws AbortException;
	public void releaseLock(int hashCode) throws AbortException;
	public Integer getDate();
	public void update(T value, Integer date);
	public T getValue();
}