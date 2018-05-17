package com.dubbo.utils.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件操作工具类
 * 
 * @author Avan - 2009-12-01
 */
public class FileUtils {

	private static Log log = LogFactory.getLog(FileUtils.class);

	/**
	 * 创建文件夹
	 * 
	 * @param dir 目录路径
	 * @param ignoreIfExitst true表示如果文件夹存在就不再创建了,false是重新创建
	 * @throws IOException
	 */
	public static void createDirs(String dir, boolean ignoreIfExitst) throws IOException {
		File file = new File(dir);
		if (ignoreIfExitst && file.exists()) {
			return;
		}
		if (!file.mkdirs()) {
			throw new IOException("Cannot create directories = " + dir);
		}
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filePath 文件路径
	 * @throws IOException
	 */
	public static void deleteFile(String filePath) throws IOException {
		File file = new File(filePath);
		log.trace("Delete file = " + filePath);
		if (file.isDirectory()) {
			throw new IOException("IOException -> BadInputException: not a file.");
		}
		if (file.exists() == false) {
			throw new IOException("IOException -> BadInputException: file is not exist.");
		}
		if (file.delete() == false) {
			throw new IOException("Cannot delete file. filename = " + filePath);
		}
	}

	/**
	 * 删除文件夹及其下面的子文件夹
	 * 
	 * @param dir 目录路径
	 * @throws IOException
	 */
	public static void deleteDir(File dir) throws IOException {
		if (dir.isFile()) {
			throw new IOException("IOException -> BadInputException: not a directory.");
		}
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
		}// if
		dir.delete();
	}

	/**
	 * 获取到目录下面文件的大小。包含了子目录。
	 * 
	 * @param dir
	 * @return 文件大小
	 * @throws IOException
	 */
	public static long getDirLength(File dir) throws IOException {
		if (dir.isFile()) {
			throw new IOException("BadInputException: not a directory.");
		}
		long size = 0;
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				long length = 0;
				if (file.isFile()) {
					length = file.length();
				} else {
					length = getDirLength(file);
				}
				size += length;
			}// for
		}// if
		return size;
	}

	/**
	 * 将文件清空。
	 * 
	 * @param srcFilename
	 * @throws IOException
	 */
	public static void emptyFile(String srcFilename) throws IOException {
		File srcFile = new File(srcFilename);
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the file: " + srcFile.getAbsolutePath());
		}
		if (!srcFile.canWrite()) {
			throw new IOException("Cannot write the file: " + srcFile.getAbsolutePath());
		}

		FileOutputStream outputStream = new FileOutputStream(srcFilename);
		outputStream.close();
	}

	/**
	 * 写文件。如果此文件不存在就创建一个。
	 * 
	 * @param content String
	 * @param fileName String
	 * @param destEncoding String
	 * @param append
	 * @return fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String writeFile(String content, String fileName, String destEncoding, boolean append) throws FileNotFoundException, IOException {
		String _fileName = null;
		File file = new File(fileName);
		if (!file.exists()) {
			// 先创建文件夹，存在就忽略
			createDirs(file.getParent(), true);
			if (file.createNewFile() == false) {
				throw new IOException("create file '" + fileName + "' failure.");
			}
		}
		if (file.isFile() == false) {
			throw new IOException("'" + fileName + "' is not a file.");
		}
		if (file.canWrite() == false) {
			throw new IOException("'" + fileName + "' is a read-only file.");
		}
		_fileName = file.getName(); // 获取文件名

		BufferedWriter out = null;
		try {
			FileOutputStream fos = new FileOutputStream(fileName, append);
			out = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));
			out.write(content);
			out.flush();
		} catch (FileNotFoundException fe) {
			log.error("Error", fe);
			throw fe;
		} catch (IOException e) {
			log.error("Error", e);
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
			}
		}
		return _fileName;
	}

	/**
	 * 以字符为单位读取文件的内容，并将文件内容以字符串的形式返回。
	 * 
	 * @param filePath
	 * @return 字符串内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFileString(String filePath) throws FileNotFoundException, IOException {

		File file = new File(filePath);
		if (file.isFile() == false) {
			throw new IOException("'" + filePath + "' is not a file.");
		}

		BufferedReader reader = null;
		try {
			StringBuffer result = new StringBuffer(1024);
			FileInputStream fis = new FileInputStream(filePath);
			reader = new BufferedReader(new InputStreamReader(fis, getCharset(file)));

			char[] block = new char[512];
			while (true) {
				int readLength = reader.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				result.append(block, 0, readLength);
			}
			return result.toString();
		} catch (FileNotFoundException fe) {
			log.error("Error", fe);
			throw fe;
		} catch (IOException e) {
			log.error("Error", e);
			throw e;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 以行为单位读取文件的内容，并将文件内容以字符串的形式返回。
	 * 
	 * @param filePath
	 * @return 字符串内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFileLine(String filePath) throws FileNotFoundException, IOException {

		File file = new File(filePath);
		if (file.isFile() == false) {
			throw new IOException("'" + filePath + "' is not a file.");
		}

		BufferedReader reader = null;
		try {
			StringBuffer result = new StringBuffer(1024);
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis, getCharset(file)));
			String line = null;
			// 一次读入一行，直到读入null为文件结束
			while ((line = reader.readLine()) != null) {
				// 显示行号
				result.append(line).append("\r\n");
			}
			return result.toString();
		} catch (FileNotFoundException fe) {
			log.error("Error", fe);
			throw fe;
		} catch (IOException e) {
			log.error("Error", e);
			throw e;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	/*
	 * 1 ABC 2 abC Gia su doc tu dong 1 lay ca thay 5 dong => 1 --> 5 3 ABC
	 */
	public static String[] getLastLines(File file, int linesToReturn) throws IOException, FileNotFoundException {

		final int AVERAGE_CHARS_PER_LINE = 250;
		final int BYTES_PER_CHAR = 2;

		RandomAccessFile randomAccessFile = null;
		StringBuffer buffer = new StringBuffer(linesToReturn * AVERAGE_CHARS_PER_LINE);
		int lineTotal = 0;
		try {
			randomAccessFile = new RandomAccessFile(file, "r");
			long byteTotal = randomAccessFile.length();
			long byteEstimateToRead = linesToReturn * AVERAGE_CHARS_PER_LINE * BYTES_PER_CHAR;

			long offset = byteTotal - byteEstimateToRead;
			if (offset < 0) {
				offset = 0;
			}

			randomAccessFile.seek(offset);
			// log.debug("SKIP IS ::" + offset);

			String line = null;
			String lineUTF8 = null;
			while ((line = randomAccessFile.readLine()) != null) {
				lineUTF8 = new String(line.getBytes("ISO8859_1"), "UTF-8");
				lineTotal++;
				buffer.append(lineUTF8).append("\n");
			}
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException ex) {
				}
			}
		}

		String[] resultLines = new String[linesToReturn];
		BufferedReader in = null;
		try {
			in = new BufferedReader(new StringReader(buffer.toString()));

			int start = lineTotal /* + 2 */- linesToReturn; // Ex : 55 - 10 = 45
			// ~ offset
			if (start < 0) {
				start = 0; // not start line
			}
			for (int i = 0; i < start; i++) {
				in.readLine(); // loop until the offset. Ex: loop 0, 1 ~~ 2
				// lines
			}

			int i = 0;
			String line = null;
			while ((line = in.readLine()) != null) {
				resultLines[i] = line;
				i++;
			}
		} catch (IOException ie) {
			log.error("Error" + ie);
			throw ie;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
				}
			}
		}
		return resultLines;
	}

	/**
	 * 单个文件拷贝(路径方式)
	 * 
	 * @param srcFilename
	 * @param destFilename
	 * @param overwrite 是否覆盖目标文件
	 * @throws IOException
	 */
	public static void copyFile(String srcFilename, String destFilename, boolean overwrite) throws IOException {

		File srcFile = new File(srcFilename);
		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
		}

		File destFile = new File(destFilename);

		if (!overwrite) {
			// 目标文件存在就不覆盖
			if (destFile.exists()) {
				return;
			}
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写。
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			} else {
				// 先创建文件夹，存在就忽略
				createDirs(destFile.getParent(), true);
				// 文件不存在就创建一个新的空文件。
				if (!destFile.createNewFile()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			}
		}

		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		byte[] block = new byte[1024];
		try {
			inputStream = new BufferedInputStream(new FileInputStream(srcFile));
			outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				outputStream.write(block, 0, readLength);
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 单个文件拷贝(文件方式)
	 * 
	 * @param srcFile
	 * @param destFile
	 * @param overwrite 是否覆盖目标文件
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile, boolean overwrite) throws IOException {

		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
		}

		if (!overwrite) {
			// 目标文件存在就不覆盖
			if (destFile.exists()) {
				return;
			}
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写。
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			} else {
				// 先创建文件夹，存在就忽略
				createDirs(destFile.getParent(), true);
				// 文件不存在就创建一个新的空文件。
				if (!destFile.createNewFile()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			}
		}

		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		byte[] block = new byte[1024];
		try {
			inputStream = new BufferedInputStream(new FileInputStream(srcFile));
			outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				outputStream.write(block, 0, readLength);
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 单个文件拷贝(文件方式)
	 * 
	 * @param inputStream
	 * @param destFilename
	 * @param overwrite 是否覆盖目标文件
	 * @throws IOException
	 */
	public static void copyFile(InputStream inputStream, String destFilename, boolean overwrite) throws IOException {

		File destFile = new File(destFilename);

		if (!overwrite) {
			// 目标文件存在就不覆盖
			if (destFile.exists()) {
				return;
			}
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写。
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			} else {
				// 先创建文件夹，存在就忽略
				createDirs(destFile.getParent(), true);
				// 文件不存在就创建一个新的空文件。
				if (!destFile.createNewFile()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			}
		}

		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream outputStream = null;
		byte[] block = new byte[1024];
		try {
			bufferedInputStream = new BufferedInputStream(inputStream);
			outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
			while (true) {
				int readLength = bufferedInputStream.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				outputStream.write(block, 0, readLength);
			}
		} finally {
			if (bufferedInputStream != null) {
				try {
					bufferedInputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 拷贝文件，从源文件夹拷贝文件到目标文件夹。 <br>
	 * 参数源文件夹和目标文件夹，最后都不要带文件路径符号，例如：C:/Path正确，C:/Path/错误。
	 * 
	 * @param srcDirName 源文件夹名称 ,例如：C:/Path/file 或者C:\\Path\\file
	 * @param destDirName 目标文件夹名称,例如：C:/Path/file 或者C:\\Path\\file
	 * @param overwrite 是否覆盖目标文件夹下面的文件。
	 * @throws IOException
	 */
	public static void copyFiles(String srcDirName, String destDirName, boolean overwrite) throws IOException {
		File srcDir = new File(srcDirName);// 声明源文件夹
		// 首先判断源文件夹是否存在
		if (!srcDir.exists()) {
			throw new FileNotFoundException("Cannot find the source directory: " + srcDir.getAbsolutePath());
		}

		File destDir = new File(destDirName);
		if (overwrite == false) {
			if (destDir.exists()) {
				// do nothing
			} else {
				if (destDir.mkdirs() == false) {
					throw new IOException("Cannot create the destination directories = " + destDir);
				}
			}
		} else {
			// 覆盖存在的目标文件夹
			if (destDir.exists()) {
				// do nothing
			} else {
				// create a new directory
				if (destDir.mkdirs() == false) {
					throw new IOException("Cannot create the destination directories = " + destDir);
				}
			}
		}

		// 循环查找源文件夹目录下面的文件（屏蔽子文件夹），然后将其拷贝到指定的目标文件夹下面。
		File[] srcFiles = srcDir.listFiles();
		if (srcFiles == null || srcFiles.length < 1) {
			// throw new IOException ("Cannot find any file from source
			// directory!!!");
			return;// do nothing
		}

		// 开始复制文件
		int SRCLEN = srcFiles.length;

		for (int i = 0; i < SRCLEN; i++) {
			// File tempSrcFile = srcFiles[i];

			File destFile = new File(destDirName + File.separator + srcFiles[i].getName());
			// 注意构造文件对象时候，文件名字符串中不能包含文件路径分隔符";".
			// log.debug(destFile);
			if (srcFiles[i].isFile()) {
				copyFile(srcFiles[i], destFile, overwrite);
			} else {
				// 在这里进行递归调用，就可以实现子文件夹的拷贝
				copyFiles(srcFiles[i].getAbsolutePath(), destDirName + File.separator + srcFiles[i].getName(), overwrite);
			}
		}
	}

	/**
	 * 压缩单个文件
	 * 
	 * @param srcFilename
	 * @param destFilename
	 * @param overwrite
	 * @throws IOException
	 */
	public static void zipFile(String srcFilename, String destFilename, boolean overwrite) throws IOException {

		File srcFile = new File(srcFilename);
		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
		}

		if (destFilename == null || destFilename.trim().equals("")) {
			destFilename = srcFilename + ".zip";
		} else {
			destFilename += ".zip";
		}
		File destFile = new File(destFilename);

		if (overwrite == false) {
			// 目标文件存在就不覆盖
			if (destFile.exists()) {
				return;
			}
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写。
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			} else {
				// 不存在就创建一个新的空文件。
				if (!destFile.createNewFile()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			}
		}

		BufferedInputStream inputStream = null;
		ZipOutputStream zipOutputStream = null;
		byte[] block = new byte[1024];
		try {
			inputStream = new BufferedInputStream(new FileInputStream(srcFile));
			zipOutputStream = new ZipOutputStream(new FileOutputStream(destFile));

			zipOutputStream.setComment("通过java程序压缩的");
			ZipEntry zipEntry = new ZipEntry(srcFile.getName());
			zipEntry.setComment("zipEntry通过java程序压缩的");
			zipOutputStream.putNextEntry(zipEntry);
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				zipOutputStream.write(block, 0, readLength);
			}
			zipOutputStream.flush();
			zipOutputStream.finish();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (zipOutputStream != null) {
				try {
					zipOutputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据文件得到该文件中文本内容的编码
	 * 
	 * @param file 要分析的文件
	 * @return 文件编码
	 */
	public static String getCharset(File file) {
		String charset = "GBK"; // 默认编码
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset;
			}
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				while ((read = bis.read()) != -1) {
					if (read >= 0xF0) {
						break;
					}
					// 单独出现BF以下的，也算是GBK
					if (0x80 <= read && read <= 0xBF) {
						break;
					}
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							// (0x80 -
							// 0xBF),也可能在GB编码内
							continue;
						} else {
							break;
							// 也有可能出错，但是几率较小
						}
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	/**
	 * 取得文件扩展名(包含.分隔符)
	 * 
	 * @param fileName
	 * @return String
	 */
	public static String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
	}

	/**
	 * 取得文件扩展名(不包含.分隔符)
	 * 
	 * @param fileName
	 * @return String
	 */
	public static String getExtensionNotDot(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
	}

	/**
	 * 文件类型的判断
	 * 
	 * @param fileName
	 * @return String
	 */
	public static String getFileType(String fileName) {
		String fileType = null;
		fileName = fileName.toLowerCase(); // 转为小写
		if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".gif") || fileName.endsWith(".png") || fileName.endsWith(".bmp")) {
			fileType = "picture";
		} else if (fileName.endsWith(".flv") || fileName.endsWith(".swf")) {
			fileType = "flash";
		} else if (fileName.endsWith(".mp3") || fileName.endsWith(".wma")) {
			fileType = "audio";
		} else if (fileName.endsWith(".3gp") || fileName.endsWith(".mov") || fileName.endsWith(".mp4") || fileName.endsWith(".wmv") || fileName.endsWith(".avi") || fileName.endsWith(".mpg") || fileName.endsWith(".rm") || fileName.endsWith(".rmvb")) {
			fileType = "video";
		} else {
			fileType = "file";
		}
		return fileType;
	}

	/**
	 * 根据文件路径字符串提取文件名称
	 * 
	 * @param filePath 文件路径
	 * @return fileName 文件名
	 */
	public static String getFileName(String filePath) {
		String temp[] = filePath.replaceAll("\\\\", "/").split("/");
		String fileName = null;
		if (temp.length > 0) {
			fileName = temp[temp.length - 1];
		}
		return fileName;
	}

	/**
	 * 根据文件路径字符串提取不包含扩展名的文件名称
	 * 
	 * @param filePath 文件路径
	 * @return fileName 文件名
	 */
	public static String getFileNameNoSuffix(String filePath) {
		String temp[] = filePath.replaceAll("\\\\", "/").split("/");
		String fileName = null;
		if (temp.length > 0) {
			fileName = temp[temp.length - 1].replace(getExtension(temp[temp.length - 1]), "");
		}
		return fileName;
	}

	/**
	 * 根据文件路径字符串提取文件(或文件夹)的父级路径
	 * 
	 * @param filePath 文件路径
	 * @return fileName 文件名
	 */
	public static String getParentPath(String filePath) {
		String[] temp = filePath.replaceAll("\\\\", "/").split("/");
		StringBuilder paths = new StringBuilder();
		if (temp.length > 0) {
			for (int i = 0; i < temp.length - 1; i++) {
				paths.append(temp[i]).append(File.separator);
			}
		}
		return paths.toString();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath 文件路径
	 * @return 判断结果
	 * @throws IOException
	 */
	public static boolean isExist(String filePath) throws IOException {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

	}
}
