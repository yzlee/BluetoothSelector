package cc.liyongzhi.bluetoothselectordemo;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothFile {
	private String filename = null;
	private String dir = null;
	private String absolutePath = null;
	private String SDPath = null;

	public BluetoothFile(String SDPath, String dir, String name) {
		this.dir = dir;
		this.filename = name;
		this.absolutePath = SDPath + dir;
	}

	public long getCreateTime() {
		File file = new File(absolutePath + File.separator + filename);
		return file.lastModified();
	}
	// check if the file is exists on sdcard
	public boolean checkFileExists() {
		File file = new File(absolutePath + File.separator + filename);
		return file.exists();
	}
	
	public boolean checkDirExists() {
		File file = new File(absolutePath);
		return file.exists();
	}

	// create directory on sd card
	public File createDIR() {
		File dir = new File(absolutePath);
		dir.mkdir();
		return dir;
	}

	// create file on sd card
	public File createFile() throws IOException {
		File file = new File(absolutePath);
		file.createNewFile();
		return file;
	}

	// write input stream on sd card
	public File writeStreamToSDCard(String dirpath, String filename, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			// create directory
			createDIR();
			// create file under of directory
			file = createFile();
			output = new FileOutputStream(file);
			byte[] bt = new byte[4 * 1024];
			while (input.read(bt) != -1) {
				output.write(bt);
			}
			// flush cache
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return file;

	}

	// check if the sd card is exist on machine
	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}



	public String readFile() {
		File file = null;
		BufferedReader br = null;
		StringBuffer sb = null;
		try {
			file = new File(SDPath + dir, filename);
			br = new BufferedReader(new FileReader(file));
			String readline = "";
			sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				System.out.println("readline:" + readline);
				sb.append(readline);
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return sb.toString();
		}

	}

	// write binary data to file
	public boolean writeFileAppend(byte input[]) {
		try {
			File file = new File(Environment.getExternalStorageDirectory() + dir, filename);
			FileOutputStream fos = new FileOutputStream(file, true);
			fos.write(input);
			fos.flush();
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// write string to file
	public boolean writeFile(String input) {
		try {
			File file = new File(Environment.getExternalStorageDirectory() + dir, filename);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(input);
			bw.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// if the sum of the file exceed 6 delete the oldest
	public void dealFileExceeded() {

		File directory = new File(absolutePath);
		String[] list = directory.list();

		File oldest = null;
		if (list.length > 6) {
			int i = 0;
			for (String item : list) {
				File temp = new File(absolutePath + "/" + item);
				if (i == 0) {
					oldest = temp;
					i++;
					continue;
				}
				if (oldest.lastModified() >= temp.lastModified()) {
					oldest = temp;
				}

			}
			oldest.delete();
		}
	}

	// clear directory or file
	private boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(SDPath+ sPath);
		//
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	// realtime mode:write the bytes into file ,dealing with directory and files
	public void write(byte[] input) throws Exception {

		if (!checkDirExists()) {
			createDIR();
		}

		if (checkFileExists()) {
			createFile();
		}

		dealFileExceeded();
		writeFileAppend(input);

	}

	//创建异常文件输出流，覆盖方式
	public FileOutputStream getAbnomalWriteStream() throws IOException {
		if (!checkDirExists()) {
			createDIR();
		}

		if (!checkFileExists()) {
			createFile();
		}
		File file = new File(absolutePath, filename);
		FileOutputStream fos = new FileOutputStream(file);
		return fos;
	}
	
	//创建异常文件读入流
		public FileInputStream getAbnomalReadStream() throws IOException {
			if (!checkDirExists()) {
				return null;
			}

			if (!checkFileExists()) {
				return null;
			}
			File file = new File(absolutePath, filename);
			FileInputStream fis = new FileInputStream(file);
			return fis;
		}
	
	//创建单次文件输出流，覆盖方式
		public FileOutputStream getSingleWriteStream() throws IOException {
			if (!checkDirExists()) {
				createDIR();
			}

			if (!checkFileExists()) {
				createFile();
			}
			File file = new File(absolutePath, filename);
			FileOutputStream fos = new FileOutputStream(file);
			return fos;
		}

	//创建实时文件输出流，append方式，文件数多于6时，删除最先创建的
	public FileOutputStream getRealtimeWriteStream() throws IOException {
		if (!checkDirExists()) {
			createDIR();
		}
		if (!checkFileExists()) {
			createFile();
		}
		dealFileExceeded();
		File file = new File(absolutePath, filename);
		FileOutputStream fos = new FileOutputStream(file, true);

		return fos;

	}

	// write binary data to file
	public boolean writeFile(byte input[]) {
		try {
			File file = new File(Environment.getExternalStorageDirectory() + dir, filename);
			FileOutputStream fos = new FileOutputStream(file, true);
			fos.write(input);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delAllFile() {
		boolean flag = false;
		String path = Environment.getExternalStorageDirectory() + dir;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			temp = new File(path + File.separator + tempList[i]);
			if (temp.isFile()) {
				temp.delete();
			}
		}
		flag = true;
		return flag;
	}

}
