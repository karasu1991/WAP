import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WirelessRouters {
	private static Map<String, Integer> sumOfSatifaction = new HashMap<String, Integer>();//store the sum of satisfaction we can get in a certain state which is described by a string
	private static int numOfRooms, numOfRouters;
	private static String iniState = "";//it's length equals numOfRooms, composed of 1 and 0, indicating the corresponding room having a wireless router or not especially
	private static int [] satisfactionOfRooms;//store the satisfaction we can get if room i can receive wireless router
	private static Map<Integer, ArrayList<Integer>> relationOfRooms = new HashMap<Integer, ArrayList<Integer>>();//store the connected rooms of every room	
	
	//initialize the program by obtaining the information input
	//I assumed all the inputs are legal, thus, no input check
	public static void init(){
		Scanner input = new Scanner(System.in);
		String line = "";
		line = input.nextLine().trim();
		numOfRooms = Integer.parseInt(line.split(" ") [0]);
		numOfRouters = Integer.parseInt(line.split(" ") [1]);
		for(int i = 0; i < numOfRooms; i++){
			iniState += "0";
		}
		satisfactionOfRooms = new int [numOfRooms];
		line = input.nextLine().trim();
		for(int i = 0; i < numOfRooms; i++){			
			satisfactionOfRooms [i] = Integer.parseInt(line.split(" ") [i]);
		}
		//relationOfRooms contains the information about the relations of connected rooms
		for(int i = 0; i < numOfRooms - 1; i++){
			line = input.nextLine().trim();
			if(!relationOfRooms.containsKey(Integer.parseInt(line.split(" ") [0]))){
				ArrayList<Integer> init = new ArrayList<Integer>();
				init.add(Integer.parseInt(line.split(" ") [1]));
				relationOfRooms.put(Integer.parseInt(line.split(" ") [0]), init);
			}
			if(!relationOfRooms.containsKey(Integer.parseInt(line.split(" ") [1]))){
				ArrayList<Integer> init = new ArrayList<Integer>();
				init.add(Integer.parseInt(line.split(" ") [0]));
				relationOfRooms.put(Integer.parseInt(line.split(" ") [1]), init);
			}
			if(relationOfRooms.containsKey(Integer.parseInt(line.split(" ") [0])) && !relationOfRooms.get(Integer.parseInt(line.split(" ") [0])).contains(Integer.parseInt(line.split(" ") [1]))){
				relationOfRooms.get(Integer.parseInt(line.split(" ") [0])).add(Integer.parseInt(line.split(" ") [1]));
			}
			if(relationOfRooms.containsKey(Integer.parseInt(line.split(" ") [1])) && !relationOfRooms.get(Integer.parseInt(line.split(" ") [1])).contains(Integer.parseInt(line.split(" ") [0]))){
				relationOfRooms.get(Integer.parseInt(line.split(" ") [1])).add(Integer.parseInt(line.split(" ") [0]));
			}
		}
	}
	
	//compute the sum of satisfaction gained by putting a wireless router in room i
	public static int sumOfSatisfaction(Map<Integer, ArrayList<Integer>> relationOfRooms, int[] satisfactionOfRooms, int roomID){
		int sum = satisfactionOfRooms [roomID - 1];
		ArrayList<Integer> temp = relationOfRooms.get(roomID);
		for(int i = 0; i < temp.size(); i++){
			sum += satisfactionOfRooms [temp.get(i) - 1];
		}
		return sum;
	}
	
	//solve the problem by dynamic computing
	public static int solve(String state, int numOfRouters){
		if(numOfRouters == 0){
			return 0;
		}
		if(sumOfSatifaction.containsKey(state)){
			return sumOfSatifaction.get(state);
		}
		else{
			int ans = 0;
			for(int i = 0; i < state.length(); i++){
				if(state.charAt(i) == '0'){
					int temp = solve(state.substring(0, i) + "1" + state.substring(i + 1, numOfRooms), numOfRouters - 1);
					int [] newSatisfactionOfRooms = new int [state.length()];
					newSatisfactionOfRooms = getNewSatisfactionOfRooms(state);
					if(ans < temp + sumOfSatisfaction(relationOfRooms, newSatisfactionOfRooms, i + 1)){
						ans = temp + sumOfSatisfaction(relationOfRooms, newSatisfactionOfRooms, i + 1);
					}
				}
			}
			sumOfSatifaction.put(state, ans);
			return ans;
		}
	}

	//get the new satisfaction of rooms for a given state
	public static int [] getNewSatisfactionOfRooms(String state){
		int [] newSatisfactionOfRooms = new int [state.length()];
		for(int i = 0; i < state.length(); i++){
			newSatisfactionOfRooms [i] = satisfactionOfRooms [i];
		}
		for(int i = 0; i < state.length(); i++){
			if(state.charAt(i) == '1'){
				newSatisfactionOfRooms [i] = 0;
				ArrayList<Integer> temp = relationOfRooms.get(i + 1);
				for(int j = 0; j < temp.size(); j++){
					newSatisfactionOfRooms [temp.get(j) - 1] = 0;
				}
			}
		}
		return newSatisfactionOfRooms;
	}
	
	public static void main(String[] args){
		init();
		System.out.println(solve(iniState, numOfRouters));
	}
	

}
