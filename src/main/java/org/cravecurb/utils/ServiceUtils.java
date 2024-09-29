package org.cravecurb.utils;

import java.text.DecimalFormat;
import java.util.Random;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceUtils {
	
	public String generateOTP() {
		return new DecimalFormat("000000").format(new Random().nextInt(999999));
	}
	
	@SneakyThrows
	public String readDataFromFile(String fileName) {
		return new String(ServiceUtils.class.getClassLoader().getResourceAsStream(fileName).readAllBytes());
	}

}
