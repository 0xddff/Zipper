package zipper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @version 1.0
 * @author Oliver Heidemann
 * 
 */

/**
 * Mithilfe dieser Klasse wird ein Objekt erzeugt, welches ein Zip-Archiv entpackt.
 */
public class Unzip {

	/**
	 * Parametrisierter Konstruktor, in welchem die Zip-Datei und der Entpackungspfad initialisiert
	 * werden.
	 * 
	 * @param zipName Pfadangabe zum zu entpackenden Zip-Archiv
	 * @param destinationDir Pfadangabe zum Entpackungsort
	 */
	public Unzip(String zipName, String destinationDir) {
		zipArchiv = new File(zipName);
		zipDestination = new File(destinationDir);
	}

	/**
	 * In dieser Methode ist der Entpackungsprozess enthalten. Zuerst wird, falls der Entpackungsort
	 * noch nicht existiert und der Entpackungsort eine gültige Pfadangabe ist, der Ordner angelegt.
	 * Ist die Pfadangabe ungültig führt dies zum Programmabbruch. Danach wird über den Zip-Archiv
	 * Inhalt iteriert und geguckt, ob das derzeitige Element ein Ordner ist und ob der Ordner noch
	 * nicht existiert. Existiert er nicht so wird der Ordner angelegt. Ist das derzeitige Element
	 * eine Datei so wird diese entpackt.
	 * 
	 * @return check Beinhaltet, ob das Entpacken erfolgreich war
	 */
	public boolean extractZIPArchiv() {
		boolean check = true;

		/*
		 * Wenn der Entpackungsordner nicht existiert dann erstelle ihn check = true Konnte der
		 * Ordner nicht erstellt werden check = false
		 */
		if (!zipDestination.exists())
			check = zipDestination.mkdir();

		if (check) {
			try {
				ZipFile zipFile = new ZipFile(zipArchiv);
                                //Buffer definieren
				byte[] buffer = new byte[1024];
				Enumeration<? extends ZipEntry> entries = zipFile.entries();

				System.out.println("Entpacke nach: " + zipDestination);

				while (entries.hasMoreElements()) {
					ZipEntry zipEntry = entries.nextElement();

					/*
					 * Ist der ZipEntry ein Ordner und dieser Ordner existiert noch nicht, dann
					 * erstelle ihn.
					 */
					if (zipEntry.isDirectory()) {
						File dir = new File(zipDestination, zipEntry.getName());
						if (!dir.exists())
							dir.mkdir();
					} else {
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(zipDestination, zipEntry.getName())));
						BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));

						// Die while-Schleife wird solange ausgeführt, bis sämtliche bits ausgelesen sind.
						int len = 0;
						while ((len = bis.read(buffer)) > 0)
							// schreibt "len" Bytes aus dem Buffer "buffer"
							bos.write(buffer, 0, len);

						bos.close();
						bis.close();
					}
					// Ist zipEntry ein Ordner dann gib "Erstelle Ordner" aus, ansonsten rücke die
					// Ausgabe ein.
					System.out.printf("%s%-54s\n", zipEntry.isDirectory() ? "Erstelle Ordner: " : "\t", zipEntry.getName());
				}
				zipFile.close();
			} catch (Exception e) {
				System.err.println("Fehlermeldung: " + e.getMessage());
				check = false;
			}
		}
		return check;
	}

	private File	zipArchiv;
	private File	zipDestination;
}
