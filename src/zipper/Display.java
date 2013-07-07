package zipper;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @version 1.0
 * @author Marvin König, Oliver Heidemann, Artur Biedulski
 */

/**
 * Mithilfe dieser Klasse wird ein Objekt erzeugt, welches Informationen zur Zip-Datei ausgibt.
 */
public class Display {

	private File			zipFile;
	private static final String	BYTE			= "Byte";
	private static final String	DATEINAME		= "Dateiname";
	private static final String	DATEIGROESSE            = "Dateigroesse";
	private static final String	KOMPRIMIERT		= "komprimiert";

	/**
	 * Parametrisierter Konstruktor, in welcher die Zip-Datei initialisiert wird. Bei der
	 * Objekterzeugung in der Start-Klasse wird folgender Parameter übergeben.
	 * 
	 * @param zipFile String, welcher den Pfad des Zip-Archives enthält.
	 */
	public Display(String zipFile) {
		this.zipFile = new File(zipFile);
	}

	/**
	 * Die Methode "showZipInfo" ist für das ausgeben von Informationen eines Zip-Archives
	 * verantwortlich. Der Methodenaufruf erfolgt in der Start-Klasse. 
         * Funktionsprinzip: Zunächst wird ein Zip-Objekt erzeugt und dabei der Pfad zum Zip-Archiv übergeben. 
         * Anschließend werden mithilfe vorgegebener Methoden der Klasse ZipFile generelle Informationen des Zip-Archives
	 * ausgegeben. Danach werden Dateiinformationen aller einzelnen Dateieinträge mithilfe einer
	 * Schleife nach gleichem Prinzip ausgegeben. Zu guter Letzt wird die Zip-Objekt Instanz wieder
	 * geschlossen.
	 * 
	 * @return check Beinhaltet, ob das Entpacken erfolgreich war
	 */
	public boolean showZipInfo() {

		boolean check = true;
                
                //Beginn des try-catch Blockes zur Fehlerbehandlung
		try {

                        //Zip-Objekt wird erstellt. Dazu wird der Pfad der Zip-Datei übergeben.
			ZipFile archive = new ZipFile(zipFile);
                        //Ein Objekt der Klasse Enumertaion wird initalisiert. Dieses Objekt dient dazu, sämtliche Einträge des bestehenden Zip-Archives als Objekt zu behnandeln.
			Enumeration<? extends ZipEntry> zipeintraege = archive.entries();
                        //Die Methode 'ermittleLaengstenDateiNamen' wird ausgeführt, und der längste Dateipfad der ausgelesenen Dateien als int-Varibale zurückgegeben.
			int laengeDateiname = ermittleLaengstenDateiNamen(zipeintraege);
                        //Die Einträge des Zip-Objekts wird dem Enumeration-Objekt zugewiesen.
			zipeintraege = archive.entries();
                        //Die Methode 'ermittleLaengsteSizeAngabe' wird ausgeführt, und die längste Größenangabe der ausgelesenen Dateien als int-Varibale zurückgegeben.
			int laengeSize = ermittleLaengsteSizeAngabe(zipeintraege);
                        //Die Einträge des Zip-Objekts wird dem Enumeration-Objekt zugewiesen.
			zipeintraege = archive.entries();
                        //Die Methode 'ermittleLaengsteCompressedAngabe' wird ausgeführt, und die längste komprimierte Größenangabe der ausgelesenen Dateien als int-Varibale zurückgegeben.
			int laengeCompressed = ermittleLaengsteCompressedAngabe(zipeintraege);
                        //Die Methode 'getTable' wird ausgeführt. Dabei werden die (größten) Längenangaben des Dateipfades, der Dateigröße und der komprimierten Dateigröße übergeben, um danach eine Tabelle zu erzeugen.
                        //Zurückgegeben wird ein String mit einer bestimmten Anzahl an (-)-Zeichen, je nach länge der einzelnen Angaben.
			String striche = getTable(laengeDateiname, laengeSize, laengeCompressed);

			System.out.println("ZIP-Info");
			System.out.println("========");
                        //Der Name des Archives wird ausgegeben.
			System.out.println("ZIP-Archiv: " + archive.getName());
                        //Die Anzahl an Dateien im Archiv wird ausgegeben.
			System.out.println("Dateianzahl: " + archive.size() + "\n");
                        //Ausgabe der (-)-Zeichen als Teil der Tabelle(Obere Linie des Rahmens)
			System.out.println(striche);
                        //Ausgabe von "Dateinname", "Dateigröße" und "komprimiert", angepasst nach der größten länge der einzelnen Angaben. Ebenfalls werden (-)-Elemnte für die Spalten ausgegeben. 
			System.out.printf("\n| %-" + laengeDateiname + "s | %-" + laengeSize + "s | %-" + laengeCompressed + "s |", DATEINAME, DATEIGROESSE, KOMPRIMIERT);
                        //Ausgabe der (-)-Zeichen als Teil der Tabelle(Mittlere Linie; Abgrenzung von Titel und Werten)
			System.out.println("\n\n" + striche);
                        //Die Einträge des Zip-Objekts wird dem Enumeration-Objekt zugewiesen.
			zipeintraege = archive.entries();
                        //Die while-Schleife wird solange ausgeführt, bis keine Elemente mehr in dem Enumeration Objekt vorhanden sind.
			while (zipeintraege.hasMoreElements()) {
                                
                                //Ein Objekt der Klasse ZipEntry wird erstellt und diesem ein Eintrag des Zip-Archives übergeben.
				ZipEntry zipeintrag = zipeintraege.nextElement();
                                //Ausgabe von Dateiname, Dateigröße und komprimierter Dateigröße der einzelnen Dateien, angepasst nach der größten länge der einzelnen Angaben. Ebenfalls werden (-)-Elemnte für die Spalten ausgegeben.
				if (!zipeintrag.isDirectory()) {
					System.out.printf("| %-" + laengeDateiname + "s | %-" + laengeSize + "s | %-" + laengeCompressed + "s |", zipeintrag.getName(),
							zipeintrag.getSize() + BYTE, zipeintrag.getCompressedSize() + BYTE);
					System.out.println("");
				}
			}
                        //Ausgabe der (-)-Zeichen als Teil der Tabelle(Untere Linie des Rahmens)
			System.out.println(striche);
			System.out.println("");
                        //Schließt das Objekt der Klasse ZipFile
			archive.close();

		} catch (IOException exception) {
			System.out.println("Fehlermeldung: " + exception.getMessage());
			check = false;
		}
		return check;
	}

        /**
	 * Die Methode "ermittleLaengstenDateiNamen" ist für das herausfinden des längsten String-Wert(Pfad der Datei) der Dateinamen aus dem Zip-Archiv zuständig.  
	 * Funktionsprinzip: In einer while-Schleife werden alle Zip-Einträge durchlaufen. Immer wenn der aktuelle Dateiname größer ist, 
         * wird die Länge als Integer-Wert in die Variable 'wert' übernommen. Sind alle Einträge durchlaufen wird die Variable 'wert' an den Methodenaufruf zurückgegeben. 
         * 
	 * @param zipeintraege Enumeration-Objekt, welches die einzelnen Zip-Einträge beinhaltet.
         * 
         * @return wert Variable vom Typ Integer, welches den Wert des längsten String enthält. 
	 */
	public int ermittleLaengstenDateiNamen(Enumeration<? extends ZipEntry> zipeintraege) {
		int wert = DATEINAME.length() + 1;
		while (zipeintraege.hasMoreElements()) {
			ZipEntry zipeintrag = zipeintraege.nextElement();
			if (zipeintrag.getName().length() > wert) {
				wert = zipeintrag.getName().length();
			}
		}
		return wert;
	}

        /**
	 * Die Methode "ermittleLaengsteSizeAngabe" ist für das herausfinden der längsten Größenangabe der Dateien aus dem Zip-Archiv zuständig.  
	 * Funktionsprinzip: In einer while-Schleife werden alle Zip-Einträge durchlaufen. Immer wenn der aktuelle Dateiname größer ist, 
         * wird die Länge als Integer-Wert in die Variable 'wert' übernommen. Sind alle Einträge durchlaufen wird die Variable 'wert' an den Methodenaufruf zurückgegeben. 
         * 
	 * @param zipeintraege Enumeration-Objekt, welches die einzelnen Zip-Einträge beinhaltet.
         * 
         * @return wert Variable vom Typ Integer, welches den Wert des längsten String enthält. 
	 */
	public int ermittleLaengsteSizeAngabe(Enumeration<? extends ZipEntry> zipeintraege) {
		int wert = DATEIGROESSE.length() + 1;
		while (zipeintraege.hasMoreElements()) {
			ZipEntry zipeintrag = zipeintraege.nextElement();
			if (String.valueOf(zipeintrag.getSize()).length() > wert) {
				wert = String.valueOf(zipeintrag.getSize()).length() + BYTE.length() + 1;
			}
		}
		return wert;
	}

        /**
	 * Die Methode "ermittleLaengsteCompressedAngabe" ist für das herausfinden des längsten String-Wert(Pfad der Datei) der Dateinamen aus dem Zip-Archiv zuständig.  
	 * Funktionsprinzip: In einer while-Schleife werden alle Zip-Einträge durchlaufen. Immer wenn der aktuelle Dateiname größer ist, 
         * wird die Länge als Integer-Wert in die Variable 'wert' übernommen. Sind alle Einträge durchlaufen wird die Variable 'wert' an den Methodenaufruf zurückgegeben. 
         * 
	 * @param zipeintraege Enumeration-Objekt, welches die einzelnen Zip-Einträge beinhaltet.
         * 
         * @return wert Variable vom Typ Integer, welches den Wert des längsten String enthält. 
	 */
	public int ermittleLaengsteCompressedAngabe(Enumeration<? extends ZipEntry> zipeintraege) {
		int wert = KOMPRIMIERT.length() + 1;
		while (zipeintraege.hasMoreElements()) {
			ZipEntry zipeintrag = zipeintraege.nextElement();
			if (String.valueOf(zipeintrag.getCompressedSize()).length() > wert) {
				wert = String.valueOf(zipeintrag.getCompressedSize()).length() + BYTE.length() + 1;
			}
		}
		return wert;
	}

        /**
	 * Die Methode "getTable" ist für das Erstellen der Tabelle für die Ausgabe der einzelnen Dateiinformationen zuständig.
         * Funktionsprinzip: In 3 for-Schleifen werden exakt genauso viele (-)-Zeichen in einen String gespeichert, 
         * wie die Stellen des jeweils längsten Dateinamens, Dateigröße und komprimierte Dateigröße.
         * Anschließend wird dieser gesamte String dem Methodenaufruf zurück gegeben.
         * 
	 * @param laengeDateinamen Stellenwert als Integer des längsten Dateinamens
         * @param laengeSize Stellenwert als Integer der längsten Dateigröße
         * @param laengeCompressed Stellenwert als Integer der längsten komprimierten Dateigröße
         * 
         * @return table String mit exakt genauso vieler (-)-Zeichen, wie die längsten Informationen von Dateiname, Dateigröße und komprimierte Dateigröße.
	 */
	public String getTable(int laengeDateinamen, int laengeSize, int laengeCompressed) {
		String minus = "-";
		String minus1 = "";
		String minus2 = "";
		String minus3 = "";

		for (int j = 0; j < laengeDateinamen; j++) {
			minus1 += minus;
		}
		for (int k = 0; k < laengeSize; k++) {
			minus2 += minus;
		}
		for (int l = 0; l < laengeCompressed; l++) {
			minus3 += minus;
		}
		String table = "+ " + minus1 + " + " + minus2 + " + " + minus3 + " +";
		return table;
	}

}
