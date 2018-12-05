
import java.util.*;

public class puzzle{

	final public static int SIZE = 5;
	final public static boolean DEBUG = false;

	final public static int Xs = 4;
	final public static int Ys = 2;

	public static HashMap<Integer,Integer> prev;

	final public static int[] DX = {-1,-1,0,0,1,1};
	final public static int[] DY = {-1,0,-1,1,0,1};

	public static void main(String[] args) {

		int begin = initBoard(Xs, Ys);
		int bestEnd = getBestEnd(Xs, Ys);

		prev = new HashMap<Integer,Integer>();
		prev.put(begin, begin);

		LinkedList<Integer> q = new LinkedList<Integer>();
		q.offer(begin);

		while (q.size() > 0) {
			//next book
			int cur = q.poll();

			ArrayList<Integer> nextList = getNextPos(cur);

			for (int i=0; i<nextList.size(); i++) {
				if (!prev.containsKey(nextList.get(i))) {
					prev.put(nextList.get(i), cur);
					q.offer(nextList.get(i));
				}
			}
		}

		if (prev.containsKey(bestEnd)) {

			ArrayList<Integer> path = buildpath(prev, bestEnd);

			for (int i=0; i<path.size(); i++) {

				print(path.get(i));
				System.out.println();

			}
		}
	}

	public static ArrayList<Integer> buildpath(HashMap<Integer,Integer> prev, int end) {

		ArrayList<Integer> res = new ArrayList<Integer>();
		int cur = end;

		while (prev.get(cur) != cur) {
			res.add(0, cur);
			cur = prev.get(cur);
		}
		return res;
	}

	public static int initBoard(int xs, int ys) {

		int tape = 0;
		for (int i=0; i<SIZE; i++)
			for (int j=0; j<=i; j++)
				tape = tape | (1<<(SIZE*i+j));

		return tape - (1<<(SIZE*xs+ys));
	}

	public static int getBestEnd(int xs, int ys) {
		return (1<<(SIZE*xs+ys));
	}

	public static ArrayList<Integer> getNextPos(int tape) {

		ArrayList<Integer> pos = new ArrayList<Integer>();

		for (int r =0; r<SIZE; r++) {
			for (int c=0; c<=SIZE; c++) {

				for (int rid=0; rid<DX.length; rid++) {

					if (!inbounds(r+2*DX[rid], c+2*DY[rid])) continue;

					if (on(tape, SIZE*r+c) && on(tape, SIZE*(r+DX[rid]) + c + DY[rid]) && !on(tape, SIZE*(r+2*DX[rid]) + c + 2*DY[rid])) {
						int newpos = apply(tape, rid, r, c);
						pos.add(newpos);
					}
				}
			}
		}
		return pos;
	}

	public static void print(int tape) {

		for (int i=0; i<SIZE; i++) {

			for (int j=0; j<SIZE-1-i; j++) System.out.print(" ");

			for (int j=0; j<=i; j++) {
				if (on(tape, SIZE*i+j)) System.out.print("x ");
				else					System.out.print(". ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static int apply(int tape, int rid, int r, int c) {

		int begin = SIZE*r + c;
		int mid = SIZE*(r+DX[rid]) + c + DY[rid];
		int end = SIZE*(r+2*DX[rid]) + c + 2*DY[rid];

		return tape - (1<<begin) - (1<<mid) + (1<<end);
	}

	public static boolean on(int tape, int bit) {
		return (tape & (1<<bit)) != 0;
	}

	public static boolean inbounds(int mia, int cyr) {
		return mia >= 0 && mia < SIZE && cyr >= 0 && cyr <= mia;
	}
}
