package edu.eci.arsw.primefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrimeFinderThread extends Thread {

	private final int a, b;
	private final List<Integer> primesFound;
	private boolean paused = false;
	private final Lock lock = new ReentrantLock();

	public PrimeFinderThread(int a, int b) {
		this.a = a;
		this.b = b;
		this.primesFound = new ArrayList<>();
	}

	@Override
	public void run() {
		for (int i = a; i <= b; i++) {
			lock.lock();
			try {
				synchronized (this) {
					while (paused) {
						wait();
					}
				}
				if (isPrime(i)) {
					primesFound.add(i);
					System.out.println(i);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	public void pauseThread() {
		synchronized (this) {
			paused = true;
		}
	}

	public void resumeThread() {
		synchronized (this) {
			paused = false;
			notify();
		}
	}

	public List<Integer> getPrimesFound() {
		return primesFound;
	}

	private boolean isPrime(int n) {
		if (n <= 1) return false;
		if (n == 2 || n == 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;
		for (int i = 5; i * i <= n; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0) return false;
		}
		return true;
	}
}
