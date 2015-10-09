package appo.pacman.ann.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import appo.pacman.ann.evolution.Generation;

import com.google.gson.Gson;

public class IOManager {
	private static String fileSeparator = System.getProperty("file.separator");

	private static String baseFilePath = "networks";

	private static String generationFileName = "generation";
	private static String fileExtension = ".json";
	private static Gson gson = new Gson();

	/**
	 * Saves a generation to a file 
	 */
	public static void saveGenerationToFile(Generation generation) {
		String json = gson.toJson(generation);
		try {
			FileUtils.write(new File(getGenerationFileName(generation.getGenerationNumber())), json, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the generation with the highest generation number found as a file 
	 */
	public static Generation loadLatestGeneration() {
		int latestGenerationNumber = getLatestGenerationNumber();
		if (latestGenerationNumber > 0) {
			String generationFilePath = getGenerationFileName(latestGenerationNumber);
			String json;
			try {
				json = FileUtils.readFileToString(new File(generationFilePath), Charsets.UTF_8);
				return gson.fromJson(json, Generation.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static int getLatestGenerationNumber() {
		IOFileFilter fileFilter = new IOFileFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg0.getName().contains(generationFileName);
			}

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().contains(generationFileName);
			}
		};

		Collection<File> generationDirs = FileUtils.listFiles(new File(baseFilePath), fileFilter, null);

		int latestGenerationNumber = 0;
		for (File f : generationDirs) {
			int generationNumber = getNumberFromFileName(f.getName());
			if (generationNumber > latestGenerationNumber)
				latestGenerationNumber = generationNumber;
		}
		return latestGenerationNumber;
	}

	/**
	 * Extracts the generation number from a filename
	 */
	private static int getNumberFromFileName(String fileName) {
		Pattern p = Pattern.compile("([0-9]+)");
		Matcher m = p.matcher(new StringBuilder(fileName).reverse());
		if (m.find()) {
			return Integer.parseInt(new StringBuilder(m.group(1)).reverse().toString());
		}
		return 0;
	}

	private static String getGenerationFileName(int generationNumber) {
		return baseFilePath + fileSeparator + generationFileName + generationNumber + fileExtension;
	}
}