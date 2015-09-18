package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import network.Network;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.google.gson.Gson;

import evolution.Generation;

public class IOManager {
	private static String fileSeparator = System.getProperty("file.separator");
	
	private static String generationFilePath = "generation";
	private static String baseFilePath = "networks";
	
	private static String networkFileName = "network";
	private static String generationFileName = "generation";
	private static String fileExtension = ".json";
	private static Gson gson = new Gson();
	
	public static void saveGenerationToFile(Generation generation, int generationNumber) {
		String json = gson.toJson(generation);
		try {
			FileUtils.write(new File(getGenerationFileName(generationNumber)), json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static void saveGenomeToFile(Genome genome, int generationNumber) {
//		saveGenomeToFile(genome, generationNumber, 1);
//	}
	
//	public static void saveGenomeToFile(Genome genome, int generationNumber, int genomeNumber) {
//		String json = gson.toJson(genome);
//		try {
//			FileUtils.write(new File(getGenomeFileName(generationNumber, genomeNumber)), json);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
//	public static void saveMultipleGenomesToFile(int generationNumber, List<Genome> genomes) {
//		int genomeNumber = 1;
//		for(Genome g: genomes) {
//			saveGenomeToFile(g, generationNumber, genomeNumber);
//			genomeNumber++;
//		}
//	}
	
	public static void saveToFile(List<Network> networks, int generationNumber) {
		int networkNumber = 1;
		for(Network network : networks) {
			saveToFile(network, generationNumber, networkNumber);
			networkNumber++;
		}
	}
	
	public static void saveToFile(Network network, int generationNumber, int networkNumber) {
		String json = gson.toJson(network);
		try {
			FileUtils.write(new File(getNetworkFileName(generationNumber, networkNumber)), json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static List<Genome> readMultipleGenomes(int generationNumber) {
//		List<Genome> result = new ArrayList<Genome>();
//		
//		int lastGenomeNumber = getLatestGenomeNumber(generationNumber);
//		for(int i = 1; i < lastGenomeNumber+1; i++) {
//			result.add(readGenome(generationNumber, i));
//		}
//		
//		return result;
//	}
	
//	public static Genome readGenome(int generationNumber, int genomeNumber) {
//		String jsonAsString = null;
//		try {
//			jsonAsString = FileUtils.readFileToString(new File(getGenomeFileName(generationNumber, genomeNumber)));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return gson.fromJson(jsonAsString, Genome.class);
//	}
	
//	public static List<Genome> readGenomesFromLatestGeneration() {
//		return readMultipleGenomes(getLatestGenerationNumber());
//	}
	
	public static List<Network> readMultipleNetworks(int generationNumber) {
		List<Network> result = new ArrayList<Network>();
		
		int lastNetworkNumber = getLatestNetworkNumber(generationNumber);
		for(int i = 1; i < lastNetworkNumber+1; i++) {
			result.add(readNetwork(generationNumber, i));
		}
		return result;
	}
	
	public static Network readNetwork(int generationNumber, int genomeNumber) {
		String jsonAsString = null;
		
		try {
			jsonAsString = FileUtils.readFileToString(new File(getNetworkFileName(generationNumber, genomeNumber)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return gson.fromJson(jsonAsString, Network.class);
		
	}
	
	public static List<Network> readNetworksFromLatestGeneration() {
		return readMultipleNetworks(getLatestGenerationNumber());
	}
	
	private static int getLatestNetworkNumber(int generationNumber) {
		IOFileFilter fileFilter = new IOFileFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg0.getName().contains(networkFileName);
			}

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().contains(networkFileName);
			}
		};
		
		Collection<File> genomeFiles = FileUtils.listFiles(new File(getGenerationFilePath(generationNumber)), fileFilter, FalseFileFilter.FALSE);
		
		int latestGenomeNumber = 0;
		for(File f: genomeFiles) {
			int genomeNumber = getNumberFromFileName(f.getName());
			if(genomeNumber > latestGenomeNumber)
				latestGenomeNumber = genomeNumber;
		}
		return latestGenomeNumber;
	}
	
	public static int getLatestGenerationNumber() {
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
		
		Collection<File> generationDirs = FileUtils.listFilesAndDirs(new File(baseFilePath), FalseFileFilter.FALSE, fileFilter);
		
		int latestGenerationNumber = 0;
		for(File f: generationDirs) {
			int generationNumber = getNumberFromFileName(f.getName());
			if(generationNumber > latestGenerationNumber)
				latestGenerationNumber = generationNumber;
		}
		return latestGenerationNumber;
	}
	
	private static int getNumberFromFileName(String fileName) {
		Pattern p = Pattern.compile("([0-9]+)");
	    Matcher m = p.matcher(new StringBuilder(fileName).reverse());
	    if(m.find()) {
	      return Integer.parseInt(new StringBuilder(m.group(1)).reverse().toString());
	    }
	    return 0;
	}
	
	private static String getNetworkFileName(int generationNumber, int genomeNumber) {
		return getGenerationFilePath(generationNumber) + fileSeparator + networkFileName + genomeNumber + fileExtension;
	}
	
	private static String getGenerationFileName(int generationNumber) {
		return getGenerationFilePath(generationNumber) + fileSeparator + generationFileName + fileExtension;
	}
	
	private static String getGenerationFilePath(int generationNumber) {
		return baseFilePath + fileSeparator + generationFilePath + generationNumber;
	}
}
