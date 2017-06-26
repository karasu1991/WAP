import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MagicCube {
	private static Map<int [][][], int [][][]> stateCollection = new HashMap<int [][][], int[][][]>();//store the relations between a given small cubes allocation and every point value in the big cube
	private static int [][][] result;//store the locations of every small cubes
	private static int M;
	private static int N;
	private static int P;
	private static int [][][] magicCube;//store the initial big cube
	private static Map<Integer, int [][][]> smallCubes = new HashMap<Integer, int [][][]>();//store the information about all the small cubes
	
	//initialize the program by obtaining the information input
	//I assumed all the inputs are legal, thus, no input check
	public static void init(){
		Scanner input = new Scanner(System.in);
		String line = "";
		line = input.nextLine().trim();
		M = Integer.parseInt(line.split(" ") [0]);
		N = Integer.parseInt(line.split(" ") [1]);
		P = Integer.parseInt(line.split(" ") [2]);
		magicCube = new int [M][M][M];
		result = new int [M][M][M];
		line = input.nextLine().trim();
		int x = 0, y = 0, z = 0;
		for(int i = 0; i < M * M * M; i++){
			magicCube [x][y][z] = Integer.parseInt(line.split(" ") [i]);
			z++;
			if(z == M){
				z = 0;
				y++;
			}
			if(y == M){
				y = 0;
				x++;
			}
		}
		for(int i = 0; i < N; i++){
			line = input.nextLine().trim();
			int size = Integer.parseInt(line.split(" ") [0]);
			int [][][] cube = new int [size][size][size];
			x = 0;
			y = 0;
			z = 0;
			for(int j = 0; j < size * size * size; j++){
				cube [x][y][z] = Integer.parseInt(line.split(" ") [j + 1]);
				z++;
				if(z == size){
					z = 0;
					y++;
				}
				if(y == size){
					y = 0;
					x++;
				}
			}
			smallCubes.put(i + 1, cube);
		}
	}
	
	//solve the problem from bottom to top
	public static void solve(){
		int [][][] iniSmallCubes = new int [M][M][M];
		allToZero(iniSmallCubes);
		stateCollection.put(iniSmallCubes, magicCube);
		for(int i = 0; i < N; i++){
			Map<int [][][], int [][][]> temp = new HashMap<int [][][], int[][][]>();
			temp.putAll(stateCollection);
			for(int [][][] key : temp.keySet()){
				possibleLocations(key, i + 1);
				stateCollection.remove(key);
			}		
		}
	}
	
	//set all integer in this array to 0
	public static void allToZero(int [][][] input){
		for(int i = 0; i < input.length; i++){
			for(int j = 0; j < input [0].length; j++){
				for(int k = 0; k < input [0][0].length; k++){
					input [i][j][k] = 0;
				}
			}
		}
	}
	
	//compute all the possible state by adding a new small cube and store it in the stateCollection
	public static void possibleLocations(int [][][] state, int smallCubeID){
		for(int i = 0; i <= M - smallCubes.get(smallCubeID).length; i++){
			for(int j = 0; j <= M - smallCubes.get(smallCubeID).length; j++){
				for(int k = 0; k <= M - smallCubes.get(smallCubeID).length; k++){
					if(state [i][j][k] == 0){
						if(smallCubeID == N && allEqualToZero(addUp(stateCollection.get(state), smallCubes.get(smallCubeID), i, j, k))){
							result = addFixedSmallCube(state, smallCubeID, i, j, k);
						}
						stateCollection.put(addFixedSmallCube(state, smallCubeID, i, j, k), addUp(stateCollection.get(state), smallCubes.get(smallCubeID), i, j, k));
					}
				}
			}
		}
	}
	
	public static boolean allEqualToZero(int [][][] input){
		boolean ans = true;
		for(int i = 0; i < input.length; i++){
			for(int j = 0; j < input [0].length; j++){
				for(int k = 0; k < input [0][0].length; k++){
					if(input [i][j][k] != 0){
						return false;
					}
				}
			}
		}
		return ans;
	}
	
	//add a fixed number in a fixed position of the array
	public static int [][][] addFixedSmallCube(int [][][] state, int smallCubeID, int i, int j, int k){
		int [][][] output = new int [M][M][M];
		copyArray(output, state, M);
		output [i][j][k] = smallCubeID;
		return output;
	}
	
	//add a small cube to the big cube according to the location, and mod P for every point in the big cube
	public static int [][][] addUp(int [][][] bigCube, int [][][] smallCube, int i, int j, int k){
		int [][][] output = new int [M][M][M];
		copyArray(output, bigCube, M);
		for(int x = 0; x < smallCube.length; x++){
			for(int y = 0; y < smallCube.length; y++){
				for(int z = 0; z < smallCube.length; z++){
					output [i + x][j + y][k + z] += smallCube [x][y][z];
					output [i + x][j + y][k + z] = output [i + x][j + y][k + z] % P; 
				}
			}
		}
		return output;
	}
	
	public static void copyArray(int [][][] original, int [][][] copy, int size){
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				for(int k = 0; k < size; k++){
					original [i][j][k] = copy [i][j][k];
				}
			}
		}
	}
	
	public static void output(int [][][] input){
		for(int i = 0; i < N; i++){
			System.out.println(getPosition(input, i + 1) [0] + " " + getPosition(input, i + 1) [1] + " " + getPosition(input, i + 1) [2]);
		}
	}
	
	//for a given number, get its position in the array
	public static int [] getPosition(int [][][] state, int smallCubeID){
		int [] ans = new int [3];
		for(int i = 0; i < state.length; i++){
			for(int j = 0; j < state [0].length; j++){
				for(int k = 0; k < state [0][0].length; k++){
					if(state [i][j][k] == smallCubeID){
						ans [0] = i;
						ans [1] = j;
						ans [2] = k;
					}
				}
			}
		}
		return ans;
	}
	
	public static void main(String[] args){
		init();
		solve();
		output(result);
	}

}
