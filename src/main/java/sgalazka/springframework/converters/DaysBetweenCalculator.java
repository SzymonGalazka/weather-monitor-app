package sgalazka.springframework.converters;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DaysBetweenCalculator {
	public static List<LocalDate> getDatesBetween(
			LocalDate startDate, LocalDate endDate) {

		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));
		return IntStream.iterate(0, i -> i + 1)
				.limit(numOfDaysBetween)
				.mapToObj(startDate::plusDays)
				.collect(Collectors.toList());
	}

}
