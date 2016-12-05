/*
 * Henrik Thulin heth7132
 * Mattin Lotfi malo5163
 */

package HuffClass;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HuffTree {

	private int bufferSize = 1024;
	
	public HuffTree(int bufferSize) {
		
		this.bufferSize = bufferSize;
	}
	
	private HuffTreeNode GetNode(HashMap<Integer, Integer> count) {

		ArrayList<Tuple<Integer, Object>> work = new ArrayList<>();

		for (Map.Entry<Integer, Integer> e : count.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toList())) {

			work.add(new Tuple<Integer, Object>(e.getValue(), e.getKey()));
		}

		System.out.println(count);

		if (work.size() == 0)
			return new HuffTreeNode(null,0,null,0);
		
		if (work.size() == 1) 
			return new HuffTreeNode(work.get(0).getT2(), work.get(0).getT1(), null, 0);		

		while (work.size() > 1) {

			Tuple<Integer, Object> a = work.get(0);
			Tuple<Integer, Object> b = work.get(1);

			work.remove(0);
			work.remove(0);

			HuffTreeNode tempht = new HuffTreeNode(a.getT2(), a.getT1(), b.getT2(), b.getT1());

			for (int i = 0; i <= work.size(); i++)
				if (i == work.size() || tempht.GetFrequency() < work.get(i).getT1()) {
					work.add(i, new Tuple<Integer, Object>(tempht.GetFrequency(), tempht));
					break;
				}

			System.out.println(work.toString());
		}

		return (HuffTreeNode) work.get(0).getT2();
	}

	public boolean Decompress(String sourceFile, String destinationFile) {

		HashMap<Integer,Tuple<Integer, Integer>> treeMap = new HashMap<>();		
		
		FileInputStream in = null;
		FileOutputStream out = null;
		byte[] readBuffer = new byte[bufferSize];
		byte[] writeBuffer = new byte[bufferSize];		
		int readSize = 0;
		int writeSize = 0;
		
		try {
			in = new FileInputStream(sourceFile);
			
			DataInputStream dis = new DataInputStream(new BufferedInputStream(in));
			
			out = new FileOutputStream(destinationFile);

			int count, characters;
			
			System.out.print("Generating tree...");
			
			try {
				count = (dis.read() << 24) | (dis.read() << 16) | (dis.read() << 8) | dis.read();
				characters = (dis.read() << 24) | (dis.read() << 16) | (dis.read() << 8) | dis.read();

				System.out.println("Symbols: " + count);
				System.out.println("Characters: " + characters);

				while (count-- > 0) {

					Integer key = dis.read();
					int code = ((dis.read() << 24) | (dis.read() << 16) | (dis.read() << 8) | (dis.read()));
					int length = dis.read();

					Byte[] b = new Byte[length];
					
					/*String str = "";
					
					for (int i = 0; i < length; i++)
						str = (((code >> i) & 0x01) > 0 ? "1" : "0") + str;*/										
					
					treeMap.put(code | (length << 24), new Tuple<Integer, Integer>(key, length));
				}

				System.out.println("done");
				
				//System.out.println("Read map: " + treeMap);

				System.out.println("Treemap size: "+treeMap.size());
				
				//int c;

				int curByte = 0;
				String curString = "";
				int curByteLength = 0;

				int charsWritten = 0;
				
				System.out.println("Writing "+destinationFile+"...");
				
				while ((readSize = dis.read(readBuffer, 0, readBuffer.length)) > 0) {
				
				//while (((c = in.read()) != -1)) {
					
					for (int b = 0; b < readSize; b++) 

					for (int i = 0; i < 8 && charsWritten < characters; i++) {

						int bit = ((readBuffer[b]) >> (7 - i)) & 0x01;
												
						//curString += bit > 0 ? "1" : "0";
						
						curByte = (curByte << 1) | bit;						
						curByteLength++;
										
						Tuple<Integer, Integer> find = treeMap.get(curByte | (curByteLength << 24));

						if (find != null) {

							//System.out.println(Character.toChars(find.getT1())[0] + " = " + find.getT1());
							
							writeBuffer[writeSize++] = find.getT1().byteValue();
							
							if (writeSize >= writeBuffer.length) {																
								out.write(writeBuffer, 0, writeSize);
								writeSize = 0;
							}
							
							//out.write(find.getT1());
							charsWritten++;

							curByte = 0;
							curString = "";
							curByteLength = 0;
						}
						

						// System.out.print((c >> (7 - i)) & 0x01);
					}				
				}
				
				if (writeSize > 0) {																
					out.write(writeBuffer, 0, writeSize);
					writeSize = 0;
				}
				
				System.out.println("done");

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
			try {
				if (in != null)
					in.close();

				if (out != null) {
					dis.close();
					out.flush();
					out.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean Compress(String sourceFile, String destinationFile) {

		HashMap<Integer, Tuple<Integer, Integer>> treeMap = new HashMap<>();
		DataInputStream dis = null;
		FileInputStream in = null;
		FileOutputStream out = null;
		FileChannel fc = null;
				
		byte[] readBuffer = new byte[bufferSize];
		byte[] writeBuffer = new byte[bufferSize];
		
		int readSize = 0;
		int writeSize = 0;
		
		HashMap<Integer, Integer> count = new HashMap<>();

		try {
			try {
				in = new FileInputStream(sourceFile);			
				dis = new DataInputStream(new BufferedInputStream(in));
				out = new FileOutputStream(destinationFile);
				fc = in.getChannel();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

			
			try {

				int characters = 0;

				System.out.print("Generating tree...");
				
				while ((readSize = dis.read(readBuffer,  0,  readBuffer.length)) > 0) {
					
					for (int b = 0; b < readSize; b++)
					{
						int tb = readBuffer[b] & 0xff;
						
						count.put(tb, count.getOrDefault(tb, 0) + 1);											
					}
					
					characters += readSize;						
				}
				
				fc.position(0);

				

				HuffTreeNode node = GetNode(count);
		
				for (Map.Entry<Integer, Integer> e : count.entrySet()) {
					treeMap.put(e.getKey(),
							new Tuple<Integer, Integer>(node.GetBinary(e.getKey()), node.GetDepth(e.getKey())));

					//System.out.println(e.getKey().byteValue() + " " + node.GetBinaryString(e.getKey()) + " "
					//		+  node.GetBinary(e.getKey()) + " " + node.GetDepth(e.getKey()));
				}

				System.out.println("done");
				
				int pos = 0;
				int outByte = 0x00;

				out.write((treeMap.size() >> 24) & 0xff);
				out.write((treeMap.size() >> 16) & 0xff);
				out.write((treeMap.size() >> 8) & 0xff);
				out.write((treeMap.size()) & 0xff);

				out.write((characters >> 24) & 0xff);
				out.write((characters >> 16) & 0xff);
				out.write((characters >> 8) & 0xff);
				out.write((characters) & 0xff);

				System.out.println("keys size: "+treeMap.size());
				System.out.println("Characters: "+characters);				
				//System.out.println("Write map: " + treeMap);
				
				for (Map.Entry<Integer, Tuple<Integer, Integer>> e : treeMap.entrySet()) {

					out.write(e.getKey());
					
					out.write((e.getValue().getT1() >> 24) & 0xff);
					out.write((e.getValue().getT1() >> 16) & 0xff);
					out.write((e.getValue().getT1() >> 8) & 0xff);
					out.write((e.getValue().getT1()) & 0xff);
					
					out.write(e.getValue().getT2());													
				}

				System.out.print("Writing compressed data...");
				
				while ((readSize = dis.read(readBuffer, 0, readBuffer.length)) > 0) {

					for (int b = 0; b < readSize; b++) {

						Tuple<Integer, Integer> work = treeMap.get((readBuffer[b] & 0xff));
						
						for (int i = 0; i < work.getT2(); i++) {

							outByte = (outByte << 1) | ((work.getT1() >> (work.getT2() - i - 1)) & 0x01);

							pos++;

							if (pos > 7) {

								pos = 0;
								writeBuffer[writeSize++] = (byte)outByte;
								
								if (writeSize >= writeBuffer.length) {
																		
									out.write(writeBuffer, 0, writeSize);
									writeSize = 0;
								}
								
								//out.write(outByte);
								outByte = 0;
							}
						}
					}

				}
				
				System.out.println("done");

				if (pos != 0)
					writeBuffer[writeSize++] = (byte)(outByte << (8 - pos));
				
				if (writeSize > 0)
					out.write(writeBuffer, 0, writeSize);

				// System.out.println(treeMap);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} finally {

			try {
				if (in != null)
					in.close();

				if (out != null) {
					
					dis.close();
					out.flush();
					out.close();

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		try {
			long sf = Files.size(new File(sourceFile).toPath());
			long df = Files.size(new File(destinationFile).toPath());
			
			System.out.println("*** Source file: "+sf+" byte(s)");
			System.out.println("*** Destination file: "+df+" byte(s)");
			System.out.println("*** Size change: "+(sf-df)+" byte(s)");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;

	}

}