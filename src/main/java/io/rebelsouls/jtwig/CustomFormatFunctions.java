package io.rebelsouls.jtwig;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.annotations.JtwigFunction;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFormatFunctions {

	private DateTimeFormatter defaultDateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-CL"));
	
	private DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-CL"));
	private NumberFormat defaultCurrencyFormatter = new DecimalFormat("CLP$###,###,###,##0", symbols);
	private NumberFormat defaultNumberFormatter = new DecimalFormat("###,###,###,##0.##", symbols);
	private NumberFormat defaultPercentFormatter = new DecimalFormat("##0.##%", symbols);
	
	@JtwigFunction("date")
	public String formatDate(FunctionRequest request) {
		TemporalAccessor time = (TemporalAccessor)request.get(0);
		
		DateTimeFormatter formatter;
		if(request.getNumberOfArguments() > 1) {
			String format = request.get(1).toString();
			formatter = DateTimeFormatter.ofPattern(format);
		}
		else {
			formatter = defaultDateFormatter;
		}
		
		return formatter.format(time);
	}
	
	@JtwigFunction("currency")
	public String formatCurrency(FunctionRequest request) {
		return formatNumber(request, defaultCurrencyFormatter);
	}

	@JtwigFunction("number")
	public String formatNumber(FunctionRequest request) {
		return formatNumber(request, defaultNumberFormatter);
	}
	
	@JtwigFunction("percent")
	public String formatPercent(FunctionRequest request) {
		return formatNumber(request, defaultPercentFormatter);
	}
	
	public String formatNumber(FunctionRequest request, NumberFormat defaultFormatter) {
		request.maximumNumberOfArguments(2);
		request.minimumNumberOfArguments(1);
		
		Number number = (Number) request.get(0);
		
		NumberFormat format;
		if(request.getNumberOfArguments() > 1) {
			String pattern = request.get(1).toString();
			format = new DecimalFormat(pattern);
		}
		else {
			format = defaultFormatter;
		}
		
		return format.format(number);
	}
}
