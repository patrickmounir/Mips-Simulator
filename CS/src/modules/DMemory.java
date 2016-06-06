package modules;

public class DMemory {
	private int [] data;
	public DMemory(){
		data=new int[65536];
	}
	public int read(int index){
		return data[index];
	}
	public void write(int index,int data){
		this.data[index]=data;
	}
	public String toString(int beginning, int end) {
		// TODO Auto-generated method stub
		String result="";
		for(int i=beginning;i<=end;i++){
			result+=i+": "+data[i]+"\n";
		}
		return result;
	}
}
