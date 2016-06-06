package modules;

public class IMemory extends DMemory {
	private int pc;
	public IMemory(int pc){
		super();
		this.pc=pc;
	}
	public int getnextIns(){
		return read(pc++);
	}
	public void setPC(int newPC){
		this.pc=newPC;
	}
	public int getPC(){
		return pc;
	}
	public void writeIns(int Ins){
		write(pc,Ins);
		pc++;
	}
}
