package pr3;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Полезные процедуры для обработки дат
 *
 * Created by Ermakov Dmitry on 12/18/15.
 */
public class DateUtils {

	public static final ZoneId MOSCOW_ZONE_ID = ZoneId.of("Europe/Moscow");
	public static final Locale defaultLocale = Locale.forLanguageTag("RU");

	/**
	 * From  java.sql.Date to LocalDate:
	 */
	public static LocalDate sqlDateToLocalDate(java.sql.Date sqlDate) {
		LocalDate localDate = null;

		if (sqlDate != null) {
			localDate = Instant.ofEpochMilli(sqlDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		}

		return localDate;
	}

	/**
	 * From long to LocalDateTime:
	 */
	public static LocalDateTime unixtimeToLocalDateTime(long unixtime) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixtime), MOSCOW_ZONE_ID);
	}

	/**
	 * From  LocalDate to java.sql.Date:
	 */
	public static java.sql.Date localDateToSqlDate(LocalDate localDate) {
		java.sql.Date sqlDate = null;

		if (localDate!=null) {
			sqlDate = java.sql.Date.valueOf(localDate);
		}

		return sqlDate;
	}

	/**
	 * Текущий год в виде целого числа
	 */
	public static int getCurrentYear() {
//		java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
//		calendar.setTime(new java.util.Date());
//		return calendar.get(java.util.Calendar.YEAR);
		return LocalDate.now().getYear();
	}

	/**
	 * From java.util.Date to LocalDate
	 */
	public static LocalDate utilDateToLocalDate(Date utilDate) {
		LocalDate res = null;

		if (utilDate != null) {
			Instant instant = Instant.ofEpochMilli(utilDate.getTime());
			res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		}

		return res;
	}

	/**
	 * From java.util.Date to LocalDateTime
	 */
	public static LocalDateTime utilDateToLocalDateTime(Date utilDate) {
		LocalDateTime res = null;

		if (utilDate != null) {
			Instant instant = Instant.ofEpochMilli(utilDate.getTime());
			res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		}

		return res;
	}

	/**
	 * From LocalDateTime to java.util.Date
	 */
	public static Date localDateTimeToUtilDate(LocalDateTime localDateTime) {
		Date res = null;

		if (localDateTime != null) {
			ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
			res = Date.from(zonedDateTime.toInstant());
		}

		return res;
	}

	/**
	 * From java.util.Date to java.sql.Date:
	 */
	public static java.sql.Date utilDateToSqlDate(Date utilDate) {
		java.sql.Date sqlDate = null;

		if (utilDate != null) {
			sqlDate = java.sql.Date.valueOf(utilDateToLocalDate(utilDate));
		}

		return sqlDate;
	}

	public static LocalDate strToLocalDate(String strDate, String pattern) {
		LocalDate localDate = null;

		if (strDate != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			formatter = formatter.withLocale(defaultLocale);  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
			localDate = LocalDate.parse(strDate, formatter);
		}

		return localDate;
	}
}
