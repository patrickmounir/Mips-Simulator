package modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ParseIt {
// add R
// sub R
// lw
// sw
// and R
// or R
// beq
// j
// slt R
	public static ArrayList<String> parIns (int baseAdress) throws FileNotFoundException {
		
        Scanner scanner = new Scanner(new File("players.csv"));
        ArrayList<String> res = new ArrayList<String>();
        ArrayList<String> instructions = new ArrayList<String>();
        HashMap<String, Integer> labels= new HashMap<String, Integer>();
        int counter =1;
       
        while(scanner.hasNextLine()){
        	
        	String input = scanner.nextLine();
        	String[] splited = input.split(":");
        	if(splited.length>1){
        		labels.put(splited[0], counter);
        		instructions.add(splited[1]);
        	}else{
        		instructions.add(splited[0]);
        	}
        	counter++;
        }
        scanner.close();
        
        for(int i =0;i<instructions.size();i++) 
        {
        	String tmp = instructions.get(i).replace(' ', ',');
        	String tmpArr [] = tmp.split(",");
            String instruction = "";
//            System.out.println(tmpArr==null);
            if(tmpArr.length == 4){
            	if(tmpArr[0].equals("beq")){
            		instruction+=getCodeBin(tmpArr[0]);           	
                	instruction+=getCodeBin(tmpArr[1]);
                	instruction+=getCodeBin(tmpArr[2]);
                	int inst=labels.get(tmpArr[3]);
                	
                	instruction+=ret16((inst-(i+2)));
                	
            	}else{
            		String [] opFunc = getCodeBin(tmpArr[0]).split(",");
                	instruction+=opFunc[0];
                	instruction+= getCodeBin(tmpArr[2]);
                	instruction+= getCodeBin(tmpArr[3]);
                	instruction+= getCodeBin(tmpArr[1]);
                	instruction+="00000";
                	instruction+=opFunc[1];
            	}
            	
            	
            } else{ if(tmpArr.length == 3){
            	tmpArr[2]=tmpArr[2].replace('(',',');
            	String [] offset =tmpArr[2].split(",");
            	instruction+= getCodeBin(tmpArr[0]);
            	instruction+= getCodeBin(offset[1].substring(0,offset[1].length()-1));
            	instruction+= getCodeBin(tmpArr[1]);
            	instruction+= ret16(Integer.parseInt(offset[0]));
            }else{
            	if(tmpArr.length==2){
            		instruction+=getCodeBin(tmpArr[0]);
            		int inst=labels.get(tmpArr[1]);
            		instruction+=ret26((inst+baseAdress)-1);
            	}
            	
            }
            }
            
            res.add(instruction);
        }
         
          
        
        return res;
		
	}
	
	public static String ret16(int n) {
		String tmp ="";
		String s1="";
		if(n>=0){
			tmp="0000000000000000"+Integer.toBinaryString(n);
			s1= tmp.substring(tmp.length()-16,tmp.length());
		
		}else{ 
			s1=Integer.toBinaryString(n).substring(16, 32);
		}
		return s1;
	}
public static String ret26(int n) {
		String tmp ="";
		String s1="";
		if(n>=0){
			tmp="00000000000000000000000000"+Integer.toBinaryString(n);
			s1= tmp.substring(tmp.length()-26,tmp.length());
		
		}else{ 
			s1=Integer.toBinaryString(n).substring(6, 32);
		}
		return s1;
	}
public static String ret32(int n) {
	String tmp ="";
	String s1="";
	if(n>=0){
		tmp="00000000000000000000000000000000"+Integer.toBinaryString(n);
		s1= tmp.substring(tmp.length()-32,tmp.length());
	
	}else{ 
		s1=Integer.toBinaryString(n);
	}
	return s1;
}
	public static String getCodeBin(String s){
		String res = "";
		switch(s){
		case "add": res = "000000,100000";break;
		case "sub": res = "000000,100010";break;
		case "lw": res = "100011";break;
		case "sw": res = "101011";break;
		case "and": res = "000000,100100";break;
		case "or": res = "000000,100101";break;
		case "slt": res = "000000,101010";break;
		case "beq": res = "000100";break;
		case "j": res = "000010";break;
		case "$0": res = "00000";break;
		case "$at": res = "00001";break;
		case "$v0": res = "00010";break;
		case "$v1": res = "00011";break;
		case "$a0": res = "00100";break;
		case "$a1": res = "00101";break;
		case "$a2": res = "00110";break;
		case "$a3": res = "00111";break;
		case "$t0": res = "01000";break;
		case "$t1": res = "01001";break;
		case "$t2": res = "01010";break;
		case "$t3": res = "01011";break;
		case "$t4": res = "01100";break;
		case "$t5": res = "01101";break;
		case "$t6": res = "01110";break;
		case "$t7": res = "01111";break;
		case "$s0": res = "10000";break;
		case "$s1": res = "10001";break;
		case "$s2": res = "10010";break;
		case "$s3": res = "10011";break;
		case "$s4": res = "10100";break;
		case "$s5": res = "10101";break;
		case "$s6": res = "10110";break;
		case "$s7": res = "10111";break;
		case "$t8": res = "11000";break;
		case "$t9": res = "11001";break;
		case "$k0": res = "11010";break;
		case "$k1": res = "11011";break;
		case "$gp": res = "11100";break;
		case "$sp": res = "11101";break;
		case "$fp": res = "11110";break;
		case "$ra": res = "11111";break;
		default: System.out.println("COMPILATION ERROR");System.exit(0);
		}
		return res;
	}
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(parIns(3));
	}
	
}
