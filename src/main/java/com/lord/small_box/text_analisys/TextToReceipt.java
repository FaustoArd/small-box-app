package com.lord.small_box.text_analisys;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.utils.PdfToStringUtils;
import lombok.RequiredArgsConstructor;

@Component
public class TextToReceipt {

	private final String strDateV2 = "^(([0-9]{2})*([-/]){1}){2}([0-9]{2,4})";
	private final String totalRegex4Current = "^(?=.?[^sub]*?[t]{1}[o]{1}[t]{1}[a]{1}[l]{1}[:;]?\\s*)";
	private final String strTicketTotalV3 = "^(([0-9]+)+[.,]+)+([0-9]{2})$";
	private final String strTicketTwoPart = "^.*(?=.*[0-9]{4,5})(.)*([0]{2}[0-9]{6}[.]*)";
	

	public List<SmallBox> getPdfToSmallBoxList(String pdfText) throws ParseException {
		return Stream.of(pdfText.split("\n")).map(receipt -> {
			SmallBox smallBox = new SmallBox();

			try {
				smallBox.setDate(getPurchaseDateFromText(receipt.split(" ")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			smallBox.setTicketNumber(getTwoPartTicketNumberFromText(receipt));
			//String result = getTotalFromText(receipt);
			//System.out.println("Total result = " + result);
			//BigDecimal total = BigDecimal.valueOf(Double.valueOf(getTotalFromText(receipt)));
			//smallBox.setTicketTotal(total);
			smallBox.setProvider(getProviderNameFromText(receipt));

			return smallBox;

		}).toList();
	}

	private String getTotalFromText(String text) {
		Pattern pTotal = Pattern.compile(totalRegex4Current, Pattern.CASE_INSENSITIVE);
		return Stream.of(text).filter(f -> pTotal.matcher(f.trim()).find())
				.map(m -> m.replace("$", "").replace("%", "")).map(this::getTotal).findFirst().get();

	}

	private String getTotal(String line) {
		System.out.println("Total result = " + line);
		line = line.replace("„", ".").replace(",,", ".").replace(";;", ".").replace("O", "0");
		return Stream.of(line.split(" "))
				.filter(f -> f.trim().matches(strTicketTotalV3)).findFirst()
			
				.get().replace(",",".");
	}

	

	
	private final Pattern patternTicketTwoPart = Pattern.compile(strTicketTwoPart);

	private String getTwoPartTicketNumberFromText(String text) {
		return Stream.of(text.split("\\n")).filter(f -> patternTicketTwoPart.matcher(f).find())
				.map(m -> m.replaceAll("[a-z\\D\\r^-]", ""))
				.findFirst().get();

	}

	private String getProviderNameFromText(String text) {

		return Stream.of(text.split("\\n")).findFirst().get();

	}

	private final Pattern patternDate = Pattern.compile(strDateV2, Pattern.CASE_INSENSITIVE);

	private Calendar getPurchaseDateFromText(String[] text) throws ParseException {
		String splittedText = Stream.of(text).collect(Collectors.joining(" "));
		Stream.of(splittedText).forEach(e -> System.out.println(e));
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String[] result = Stream.of(splittedText.split(" "))
				.filter(f -> patternDate.matcher(f).find())
				.sorted((s2, s1) -> s1.compareTo(s2)).collect(Collectors.joining(" ")).split(" ");
		for (String s : result) {
			System.out.println(s);
		}
		if (result.length == 0) {
			return null;
		}
		
		try {
			if (result.length == 1) {
				cal.setTime(sdf.parse(result[0].replace(result[0].substring(6, 8), "20" +result[0].substring(6, 8)).replace("/", "-")));
				return cal;
			} else {
				cal.setTime(sdf.parse(result[1].replace(result[1].substring(6, 8), "20" +result[1].substring(6, 8)).replace("/", "-")));
				return cal;
			}
		}catch(ParseException e) {
			throw new RuntimeException("Error al parsear fecha");
		}
	}

}
