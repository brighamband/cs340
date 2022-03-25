
package spellcheck.src;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;
import java.util.SortedMap;


public class Main {

	public static void main(String[] args) {

		try {
			Injector injector = Guice.createInjector(new Module());
			SpellingChecker checker = injector.getInstance(SpellingChecker.class);

			SortedMap<String, Integer> mistakes = checker.check(args[0]);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

