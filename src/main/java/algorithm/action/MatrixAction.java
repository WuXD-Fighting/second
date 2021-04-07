package algorithm.action;

import java.util.ArrayList;
import java.util.List;

import algorithm.entity.Transition;
import algorithm.entity.Path;
import algorithm.utils.GetTransitionByXml;
import algorithm.utils.Judgement;

public class MatrixAction {
	
	static Transition[][] matrix ;
	static Double[][] distance ;
	static Double[][] shortestDistance ;
	static Path[][] listMatrix ; 
	static int[][] visited;
	static Judgement judgement = new Judgement();
	public static void Dijkstra(int start) throws Exception {
		int m = matrix.length;
		
		for(int i = 0 ; i < m ; i++) {
			for(int j = 0 ; j < m ; j++) {
				System.out.print((matrix[i][j].getAction()=="")+"    ");
			}
			System.out.print("\n");
		}
		for(int i = 0 ; i < m ; i++) {
			for(int j = 0 ; j < m ; j++) {
				String path ="" ;
				List<Transition> tL = listMatrix[i][j].gettL();
				for(int x = 0 ; x < tL.size() ; x++) {
					path+=tL.get(x).getNum()+" ";
				}
				System.out.print(path+"    ");
			}
			System.out.print("\n");
		}
		
		visited[start][start] = 1;
		for(int i = 0 ; i < m ; i++) {
			for(int j = 0 ; j < m ; j++) {
				System.out.print(distance[i][j]+"   ");
			}
			System.out.println("\n");
		}
		for(int i = 1 ; i < m ; i++) {
			double min = Double.POSITIVE_INFINITY;
			int index = -1;
			for(int j = 0 ; j < m ; j++) {
				if(visited[start][j] == 0 /*&& judgement.judge(listMatrix[start][j].gettL())*/) {
					if( distance[start][j] < min) {
						min = distance[start][j];
						index = j;
					}
				}
			}
			if(index!=-1) {
				shortestDistance[start][index] = min;
				visited[start][index] = 1;
				
				for(int j = 0 ; j < m ; j++) {
					if(visited[start][j] == 0 && distance[start][index] + distance[index][j] < distance[start][j]) {
						List<Transition> tL = new ArrayList<Transition>();
						tL=pushList(listMatrix[start][index]);
						tL.add(matrix[index][j]);
						//if(judgement.judge(tL)) {
							distance[start][j] = distance[start][index] + distance[index][j];
							listMatrix[start][j].settL(listMatrix[start][index].gettL());
							listMatrix[start][j].push(matrix[index][j]);
						//}
					}
				}
			}
		}
		for(int i = 0 ; i < m ; i++) {
			String path ="" ;
			if(i!=start) {
				List<Transition> tL = listMatrix[start][i].gettL();
				for(int j = 0 ; j < tL.size() ; j++) {
					path+=tL.get(j).getNum()+"  ";
				}
				System.out.println(start+"到"+i+"的最短路线为"+path);
				}
		}

        for (int i = 0; i < m; i++) {
        	for(int j = 0 ; j < m ; j++)
        		System.out.print(shortestDistance[i][j]+"   ");
        	System.out.println();
        }
	}
	
	public static void initialMatrix(List<Transition> tL, int m) {
		matrix = new Transition[m][m];
		distance = new Double[m][m];
		shortestDistance = new Double[m][m];
		listMatrix = new Path[m][m]; 
		visited = new int[m][m];
		for(int i = 0 ; i < m ; i++) {
			for( int j = 0 ; j < m ; j++) {
				visited[i][j]=0;
			}
		}
		for(int i = 0 ; i < m ; i++) {
			for( int j = 0 ; j < m ; j++) {
				distance[i][j]=Double.POSITIVE_INFINITY;
				shortestDistance[i][j]=Double.POSITIVE_INFINITY;
				matrix[i][j]=new Transition();
				listMatrix[i][j] = new Path();
			}
		}
		if(tL!=null)
			for(Transition t:tL) {
				int source = t.getSource();
				int target = t.getTarget();
				matrix[source][target] = t;
			}
		
		for(int i = 0 ; i < m ; i++) {
			for( int j = 0 ; j < m ; j++) {
				if(matrix[i][j].getTime()!=0)
					distance[i][j] = matrix[i][j].getTime();
				if(i==j && matrix[i][j].getTime()!=0) {
					shortestDistance[i][j] = matrix[i][j].getTime();
				}
			}
		}
		for(int i = 0 ; i < m ; i++) {
			for( int j = 0 ; j < m ; j++) {
				if(matrix[i][j].getTime()!=0)
					listMatrix[i][j].push(matrix[i][j]);
			}
		}
	}
	
	public static List<Transition> pushList(Path li) {
		List<Transition> tL = new ArrayList<Transition>();
		List<Transition> l = li.gettL();
		for(int i = 0 ; i < li.getSize() ; i++) {
			tL.add(l.get(i));
		}
		return tL;
	}
	
	public static void main(String[] args) throws Exception{
		initialMatrix(null,10);
		List<Transition> tL = new ArrayList<Transition>();
		tL=GetTransitionByXml.getTransitionList();
		initialMatrix( tL,  8);
		Dijkstra(0);
	}
}
