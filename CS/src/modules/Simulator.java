package modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Simulator {
	IMemory im;
	DMemory result;
	RegFile regs;
	HashMap<String,Object> reg1;
	HashMap<String,Object> reg2;
	HashMap<String,Object> reg3;
	HashMap<String,Object> reg4;
	boolean jump=false;
	PriorityQueue<pair>q=new PriorityQueue<pair>();
	int clk;
	public Simulator(int pc){
		im=new IMemory(pc);
		result= new DMemory();
		regs=new RegFile();
		reg1=new HashMap<String, Object>();
		reg2=new HashMap<String, Object>();
		reg3=new HashMap<String, Object>();
		reg4=new HashMap<String, Object>();
		clk=1;
		
	}
	public boolean IF(){
		int ins = im.getnextIns();
		int pc = im.getPC();
		if(ins==0){
			return false;
		}
		String instruction=ParseIt.ret32(ins);
		reg1.put("pcAdd",im.getPC());
		reg1.put("instr",instruction);// 3oda za3lan 
		//ID(instruction);
		return true;
	}
	public void ID(){
		String ins=(String)reg1.get("instr");
		boolean signals []=ControlUnit.controls(ins);
		String op=ins.substring(0,6);
		switch(op){
		case "000000":handelR(ins,signals);break;
		case "100011":handelLw(ins,signals);break;
		case "101011":handelSw(ins,signals);break;
		case "000100":handelBeq(ins,signals);break;
		case "000010":handelJ(ins,signals);break;
			default:System.out.println("Invalid Instruction!"+op);
			
		}
		
	}
	
	static int toDec(String num){
		
		int n=0;
		int v=1;
		if(num.charAt(0)=='0'||num.length()<7){
		
		for (int i=num.length()-1;i>=0;i--){
			n+=(num.charAt(i)-'0')*v;
			v*=2;
		}}
		else{String r = "";
			for(int i =0;i<num.length();i++){
				if(num.charAt(i)=='0'){
					r=r+"1";
				}else{
					r=r+"0";
				}
			}
			
			
			int d= toDec(r);
			n=-1*(d+1);
		}
		return n;
	}
	void fillreg2(int rs, int rt,String rd,int immidiate,boolean [] controls,int pc){
		// 3oda za3lan
		reg2.put("rs", rs);
		reg2.put("rt", rt);
		reg2.put("rd", rd);
		reg2.put("immidiate", immidiate);
		reg2.put("controls", controls);
		reg2.put("pc",pc);
		
		
	}
	private  void handelJ(String ins, boolean[] signals) {
		int pc=(Integer)reg1.get("pcAdd");
		fillreg2(0,0,null,toDec(ins.substring(6,ins.length())),signals,pc);
		
	}

	private  void handelBeq(String ins, boolean[] signals) {
		int pc=(Integer)reg1.get("pcAdd");
	   fillreg2(regs.readReg(toDec(ins.substring(6,11))),regs.readReg(toDec(ins.substring(11,16))),null,toDec(ins.substring(16,32)),signals,pc);
		
	}

	private  void handelSw(String ins, boolean[] signals) {
		int pc=(Integer)reg1.get("pcAdd");
		fillreg2(regs.readReg(toDec(ins.substring(6,11))),regs.readReg(toDec(ins.substring(11,16))),null,toDec(ins.substring(16,32)),signals,pc);
	}

	private  void handelLw(String ins, boolean[] signals) {
		int pc=(Integer)reg1.get("pcAdd");
		fillreg2(regs.readReg(toDec(ins.substring(6,11))),0,ins.substring(11,16),toDec(ins.substring(16,32)),signals,pc);
	}

	private  void handelR(String ins, boolean[] signals) {
		int pc=(Integer)reg1.get("pcAdd");
		fillreg2(regs.readReg(toDec(ins.substring(6,11))),regs.readReg(toDec(ins.substring(11,16))),ins.substring(16,16+5),0,signals,pc);
		
	}
	void edit(int st){
		ArrayList<pair> a =new ArrayList<pair>();
		while (!q.isEmpty()){
			pair p =q.poll();
			if (p.st>=st)a.add(p);
		}
		for (pair p : a){
			q.add(p);
		}
	}
	public void EXE(){
		int pc=(Integer)reg2.get("pc");
		int rs=(Integer)reg2.get("rs");
		int rt=(Integer)reg2.get("rt");
		String rd=(String)reg2.get("rd");
		int immidiate=(Integer)reg2.get("immidiate");
		boolean [] controls=(boolean [])reg2.get("controls");
		
		int aluoutput=0;
		int alusecondinput=0;
		if(controls[Controls.ALU_SRC.ordinal()]){
			alusecondinput=immidiate;
		}else{
			alusecondinput=rt;
		}
		if(controls[Controls.ADD.ordinal()]||controls[Controls.MEM_READ.ordinal()]||controls[Controls.MEM_WRITE.ordinal()]){
			aluoutput=alusecondinput+rs;
		}else{
			if(controls[Controls.OR.ordinal()]){
				aluoutput=rs|alusecondinput;
			}else{
				if(controls[Controls.AND.ordinal()]){
					aluoutput=rs&alusecondinput;
				}else{
					if(controls[Controls.SUB.ordinal()]){
						aluoutput=rs-alusecondinput;
					}else{
						if(controls[Controls.SLT.ordinal()]){
							if(rs<rt){
								System.out.println(alusecondinput);
								aluoutput=1;
							}else{
								aluoutput=0;
							}
						}else{
							if(controls[Controls.BRANCH.ordinal()]){
								if(rs==alusecondinput){
									im.setPC(pc+immidiate);
									edit(3);
									jump=true;
									
								}
							}else{
								if(controls[Controls.JUMP.ordinal()]){
									im.setPC(immidiate);
									edit(3);
									jump=true;
								}
							}
						}
					}
				}
			}
		}
		int rd2=0;
		if(rd!=null){
		rd2=toDec(rd);
		}
		reg3.put("rt",rt);
		reg3.put("aluoutput",aluoutput);
		reg3.put("rd2",rd2);
		reg3.put("controls",controls);
		
	}
	
	public void MEM(){
		int data = (Integer)reg3.get("rt");
		int aluout = (Integer)reg3.get("aluoutput");
		int rd = (Integer)reg3.get("rd2");
		boolean[]control = 	(boolean [])reg3.get("controls");
		int x=0;
		
		//Memory read (load ins)
		
		if(control[4]==true){
			x = result.read(aluout);
		}
		else{
			//Memory write (store)
			if(control[11]==true){
				result.write(aluout, data);
			}
		}
		//Writeback(control,x,aluout,rd);
		
		reg4.put("control", control);
		reg4.put("x", x);
		reg4.put("aluout", aluout);
		reg4.put("rd", rd);
		
	}
	public void Writeback() {
		
		boolean[] controls = (boolean [])reg4.get("control");
		int memout = (Integer)reg4.get("x");
		int aluout = (Integer)reg4.get("aluout");
		int rd = (Integer)reg4.get("rd");
		
		int r=0;
		if(controls[Controls.MEM_TO_REG.ordinal()]){
			r = memout;
		}else{
			r = aluout;
		}
		if(controls[Controls.REG_WRITE.ordinal()]){
			regs.writeReg(rd, r);
		}
		
	}
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner =new Scanner(System.in);
		System.out.println("Enter the start Instruction memory:");
		int pc = scanner.nextInt();
		Simulator sim = new Simulator(pc);
		ArrayList<String> insSet=ParseIt.parIns(pc);
		sim.seedIns(insSet);
		sim.im.setPC(pc);
		System.out.println("Enter the start and end of the data memory:");
		int beginning = scanner.nextInt();
		int end = scanner.nextInt();
		Scanner data  = new Scanner(new File("data.csv"));
		while(data.hasNext()){
			String in=data.nextLine();
			String [] input = in.split(" ");
			sim.result.write(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
		}
		sim.regs.fill();
		int k=0;
		sim.q.add(new pair(k,1));
		
		while(!sim.q.isEmpty()){
			System.out.println("Clock: "+sim.clk);
//			System.out.println(sim.im.getPC());
//			if(!sim.IF())
//				break;
//			
			sim.clk++;
			int sz=sim.q.size();
			ArrayList<pair>a=new ArrayList<pair>();
			while(!sim.q.isEmpty()){
				pair p=sim.q.poll();
				switch (p.st){
				case 1: 
					if (sim.IF()){
						System.out.print("The Insturction fetch of instruction number "+p.id);
					p.st++;
					a.add(p);
					}
					break;
				case 2:
					sim.ID();
					System.out.print("The Insturction decoding of instruction number "+p.id+",");
					p.st++;
					a.add(p);
					break;
				case 3:
					sim.EXE();
					System.out.print("The Insturction execution of instruction number "+p.id+",");
					p.st++;
					a.add(p);
					break;
				case 4:
					sim.MEM();
					System.out.print("The Insturction memory of instruction number "+p.id+",");
					p.st++;
					a.add(p);
					break;
					default:
						sim.Writeback();
						System.out.print("The Insturction write back of instruction number "+p.id+",");
						break;
				}
			}
			System.out.println();
			int min=10000;
			for (pair p: a){
				sim.q.add(p);
				min=Math.min(min,p.st);
			}
			if (min==2 || sim.jump==true){
				sim.q.add(new pair(++k, 1));
				sim.jump=false;
			}
			
		}
		System.out.println(sim.regs.toString());
		System.out.println(sim.result.toString(beginning,end));
	}
	private void seedIns(ArrayList<String> insSet) {
		// TODO Auto-generated method stub
		for(String ins :insSet){
//			System.out.println(toDec(ins));
			im.writeIns(toDec(ins));
			
		}
	}

	
}
class pair implements Comparable<pair> {
   int id,st;
   pair(int id,int st){
	   this.id=id;
	   this.st=st;
   }
	public int compareTo(pair arg0) {
		return arg0.st-st;
	}
	
}
