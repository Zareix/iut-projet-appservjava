package emprunt;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * A la connexion d'un abonné : lance le service d'emprunt pour ce dernier
 * 
 * @see ServiceEmprunt
 */
public class ServeurEmprunt implements Runnable {
	private ServerSocket socketServ;

	public ServeurEmprunt(int port) throws IOException {
		this.socketServ = new ServerSocket(port);
	}

	@Override
	public void run() {
		try {
			System.err.println("Lancement du serveur d'emprunt au port " + this.socketServ.getLocalPort());
			while (true)
				new Thread(new ServiceEmprunt(socketServ.accept())).start();
		} catch (IOException e) {
			try {
				this.socketServ.close();
			} catch (IOException e1) {
			}
			System.err.println("Arrêt du serveur au port " + this.socketServ.getLocalPort());
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.socketServ.close();
		} catch (IOException e1) {
		}
	}
}
