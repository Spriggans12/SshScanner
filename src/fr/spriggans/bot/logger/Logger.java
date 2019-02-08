package fr.spriggans.bot.logger;

public class Logger {

	public static final String LINE_SEPARATOR = "----------";

	private Logger() {
		// Nothing
	}

	public static final void log(Object o) {
		System.out.println(o);
	}

	public static final void err(Object o) {
		System.err.println(o);
	}

	public static final void stack(Throwable t) {
		t.printStackTrace();
	}

	public static void emptyLine() {
		log("");
	}

	public static void separationLine() {
		log(LINE_SEPARATOR);
	}

}
