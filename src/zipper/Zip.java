package zipper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.*;

/**
 * @version 1.0
 * @author Artur Biedulski, Marvin König, Oliver Heidemann
 */

/**
* Diese Klassen stellt Funktionen bereit die zum Erstellen und Hinzufügen von Ordern / Dateien zum
* Zip-Archiv notwendig sind.
*/
public class Zip {

	private ZipEntry	newzipeintrag;

	/**
	 * Diese Methode nutzt die Methoden findeUnterOrdner und sammelAlleDaten um alle übergebenen
	 * Dateien bzw. Ordner samt Unterordner und deren Inhalt mit der korrekten Hierarchie in das
	 * Zip-Archiv zu schreiben. Hierzu wurde vorher der ZipOutputStream und die einzutragenden
	 * Dateien an diese Methode übergeben.
	 * 
	 * @param zipFiles die einzutragenden Dateien
	 * @param zos der zuvor geöffnete ZipOutputStream
	 * @return check Variable, welche beim Methodenaufruf im Falle einer Exception ein "false" zurückgibt.
	 */
	public boolean auslesenUndEintragen(String[] zipFiles, ZipOutputStream zos) {

		boolean check = true;
		byte[] buffer = new byte[1024];

		try {
			// Die for-Schleife wird solange ausgeführt, wie noch Werte(Pfad der hinzuzufügenden
			// Datei) in dem zipFiles-Array vorhanden sind. Gleichzeitig werden Sie der Variablen
			// "s" zugeordnet.
			for (String zipDateien : zipFiles) {
				// Es wird ein neues File Objekt erzeugt und diesem der Pfad der hinzuzufügenden
				// Datei übergeben.
				File datei = new File(zipDateien);
				for (File file : sammleAlleDateien(datei)) {
					// Der absolute Pfad der Datei wird mithilfe der Methode "findeUnterOrdner" der
					// Variablen "newzipeintrag" zugewiesen.
					newzipeintrag = new ZipEntry(findeUnterOrdner(datei, file));
					// In dem temporären File wird mithilfe des ZipOutputStreams ein neuer Eintrag
					// mit dem eben ausgelesenen Namen ertsellt.
					zos.putNextEntry(newzipeintrag);
					// Ein Objekt der Klasse BufferedinputStream wird initalisiert. Dieses wird
					// benötigt um den Binärcode(0,1) des vorhandenen Zip-Eintrages auszulesen.
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					// Die while-Schleife wird solange ausgeführt, bis sämtliche bits ausgelesen und
					// in das temporäre File mithilfe des ZipOutputStreams geschreiben sind.
					int len = 0;
					while ((len = bis.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					bis.close();
					zos.closeEntry();
				}
			}
			zos.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.getMessage();
			System.out.println("ACHTUNG: Leerzeichen sind nicht erlaubt!");
			check = false;
		} catch (IOException exception) {
			System.err.println("Fehlermeldung: " + exception.getMessage());
			check = false;
		}
		return check;
	}

	/**
	 * Die Methode "findeUnterOrdner" ist für das Finden des absoluten Pfades der hinzuzufügenden
	 * Datei zuständig. Standardmäßig wird als Dateiname der Name der Datei genommen. Ist die Datei
	 * in einem Unterordner so wird der Dateiname um die Angabe des Unterordners erweitert. Dazu
	 * überprüfen wir, ob sich der Parent-Ordner mit dem Pfad der Datei ähnelt, ermitteln dann den
	 * letzten Index des Backslashes im Parent-Ordner und schneiden die überflüßige Angabe bei dem
	 * Pfad der Datei ab.
	 * 
	 * @param parent Vater-Ordner von "datei"
	 * @param datei im Unterordner liegende Datei
	 */
	public String findeUnterOrdner(File parent, File datei) {
		String tmp = datei.getName();
		if (datei.getAbsolutePath().contains(parent.getAbsolutePath())) {
			int index = parent.getAbsolutePath().lastIndexOf("\\");
			tmp = datei.getAbsolutePath().substring(index + 1);
		}
		return tmp;
	}

	/**
	 * Die Methode "sammelAlleDateien" gibt eine Liste aller Dateien zurück. Handelt es sich bei der
	 * Datei um einen Ordner, wird der Ordnerinhalt der Liste hinzugefügt. Hat der Ordner
	 * Unterordner, ruft sich die Methode rekursiv auf bis alle Unterordner gefunden und deren
	 * Inhalt der Liste hinzugefügt wurde.
	 * 
	 * @param file die zu überprüfende Datei/Ordner
	 * @return list liefert Liste aller Dateien in Ordern / Unterordnern
	 */
	public List<File> sammleAlleDateien(File file) {
		// enthält File-Objekte aus Ordnern und Unterordnern
		List<File> list = new ArrayList<File>();

		if (file.isDirectory()) {
			/*
			 * Iteration über den vollständigen Ordnerinhalt und Wertzuweisung der ausgelesen Datei
			 * an tmp
			 */
			for (File tmp : file.listFiles()) {
				if (tmp.isDirectory()) {
					/*
					 * Wenn tmp ein Ordner ist dann ruft sich die Methode selbst auf und liest die
					 * im Unterordner enthaltenen Dateien aus. Diese Dateien werden in einer
					 * temporären Liste gespeichert und der Liste "list" hinzugefügt.
					 */
					List<File> weitereDateien = sammleAlleDateien(tmp);
					Iterator<File> iterator = weitereDateien.iterator();
					while (iterator.hasNext()) {
						list.add(iterator.next());
					}
				} else {
					// tmp ist eine Datei und wird somit der Liste hinzugefügt
					list.add(tmp);
				}
			}
		} else {
			// tmp ist eine Datei und wird somit der Liste hinzugefügt
			list.add(file);
		}
		return list;
	}
}
