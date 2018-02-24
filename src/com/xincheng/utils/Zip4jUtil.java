package com.xincheng.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Zip4jUtil {

	/**
	 * 简单操作，不考虑压缩文件夹，以后再扩充
	 * */
	public static void zip(String destFile, ArrayList<String> sourceFiles, boolean deleteAfterCompress) {

		try {
			File existsFile = new File(destFile);
			if (existsFile.exists()) {
				existsFile.delete();
			}

			ZipFile zipFile = new ZipFile(destFile);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			ArrayList<File> tempFiles = new ArrayList<File>();
			ArrayList<File> directors = new ArrayList<File>();
			for (String filePath : sourceFiles) {
				File sourceFile = new File(filePath);
				if (sourceFile.exists()) {
					if (sourceFile.isFile()) {
						tempFiles.add(sourceFile);
					}

					if (sourceFile.isDirectory()) {
						directors.add(sourceFile);
						zipFile.addFolder(sourceFile, parameters);
					}
				}
			}

			if (tempFiles.size() > 0) {
				zipFile.addFiles(tempFiles, parameters);
			}

			if (deleteAfterCompress) {
				for (File sourceFile : tempFiles) {
					if (!sourceFile.exists())
						continue;

					if (sourceFile.isFile()) {
						sourceFile.delete();
					}
				}
				
				for (File sourceFile : directors) {
					if (!sourceFile.exists())
						continue;
					
					if (sourceFile.isDirectory()) {
						try {
							FileUtils.deleteDirectory(sourceFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ArrayList<String> files = new ArrayList<String>();
		files.add("d:\\d");
		zip("d:\\s0000000000.zip", files, true);
	}
}
