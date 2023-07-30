package com.florenceconsulting.demorest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.florenceconsulting.demorest.model.User;

public class CSVHelper {

	public static final String TYPE = "text/csv";

	public static final String[] HEADERS = { "Nome", "Cognome", "Email", "CodiceFiscale" };

	public static boolean hasCSVFormat(MultipartFile file) {
		return TYPE.equals(file.getContentType());
	}

	public static List<User> userConverter(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<User> users = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				User user = new User();
				user.setFirstName(csvRecord.get("Nome"));
				user.setLastName(csvRecord.get("Cognome"));
				user.setEmail(csvRecord.get("Email"));
				user.setCodiceFiscale(csvRecord.get("CodiceFiscale"));
				users.add(user);
			}

			return users;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

}