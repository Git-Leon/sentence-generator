package main.java;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Utils {
	public static <T> void changeField(T object, String fieldName, T fieldVal) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			boolean defaultAccess = field.isAccessible();

			field.setAccessible(true);
			field.set(object, fieldVal);
			field.setAccessible(defaultAccess);
		} catch (Exception e) {

		}
	}

	public static String consoleInput(String prompt) {
		System.out.println(prompt);
		Scanner scanner = new Scanner(System.in);
		String out = scanner.nextLine();
		scanner.close();
		return out;
	}

	public static String dialogueInput(String prompt) {
		return JOptionPane.showInputDialog(prompt);
	}

	public static void disableLogging(Class<?>... classes) {
		for (Class<?> cls : classes) {
			Logger.getLogger(cls.getName()).setLevel(Level.OFF);
		}
	}

	public static String generalException(Exception e) {
		StackTraceElement ste = lastMethodCall(3);
		String exceptionName = e.getClass().getSimpleName();
		String header = "\n" + exceptionName + " caught by " + klickable(ste);
		String border = repeatStr("-", 100);
		return print(
				"\n\n" + border + header + "\n" + parseStackTrace(e).replace("<br>", "\n") + "\n" + border + "\n\n");
	}

	// @return = integer representation of condition
	// @return = -1 if all conditions false
	public static int getCase(boolean... conditions) {
		for (int i = 0; i < conditions.length; i++) {
			if (conditions[i])
				return i;
		}
		return -1;
	}

	// return String representation of specified integer
	public static String getColumnVal(long number) {
		StringBuilder sb = new StringBuilder();
		while (number-- > 0) {
			sb.append((char) ('a' + (number % 26)));
			number /= 26;
		}
		return sb.reverse().toString();
	}

	public static String getCurrentMethodDetails() {
		StackTraceElement ste = lastMethodCall(3);
		int lineNumber = ste.getLineNumber();
		String steMethod = ste.getMethodName() + "()";
		String steClass = ste.getClassName();
		steClass = steClass.substring(steClass.lastIndexOf('.') + 1);

		return steClass + "." + steMethod + " on line " + lineNumber;
	}

	public static String getDate() {
		return Calendar.getInstance().getTime().toString().replace(":", ".");
	}

	public static double getElapsedTime(double t0) {
		return (((double) System.currentTimeMillis()) - t0) / 1000.0;
	}

	/**
	 * @param object
	 *            - object whose fields to access
	 * @param fieldClass
	 *            - type of field to retrieve
	 * @return - ArrayList of values
	 */
	public static <T, E> ArrayList<E> getFieldValues(T object, Class<E> fieldClass) {
		ArrayList<E> fields = new ArrayList<E>();
		for (Field f : object.getClass().getDeclaredFields()) {
			if (f.getType().equals(fieldClass)) {
				try {
					boolean defaultAccess = f.isAccessible();
					f.setAccessible(true);
					E obj = fieldClass.cast(f.get(object));

					fields.add(obj);
					f.setAccessible(defaultAccess);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return fields;
	}

	public static <T> String getId(T obj) {
		return obj.getClass().getSimpleName() + " - " + obj.hashCode();
	}

	public static String reverseString(String text) {
		return new StringBuffer(text).reverse().toString();
	}

	public static void testThis() {
		for (int i = 0; i < 10; i++) {
			print(Utils.randint(0, 1) == 1);
		}

		for (int i = 0; i < 10; i++) {
			print(randomCasing(randomAlphaNum()));
		}
	}

	public static String randomCasing(String val) {
		char[] chars = val.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Utils.randint(0, 1) == 1) {
				chars[i] = Character.toUpperCase(chars[i]);
			} else {
				chars[i] = Character.toLowerCase(chars[i]);
			}
		}
		return new String(chars);
	}

	public static String randomID() {
		return UUID.randomUUID().toString();
	}

	// random alphanumeric string
	public static String randomAlphaNum() {
		return randomID().replace("-", "");
	}

	public static String getRandomString(int length) {
		StringBuilder randomString = new StringBuilder();
		long t0 = System.nanoTime();
		while (length > randomString.length()) {
			randomString.append(getColumnVal(randint(Math.abs((int) t0 / 1000))));
		}
		return randomString.substring(0, Math.min(randomString.length(), length));
	}

	public static String klickable(Class<?> cls, int lineNumber) {
		return klickable(cls.getSimpleName(), lineNumber);
	}

	public static String klickable(StackTraceElement ste) {
		String simpleClassName = ste.getClassName();
		simpleClassName = simpleClassName.substring(simpleClassName.lastIndexOf('.') + 1);
		return klickable(simpleClassName, ste.getLineNumber());
	}

	public static String klickable(String simpleName, int lineNumber) {
		return "(" + simpleName + ".java:" + lineNumber + ")";
	}

	public static StackTraceElement lastMethodCall(int n) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		return stackTraceElements[n];
	}

	public static void main(String[] args) {
		// test();
		testThis();
	}

	public static void makeMissingDirectories(String directory) {
		try {
			new File(directory).mkdirs();
		} catch (Exception e) {
			generalException(e);
		}
	}

	// displays frame with specified output message
	public static void messageBox(String message) {
		JOptionPane.showMessageDialog(new JFrame("Output"), message);
	}

	public static void openFile(String fullpath) {
		File file = new File(fullpath);
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				throw new Exception("Awt Desktop is not supported!");
			}
		} catch (Exception e) {
			generalException(e);
		}
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	// Parse and neatly display stack trace elements
	private static String parseStackTrace(Throwable t) {
		StringBuilder result = new StringBuilder();
		String tmessage = t.getMessage() == null ? "Exception Message = null" : t.getMessage();
		String[] details = tmessage.split("\n");
		String delimiter = "<br>";
		String message = details[0];
		String info = String.join("\n", Arrays.copyOfRange(details, 1, details.length)).replace("\n", "<br>");

		result.append(message + delimiter);
		result.append(delimiter + info + delimiter + delimiter);
		result.append("<b><u>Stacktrace:</b></u>");

		StackTraceElement[] ste = t.getStackTrace();
		Collections.reverse(Arrays.asList(ste));
		for (int i = 1; i < ste.length; i++) {
			StackTraceElement sti = ste[i];
			if (sti.getLineNumber() < 0) {
				continue;
			}
			result.append(delimiter + ">> " + klickable(sti));
		}
		return result.toString();
	}

	public static <T> String print(String baseString, T... args) {
		return print(String.format(baseString, args));
	}

	public static <T> T print(T output) {
		System.out.println(output);
		return output;
	}

	public static String printElapsedTime(double t0) {
		return print("Elapsed Time: %s seconds", getElapsedTime(t0));
	}

	public static <T> T printFields(T object) {
		print("Printing field values of %s", getId(object));
		for (Field field : object.getClass().getDeclaredFields()) {
			try {
				print("\tName: %s\n\tValue: %s\n", field.getName(), field.get(object));
			} catch (Exception e) {

			}
		}
		return object;
	}

	public static ResultSet query(String sql) {
		try {
			String user = System.getenv("AGENTDESK_USERNAME");
			String pass = System.getenv("AGENTDESK_PASS");
			Connection c = DriverManager.getConnection(
					"sqlserver:\\DB05.marlettefunding.com, database=MFDW, username=" + user + ", password=" + pass);
			PreparedStatement ps = (PreparedStatement) c.createStatement();
			ps.close();
			c.close();
			return ps.executeQuery(sql);
		} catch (Exception e) {
			generalException(e);
		}
		return null;
	}

	public static int randint(int max) {
		return randint(0, max);
	}

	public static int randint(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}

	@SafeVarargs
	public static <T> T randomItem(T... args) {
		return args[randint(args.length - 1)];
	}

	public static <T> T randomItem(List<T> list) {
		return randomItem(Utils.toArray(list));
	}

	// removes all specified 'removable' characters from base String
	public static String removeChars(String base, String removables) {
		for (Character removable : removables.toCharArray()) {
			base = base.replace(removable.toString(), "");
		}
		return base;
	}

	public static String removeStrings(String base, String... removeableStrings) {
		for (String removeableString : removeableStrings) {
			base = base.replace(removeableString, "");
		}
		return base;
	}

	public static String removeSubstring(String base, String... removeAbles) {
		for (String removeAble : removeAbles) {
			base.replace(removeAble, "");
		}
		return base;
	}

	public static String repeatStr(String str, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static void runMethod(Object invoker, String methodName) {
		Method methodToFind = null;
		try {
			if (invoker != null) {
				methodToFind = invoker.getClass().getMethod(methodName);
				if (methodToFind != null) {
					methodToFind.invoke(invoker);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean strsNotNull(String... strs) {
		for (String str : strs)
			if (str == null || "".equals(str) || "null".equals(str.toLowerCase()))
				return false;
		return true;
	}

	public static void test() {
		Object ranObj = "" + randint(9999);
		printFields(ranObj);
		for (Object obj : getFieldValues(ranObj, String.class)) {
			print(obj.toString());
		}
	}

	public static boolean textEquals(String base, String... compares) {
		for (String compare : compares) {
			if (base.equals(compare)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <E> E[] toArray(List<E> list) {
		return list.toArray((E[]) new Object[list.size()]);
	}

	public static void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static File writeToFile(String fileName, String fileText) {
		BufferedWriter writer = null;
		File file = new File(fileName);
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(fileText);
		} catch (Exception e) {
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
			openFile(file.getAbsolutePath());
		}
		return file;
	}

	@SafeVarargs
	public static <T> String toString(T... vals) {
		StringBuilder sb = new StringBuilder();
		for (T val : vals) {
			sb.append(val);
		}
		return sb.toString();
	}

	public static String affirmate(boolean b) {
		return b ? "Yes" : "No";
	}

}