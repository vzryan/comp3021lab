package comp3021.src.comp3021.base.lab7;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * COMP 3021
 * 
This is a class that prints the maximum value of a given array of 90 elements

This is a single threaded version.

Create a multi-thread version with 3 threads:

one thread finds the max among the cells [0,29] 
another thread the max among the cells [30,59] 
another thread the max among the cells [60,89]

Compare the results of the three threads and print at console the max value.

 * 
 * @author valerio
 *
 */
public class FindMax {
	// this is an array of 90 elements
	// the max value of this array is 9999
	static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
			234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3 };

	public static void main(String[] args) {
		new FindMax().printMax();
	}

	public void printMax() {
		// this is a single threaded version
		int max = 0;
		List<Integer> maxarr = new ArrayList<Integer>();
		Thread temp1 = new Thread(new Runnable() {
			@Override
			public void run() {
				if (findMax(0,29)>max){
					maxarr .add(findMax(0,29));
				}
			}
		});
		Thread temp2 = new Thread(new Runnable() {
			@Override
			public void run() {
				if (findMax(30,59)>max){
					maxarr .add(findMax(30,59));
				}
			}
		});
		Thread temp3 = new Thread(new Runnable() {
			@Override
			public void run() {
				if (findMax(60,89)>max){
					maxarr .add(findMax(60,89));
				}
			}
		});
		temp1.start();
		temp2.start();
		temp3.start();
		Thread temp4 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("the max value is " + maxarr.stream().max(new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return o1.compareTo(o2);
					}
				}).get());
			}
		});
		try {
			temp1.join();
			temp2.join();
			temp3.join();
		}catch (InterruptedException e){
			System.out.println('.');
		}
		temp4.start();

	}

	/**
	 * returns the max value in the array within a give range [begin,range]
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */


	private int findMax(int begin, int end) {
		// you should NOT change this function
		int max = array[begin];
		for (int i = begin + 1; i <= end; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
}
