package arsw.threads;

/**
 * Un galgo que puede correr en un carril
 *
 * @author rlopez
 *
 */
public class Galgo extends Thread {
	private int paso;
	private Carril carril;
	private RegistroLlegada regl;
	private static boolean paused = false;
	private static final Object monitor = new Object();

	public Galgo(Carril carril, String name, RegistroLlegada reg) {
		super(name);
		this.carril = carril;
		paso = 0;
		this.regl = reg;
	}

	public void corra() throws InterruptedException {
		while (paso < carril.size()) {
			// Sincronización para pausa
			synchronized (monitor) {
				while (paused) {
					monitor.wait();
				}
			}

			Thread.sleep(100);
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);

			if (paso == carril.size()) {
				carril.finish();
				synchronized (regl) {
					int ubicacion = regl.getUltimaPosicionAlcanzada();
					regl.setUltimaPosicionAlcanzada(ubicacion + 1);
					System.out.println("El galgo " + this.getName() + " llego en la posicion " + ubicacion);
					if (ubicacion == 1) {
						regl.setGanador(this.getName());
					}
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			corra();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void pauseRace() {
		paused = true;
	}

	public static void resumeRace() {
		synchronized (monitor) {
			paused = false;
			monitor.notifyAll();
		}
	}
}

