package com.lord.small_box.text_analisys;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.lord.small_box.models.SmallBox;

@Component
public class TextToReceipt {

	private static final Logger log = LoggerFactory.getLogger(TextToReceipt.class);

	private final String strDateV2 = "^(([0-9]{2})*([-/]){1}){2}([0-9]{2,4})";
	private final String totalRegex4Current = "^(?=.?[^sub]*?[t]{1}[o]{1}[t]{1}[a]{1}[l]{1}[:;]?\\s*)";
	private final String strTicketTotalV3 = "^(([0-9]+)+[.,]+)+([0-9]{2})$";
	

	public List<SmallBox> getPdfToSmallBoxList(String pdfText) throws ParseException {
		List<SmallBox> smallBoxsList = new ArrayList<>();
		// System.out.println(pdfText);
		List<String> textList = List.of(pdfText.split("PageEnd"));
		// textList.forEach(e -> System.out.println(e));
		ListIterator<String> it = textList.listIterator();
		//it.next();
		it.forEachRemaining(page -> {
			SmallBox smallBox = new SmallBox();

			try {
				smallBox.setDate(getPurchaseDateFromText(page.split(" ")));
			} catch (ParseException e) {
				throw new RuntimeException("Error al parsear fecha");
			}
			smallBox.setTicketNumber(getTwoPartTicketNumberFromText(page));
			String result = getTotalFromText(page);
			System.out.println("Total result = " + result);
			BigDecimal total = BigDecimal.valueOf(Double.valueOf(getTotalFromText(page)));
			smallBox.setTicketTotal(total);
			smallBox.setProvider(getProviderNameFromText(page));
			smallBoxsList.add(smallBox);

		});
		System.out.println(textList.size());
		return smallBoxsList;
	}

	private String getTotalFromText(String text) {
		log.info("Getting total:");
		Pattern pTotal = Pattern.compile(totalRegex4Current, Pattern.CASE_INSENSITIVE);
		return Stream.of(text.split("\\n")).filter(f -> pTotal.matcher(f.trim()).find())
				.map(m -> m.replace("$", "").replace("%", "")).map(this::matchTotal).findFirst().orElse("0");

	}

	private String matchTotal(String line) {
		line = line.replace("„", ".").replace(",,", ".").replace(";;", ".").replace("O", "0").replace(",", ".");
		return Stream.of(line.split(" ")).filter(f -> f.trim().matches(strTicketTotalV3)).map(this::findIllegalDots)
				.findFirst().orElse("0");
	}

	private String findIllegalDots(String total) {
		char[] chars = total.trim().toCharArray();
		for (int i = 0; i < chars.length - 3; i++) {
			if (chars[i] == '.') {
				chars[i] = ' ';
			}
		}
		return Arrays.toString(chars).replace(",", "").replace(" ", "").replace("[", "").replace("]", "");
	}

	private final String strTicketTwoPart = "^.*(?=.*[0-9^.]{4,5})(.)*([0]{2}[0-9]{6}[.]*)";
	private final Pattern patternTicketTwoPart = Pattern.compile(strTicketTwoPart);

	private String getTwoPartTicketNumberFromText(String text) {
		log.info("Getting Ticket:");
		System.out.println("Ticket: " +text);
		return Stream.of(text.split(" ")).filter(f -> patternTicketTwoPart.matcher(f).find())
				//.map(m -> m.replaceAll("[a-z\\D\\r^-]", ""))
				.findFirst().orElse("0");

	}

	private String getProviderNameFromText(String text) {
		log.info("Getting Provider:");
		return Stream.of(text.split("\\n")).findFirst().get();

	}

	private final Pattern patternDate = Pattern.compile(strDateV2, Pattern.CASE_INSENSITIVE);

	private Calendar getPurchaseDateFromText(String[] text) throws ParseException {
		log.info("Getting Date:");
		String splittedText = Stream.of(text).collect(Collectors.joining(" "));
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String[] result = Stream.of(splittedText.split(" ")).filter(f -> patternDate.matcher(f).find())
				.sorted((s2, s1) -> s1.compareTo(s2)).collect(Collectors.joining(" ")).split(" ");
		if (result.length == 0) {
			return null;
		}
		try {
			if (result.length == 1) {
				System.out.println("Length 1: " + result[0]);
				if (result[0].length() == 8) {
					cal.setTime(sdf.parse(result[0].replace(result[0].substring(6, 8), "20" + result[0].substring(6, 8))
							.replace("/", "-")));
				} else {
					cal.setTime(sdf.parse(result[0].replace("/", "-")));
				}
				return cal;
			} else if (result.length == 2) {
				System.out.println("Length 2: " + result[1]);
				if (result[1].length() == 8) {
					
					cal.setTime(sdf.parse(result[1].replace(result[1].substring(6, 8), "20" + result[1].substring(6, 8))
							.replace("/", "-")));
				} else {
					cal.setTime(sdf.parse(result[1].replace("/", "-")));
				}
			}
			return cal;
		} catch (ParseException e) {
			return Calendar.getInstance();
		}
	}

}
