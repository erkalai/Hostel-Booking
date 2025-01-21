package com.hostelbooking.component;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class DateUtils {
	public String format(LocalDate date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return date.format(formatter);
	}
}
