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
			Process ls = new ProcessBuilder("cmd","/c","dir").start();
			Process grep = new ProcessBuilder("cmd", "/c", "find", "\"@\"").start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(ls.getInputStream(), "UTF-8"));
			int lsCode = ls.waitFor();
			
			if(lsCode != 0){
				System.out.println("El programa ls se ejecutó con error. Saliendo...");
				System.exit(-1);
			}
			
			String linea = null;
			StringBuilder output = new StringBuilder();
			
			while ((linea = reader.readLine()) != null){
				output.append(linea+"\n");
			}
			
			
			reader.close();
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(grep.getOutputStream(), "UTF-8"));
			writer.write(output.toString());
			writer.flush();
			writer.close();
			
			int grepCode = grep.waitFor();
			
			if(grepCode != 0){
				System.out.println("El programa grep se ejecutó con error. Saliendo...");
				System.exit(-1);
			}
			
			reader = new BufferedReader(new InputStreamReader(grep.getInputStream(), "UTF-8"));
			
			while ((linea = reader.readLine())!=null){
				System.out.println(linea);
			}
			
			reader.close();
			
		} catch (IOException | InterruptedException e) {
			System.err.println("Error");
		}
		
	}

}
