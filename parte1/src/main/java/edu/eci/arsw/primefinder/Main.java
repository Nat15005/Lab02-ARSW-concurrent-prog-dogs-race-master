package edu.eci.arsw.primefinder;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		PrimeFinderThread pft1 = new PrimeFinderThread(0, 10000000);
		PrimeFinderThread pft2 = new PrimeFinderThread(10000001, 20000000);
		PrimeFinderThread pft3 = new PrimeFinderThread(20000001, 30000000);

		pft1.start();
		pft2.start();
		pft3.start();
		while(pft1.isAlive() || pft2.isAlive() || pft1.isAlive()){

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			pft1.pauseThread();
			pft2.pauseThread();
			pft3.pauseThread();

			System.out.println("Hilos pausados. Presiona ENTER para continuar...");

			// ENTER
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();

			pft1.resumeThread();
			pft2.resumeThread();
			pft3.resumeThread();
		}


		try {
			pft1.join();
			pft2.join();
			pft3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Todos los hilos han terminado.");
	}
}
