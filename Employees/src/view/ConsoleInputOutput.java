package view;


import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConsoleInputOutput {

	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private PrintStream output = System.out;

	//READ

	//line
	public String readString(String prompt) {
		output.println(prompt);
		try {
			String res = input.readLine();
			return res;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	//line corresponds to predicate
	public String readString(String prompt, String errorPrompt, Predicate<String> predicate) {
		return readObject(prompt, errorPrompt, string -> {
			if(!predicate.test(string)) {
				throw new IllegalArgumentException("");
			}
			return string;
		});
	}

	//line corresponds to options
	public String readString(String prompt, String errorPrompt, Set<String> options) {
		return readString(prompt, errorPrompt, s -> options.contains(s));
	}

	//object
	public <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		boolean running = false;
		T res = null;
		do {
			running = false;
			String resInput = readString(prompt);
			try {
				res = mapper.apply(resInput);

			} catch (Exception e) {
				writeLine(errorPrompt + ": " + e.getMessage());
				running = true;
			}

		} while (running);
		return res;
	}

	//int
	public int readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Integer::parseInt);
	}

	//int from - to
	public int readInt(String prompt, String errorPrompt, int min, int max) {
		return readObject(String.format("%s[%d - %d] ", prompt, min, max), errorPrompt,
				string -> {

					int res = Integer.parseInt(string);
					if (res < min) {
						throw new IllegalArgumentException("must be not less than " + min);
					}
					if (res > max) {
						throw new IllegalArgumentException("must be not greater than " + max);
					}
					return res;

				});
	}

	//long
	public long readLong(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Long::parseLong);
	}

	//long from min to max
	public long readLong(String prompt, String errorPrompt, long min, long max) {
		return readObject(String.format("%s[%d - %d] ", prompt, min, max), errorPrompt, string -> {
			long res = Long.parseLong(string);
			if (res < min) {
				throw new IllegalArgumentException("must be not less than " + min);
			}
			if (res > max) {
				throw new IllegalArgumentException("must be not greater than " + max);
			}
			return res;
			
		});
	}

	
	//date
	public LocalDate readDate(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, s -> LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE));
	}

	//date from - to
	public LocalDate readDate(String prompt, String errorPrompt, LocalDate from, LocalDate to) {
		return readObject(String.format("%s[%s - %s] ", prompt, from.toString(), to.toString()), errorPrompt, string -> {
			LocalDate res = LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE);
			if (res.compareTo(from) < 0) {
				throw new IllegalArgumentException("must be not less than " + from.toString());
			}
			if (res.compareTo(to) > 0) {
				throw new IllegalArgumentException("must be not greater than " + to.toString());
			}
			return res;
			
		});
	}

	//double
	public double readDouble(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}


	//WRITE

	//string
	public void write(String string) {
		output.print(string);
	}

	//line
	public void writeLine(String string) {
		write(string + "\n");
	}

}
