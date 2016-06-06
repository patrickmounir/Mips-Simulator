package modules;

import java.util.Arrays;

public class RegFile {
	private int [] regs;
	public RegFile(){
		regs=new int[32];
	}
	public void writeReg(int index, int data){
		if(index!=0){
			regs[index]=data;
		}
	}
	public int readReg(int index){
		return regs[index];
	}
	public String toString(){
		return Arrays.toString(regs);
	}
	public void fill(){
		Arrays.fill(regs, 2);
		regs[0]=0;
		regs[1]=20;
		regs[31]=1;
	}
}
