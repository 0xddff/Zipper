package zipper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @version 1.0
 * @author Marvin König
 */

/**
 * Mithilfe dieser Klasse wird ein Objekt erzeugt, welches Dateien zum Zip-Archiv hinzufügt.
 * Des Weiteren erbt diese Klasse von Zip.
 */
public class Add extends Zip {

	private File		zipFile;
	private String[]	zipFiles;
	private ZipEntry	newzipeintrag;

	/**
	 * Parametrisierter Konstruktor, in welcher die Zip-Datei und die Dateien die zum Archiv
	 * hinzugefügt werden sollen initialisiert werden. Bei der Objekt Erzeugung in der Start-Klasse
	 * werden folgende Parameter übergeben.
	 * 
	 * @param zipFile String, welcher den Pfad des Zip-Archives enthält.
	 * @param zipFiles String-Array, welches sämtliche Pfade der Dateien enthält, die dem Archiv hinzugefügt werden sollen
	 */
	public Add(String zipFile, String[] zipFiles) {
		this.zipFile = new File(zipFile);
		this.zipFiles = zipFiles;
	}

	/**
	 * Die Methode "addFileToArchive" ist für das hinzufügen der einzelnen Dateien zum Zip-Archiv
	 * verantwortlich. Der Methodenaufruf erfolgt in der Start-Klasse. 
         * Funktionsprinzip: Zunächst wird ein temporäres File erstellt. Aus der vorhandenen Zip-Datei werden mithilfe einer
	 * Schleife sämtlich Einträge ausgelesen(InputStream) und gleichzeitig in das temporäre File
	 * geschrieben(ZipOutputStream). Anschließend werden die hinzuzufügenden Dateien ebenfalls
	 * mithilfe einer Schleife ausgelesen(InputStream) und ebenfalls in das temporäre File
	 * geschrieben(ZipOutputStream). Als letztes werden sämtliche Instanzen geschlossen.
	 * 
	 * @return check Beinhaltet, ob das Entpacken erfolgreich war
	 */
	public boolean addFileToArchive() {

		boolean check = true;

                //Beginn des try-catch Blockes zur Fehlerbehandlung
		try {

                        //Ein temporäres File-Objekt wird erstellt, worin später die bereits vorhandenen und die hinzuzufügenden Dateien geschrieben werden.
			File tmpFile = new File("tmpFile");

			//Zip-Objekt wird erstellt. Dazu wird der Pfad der Zip-Datei übergeben.
			ZipFile archive = new ZipFile(zipFile);

                        //Es wir ein ZipOutputStream Objekt erzeugt und unser temporäres File Objekt übergeben. Dieses Klasse sorgt dafür das das File als Zip-File geschrieben wird.
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmpFile));

                        //Ein Array vom Typ 'byte' wird initialisiert.
			byte[] buffer = new byte[1024];
			int len;

                        //Ein Objekt der Klasse Enumertaion wird initalisiert. Dieses Objekt dient dazu, sämtliche Einträge des bestehenden Zip-Archives als Objekt zu behnadeln.
			Enumeration<? extends ZipEntry> zipeintraege = archive.entries();
                        //Die while-Schleife wird solange ausgeführt, bis keine Elemente mehr in dem Enumeration Objekt vorhanden sind. In der Schleife werden sämtliche Einträge ausgelesen und in das temporäre File mithilfe des ZipOutputStreams geschrieben.
			while (zipeintraege.hasMoreElements()) {
                                //Ein Objekt der Klasse ZipEntry wird erstellt und diesem der Zip-Eintrag des bereits bestehenden Archives übergeben.
				ZipEntry zipeintrag = zipeintraege.nextElement();

                                //Der Name  Name des Zip-Eintrages wird der Variablen 'newzipeintrag' zugewiesen.
				newzipeintrag = new ZipEntry(zipeintrag.getName());

                                //In dem temporären File wird mithilfe des ZipOutputStreams ein neuer Eintrag mit dem eben ausgelesenen Namen ertsellt.
				zos.putNextEntry(newzipeintrag);

                                //Ein Objekt der Klasse BufferedInputStream wird initalisiert. Dieses sorgt dafür den Binärcode(0,1) des vorhandenen Zip-Eintrages auszulesen.
				BufferedInputStream bis = new BufferedInputStream(archive.getInputStream(zipeintrag));

                                // Die while-Schleife wird solange ausgeführt, bis sämtliche bits ausgelesen und
                                // in das temporäre File mithilfe des ZipOutputStreams geschreiben sind.
				while ((len = bis.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

                                //Schließt den aktuellen Eintrag
				zos.closeEntry();
                                //Schließt das aktuelle BufferedInputStream-Objekt.
				bis.close();
			}

                        //Die Methode 'auslesenUndEintragen', welche aus der Abstrakten-Klasse Zip geerbt wird, wird ausgeführt und ihr die hinzuzufügenden Daten und das aktuelle ZipOutputStream-Objekt übergeben.
                        //Damit die Fehlerbehandlung weiterhin ausgeführt werden kann wird die Variable 'check' vom Typ 'boolean' zurückgegeben.
			check = auslesenUndEintragen(zipFiles, zos);
                        
                        //Schließt das Objekt der Klasse ZipFile
			archive.close();
                        //Das eingelesene Zip-File Objekt des alten Archives wird gelöscht, wenn kein 'false' zurückgegeben wurde.
			if(check){
                            zipFile.delete(); 
                        }
                        //Das temporäre File wird nach den alten Zip-File benannt.
			tmpFile.renameTo(zipFile);
                        //Das temporäre File Objekt wird gelöscht.
			tmpFile.delete();

		} catch (IOException exception) {
			System.err.println("Fehlermeldung: " + exception.getMessage());
			check = false;
		}
		return check;

	}

	
}
