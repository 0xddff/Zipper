package zipper;

import java.util.zip.*;
import java.io.*;

/**
 * 
 * @version 1.0
 * @author Artur Biedulski
 */

/**
 * 
 * Mithilfe dieser Klasse wird ein Objekt erzeugt, welches ein neues Zip-Archiv erstellt. 
 * Des Weiteren erbt diese Klasse von Zip.
 */
public class Create extends Zip{

	private String zipName;
	private String[] zipFiles;

        /**
	 * Parametrisierter Konstruktor, in welchem die neue Zip-Datei und die hinzuzufügenden Dateien initialisiert
	 * werden.
	 * 
	 * @param zipName Name des neuen Zip-Archives.          
	 * @param zipFiles hinzuzufügende Dateien
	 *            
	 */
	public Create(String zipName, String[] zipFiles) {
		this.zipName = zipName;
		this.zipFiles = zipFiles;
	}

        /**
	 * Diese Methode erstellt ein neues Zip-Archiv mit den übergebenen Dateien. 
         * Funktionsprinzip: Das Zip-Archiv wird mit dem übergebenem Namen erstellt (FileOutputStream) und mit dem ZipOutputStream-Objekt 'zos' verbunden.
         * Die hinzuzufügenden Dateien und das ZipOutputStream-Objekt werden der Methode 'auslesenUndEintragen' übergeben.
	 * 
	 * @return check Beinhaltet, ob das Erstellen erfolgreich war
	 */
	public boolean create() {
            boolean check = true;

            try {
                // Zip-Archiv mit Stream verbinden.
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipName));

                //Auslesen und Hinzufügen der einzelnen Einträge.
                check = auslesenUndEintragen(zipFiles, zos);
            }catch (Exception e) {
                e.getMessage();
                check = false;
            }

            return check;
        }
}

