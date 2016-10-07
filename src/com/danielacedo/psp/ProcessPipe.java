package com.danielacedo.psp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


public class ProcessPipe {

	public static void main(String[] args) {
		try {
			Process ls = new ProcessBuilder("ls","-la").start();
			Process grep = new ProcessBuilder("grep", "@").start();
			
			//Connect ls output to a reader and wait for it to finish
			BufferedReader readerA = new BufferedReader(new InputStreamReader(ls.getInputStream(), "UTF-8"));
			//Connect grep input to writer
			BufferedWriter writerB = new BufferedWriter(new OutputStreamWriter(grep.getOutputStream(), "UTF-8"));
			
			int lsCode = ls.waitFor();
			
			if(lsCode != 0){
				System.out.println("El programa ls se ejecutó con error. Saliendo...");
				System.exit(-1);
			}
			
			//Start reading ls output and write it to grep as it is being read
			String outputA = null;
			
			while ((outputA = readerA.readLine()) != null){
				writerB.write(outputA+"\n");
			}
			
			readerA.close();
			writerB.flush();
			writerB.close();
			
			int grepCode = grep.waitFor();
			
			if(grepCode != 0){
				System.out.println("El programa grep se ejecutó con error. Saliendo...");
				System.exit(-1);
			}
			
			//Read grep output to stdout
			readerA = new BufferedReader(new InputStreamReader(grep.getInputStream(), "UTF-8"));
			
			while ((outputA = readerA.readLine())!=null){
				System.out.println(outputA);
			}
			
			readerA.close();
			
		} catch (IOException | InterruptedException e) {
			System.err.println("Error");
		}
		
	}

}
