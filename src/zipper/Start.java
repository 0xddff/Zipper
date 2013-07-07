package zipper;

/**
 * @version 1.0
 * @author Oliver Heidemann
 */

/**
 * Start-Klasse, dient der Verwaltung und Strukturierung.
 */
public class Start {

	/**
	 * In dieser Methode wird das Kommando ausgewertet. Existiert der im Kommando enthaltene Case, so
	 * wird dieser dementsprechend aufgerufen. Ist das Kommando ungültig wird der Default-Block
	 * ausgeführt und das Programm beendet.
	 * 
	 * @param args Parameter die das Programm beim Start übergeben bekommt
	 */
	public static void main(String[] args) {
		setzeParameterUndPruefeVollstaendigkeit(args);

		switch (command) {
			case UNZIP: {
				Unzip unzip = new Unzip(zipName, zipDestination);
				if (unzip.extractZIPArchiv()) {
					message = "Archiv erfolgreich entpackt!";
				} else {
					message = "Fehler bei der Ausführung: Bitte ueberpruefen Sie ihre Angaben!";
				}
				break;
			}
			case ZIP: {
				Create zc = new Create(zipName, zipFiles);
				if (zc.create()) {
					message = "Archiv erfolgreich gepackt!";
				} else {
					message = "Fehler bei der Ausführung: Bitte ueberpruefen Sie ihre Angaben!";
				}
				break;
			}
			case ADD: {
				Add add = new Add(zipName, zipFiles);
				if (add.addFileToArchive()) {
					message = "Archiv erfolgreich modifiziert!";
				} else {
					message = "Fehler bei der Ausführung: Beachten Sie die Fehlermeldung und fuehren Sie das Programm erneut aus.";
				}
				break;
			}
			case DISPLAY: {
				Display display = new Display(zipName);
				if (display.showZipInfo()) {
					message = "\nDatei-Informationen erfolgreich ausgelesen.";
				} else {
					message = "Fehler bei der Ausführung: Beachten Sie die Fehlermeldung und fuehren Sie das Programm erneut aus.";
				}
				break;
			}
			case INFO: {
				message = "\n Die folgenden Parameter stehen ihnen zur Verfügung: " + "\n (c)reate - erstellt ein Zip-Archiv mit mehreren Dateien"
						+ "\n (a)dd - fuegt einem bestehenden Zip-Archiv Dateien hinzu" + "\n (x)tract - extrahiert alle Dateien"
						+ "\n (d)isplay - listet alle im Archiv befindlichen Dateien auf\n";

				break;
			}
			default: {
				message = "Fehler bei der Ausführung: Es wurde ein falscher Parameter angegeben!";
			}
		}
		System.out.println(message);
	}

	/**
	 * Mit dieser Methode wird die Vollständigkeit der Parameter überprüft, Variablen
	 * initialisiert und ggf. Standardwerte gesetzt.
	 * 
	 * @param args Parameter die das Programm beim Start übergeben bekommt
	 */
	public static void setzeParameterUndPruefeVollstaendigkeit(String[] args) {
		command = INFO; // Standardinitialisierung um den Info-Case bei Fehleingaben aufzurufen.
                
		if (args.length < 1) {
			message = "Fehler bei Ausführung: Es wurden zu wenig Parameter uebergeben!";
		} else if (args[COMMAND].matches("-\\w") && args.length >= 2) {
			command = args[COMMAND].replace("-", "").toCharArray()[0];
			zipName = args[ZIPNAME];
			zipDestination = CURRENT_DIR; // Setzen des Standardpfades --> Pfad in dem das JAR liegt

			/*
			 * Wenn das Entpacken eines Archivs und ein spezieller Ordner angegeben wurde, dann merke
			 * ihn dir.
			 */
			if (args.length == 3 && command == UNZIP) {
				zipDestination = args[ZIPDESTINATION];
			}

			/*
			 * Wenn das Archiv gepackt oder eine Datei/Ordner zum Archiv hinzugefuegt werden soll,
			 * dann merke dir diese Angaben.
			 */
			if (args.length >= 3 && (command == ADD || command == ZIP)) {
				zipFiles = new String[args.length - 2];
				for (int i = 3; i <= args.length; i++) {
					zipFiles[i - 3] = args[i - 1];
				}
			}/*
			 * Entspricht das Kommando nicht dem Muster wird die Fehlermeldung dementsprechend
			 * angepasst und der Info-Case durchlaufen.
			 */
		} else if (!args[COMMAND].matches("-\\w")) {
			message = "Fehler bei Ausführung: Ihr Startparameter entspricht nicht dem Muster!";
		}
	}

	private final static int	COMMAND			= 0;
	private final static int	ZIPNAME			= 1;
	private final static int	ZIPDESTINATION	= 2;

	private final static char	UNZIP			= 'x';
	private final static char	ZIP			= 'c';
	private final static char	ADD			= 'a';
	private final static char	DISPLAY			= 'd';
	private final static char	INFO			= 'i';

	private static char		command;
	private static String[]		zipFiles;
	private static String		zipDestination;
	private static String		zipName;
	private static String		message;
	private final static String	CURRENT_DIR		= System.getProperty("user.dir");
}