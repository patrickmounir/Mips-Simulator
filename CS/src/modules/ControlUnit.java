package modules;

public class ControlUnit {
	
	public static String intToBinary(int i){
		String result ="";
		while(i>0){
			result =i%2+result;
			i=i/2;
		}
		while(result.length()!=32){
			result = 0+result;
		}
		return  result;
	}
	public static int binaryToInt(String s){
		int result = 0;
		for(int i =s.length()-1;i>=0;i--){
			int index = s.charAt(i)-'0';
			result+=index*Math.pow(2, s.length()-1-i);
		}
		return result;
	}
	public static boolean[] controls(String ins){
		boolean [] controls = new boolean[13];
		
		String opcode = ins.substring(0, 6);
		switch(opcode){
		case "101011": controls[11]=true;
						controls[12]=true;
						break;
		case "100011": controls[12]=true;
					   controls[5]=true;
					  controls[4]=true;
					  controls[0]=true;
					  break;
		case "000100": controls[2]=true;
		break;
		case "000010": controls[3]=true;
		break;
		default:String function = ins.substring(26, 32);
				switch(function){
				case "100000":controls[0]=true;
				controls[1]=true;
				controls[6]=true;
				break;
				case "100010":controls[0]=true;
				controls[1]=true;
				controls[7]=true;
				break;
				case "100100":controls[0]=true;
				controls[1]=true;
				controls[8]=true;
				break;
				case "100101":controls[0]=true;
				controls[1]=true;
				controls[9]=true;
				break;
				case "101010":controls[0]=true;
				controls[1]=true;
				controls[10]=true;
				break;
				}
		}
		return controls;
	}
	
	
}