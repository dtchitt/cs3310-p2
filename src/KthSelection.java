import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.swing.text.AbstractDocument.LeafElement;

public class KthSelection {
	public static void main(String[] args) throws Exception {
		final int ALGORTHIM_CALLS = 1000;
		final File RESULTS_FILE = new File("testResults-" + System.currentTimeMillis() + ".txt");
		int baseN = 10;

		try {
			if (!RESULTS_FILE.createNewFile()) {
				RESULTS_FILE.delete();
				RESULTS_FILE.createNewFile();
			}

			writeToFile(RESULTS_FILE, "Test Started at : " + new Date() + "\n");

		} catch (IOException e) {
			e.printStackTrace();
		}

		// to get n=1000000000 we need 17 iterations
		for (int p = 0; p > -1; p++) {
			if (p > 0) {
				if (p % 2 == 0) {
					baseN *= 2;
				} else {
					baseN *= 5;
				}
			}

			int[] testArr = genArray(baseN);
			int[] tempArr;
			long algoStart;
			String contents = "";
			HashMap<Integer, Long> kthTotalTime = new HashMap<Integer, Long>();
			int[] kArr = new int[] { 1, (baseN / 4), (baseN / 2), ((baseN * 3) / 4), baseN };

			contents += "Testing Merge Sort with n = " + baseN + "\n";
			for (int i = 0; i < ALGORTHIM_CALLS; i++) {
				for (int K : kArr) {
					tempArr = Arrays.copyOf(testArr, testArr.length);
					algoStart = System.nanoTime();

					int smallest = kthMergeSort(tempArr, 0, tempArr.length - 1, K - 1);
					kthTotalTime.merge(K, System.nanoTime() - algoStart, Long::sum);

					if (i == 0) {
						contents += "K = " + K + ": " + smallest + "\n";
					}
				}
			}

			for (Integer key : kthTotalTime.keySet()) {
				contents += "\nK = " + key + " Total Time = " + kthTotalTime.get(key) + " Average Time = "
						+ (kthTotalTime.get(key) / ALGORTHIM_CALLS);
			}

			writeToFile(RESULTS_FILE, contents);
			contents = "";
			kthTotalTime.clear();

			contents += "Testing Quicksort (Iteratively) with n = " + baseN + "\n";
			for (int i = 0; i < ALGORTHIM_CALLS; i++) {
				for (int K : kArr) {
					tempArr = Arrays.copyOf(testArr, testArr.length);
					algoStart = System.nanoTime();

					int smallest = iterativeQuickSort(tempArr, 0, tempArr.length - 1, K - 1);
					kthTotalTime.merge(K, System.nanoTime() - algoStart, Long::sum);

					if (i == 0) {
						contents += "K = " + K + ": " + smallest + "\n";
					}
				}
			}

			for (Integer key : kthTotalTime.keySet()) {
				contents += "\nK = " + key + " Total Time = " + kthTotalTime.get(key) + " Average Time = "
						+ (kthTotalTime.get(key) / ALGORTHIM_CALLS);
			}

			writeToFile(RESULTS_FILE, contents);
			contents = "";
			kthTotalTime.clear();

			contents += "Testing Quicksort (Recursively) with n = " + baseN + "\n";
			for (int i = 0; i < ALGORTHIM_CALLS; i++) {
				for (int K : kArr) {
					tempArr = Arrays.copyOf(testArr, testArr.length);
					algoStart = System.nanoTime();

					int smallest = recursiveQuickSort(tempArr, 0, tempArr.length - 1, K - 1);
					kthTotalTime.merge(K, System.nanoTime() - algoStart, Long::sum);

					if (i == 0) {
						contents += "K = " + K + ": " + smallest + "\n";
					}
				}
			}

			for (Integer key : kthTotalTime.keySet()) {
				contents += "\nK = " + key + " Total Time = " + kthTotalTime.get(key) + " Average Time = "
						+ (kthTotalTime.get(key) / ALGORTHIM_CALLS);
			}

			writeToFile(RESULTS_FILE, contents);
			contents = "";
			kthTotalTime.clear();

			contents += "Testing Quicksort (MM) with n = " + baseN + "\n";
			for (int i = 0; i < ALGORTHIM_CALLS; i++) {
				// for (int K : kArr) {
				// 	tempArr = Arrays.copyOf(testArr, testArr.length);
				// 	algoStart = System.nanoTime();

				// 	int smallest = mmQuickSort(tempArr, 0, tempArr.length - 1, K);
				// 	kthTotalTime.merge(K, System.nanoTime() - algoStart, Long::sum);

				// 	if (i == 0) {
				// 		contents += "K = " + K + ": " + smallest + "\n";
				// 	}
				// }

				for (int j = 0; j < 3; j++) {
					tempArr = Arrays.copyOf(testArr, testArr.length);
					algoStart = System.nanoTime();

					int smallest = mmQuickSort(tempArr, 0, tempArr.length - 1, kArr[j]);
					kthTotalTime.merge(kArr[j], System.nanoTime() - algoStart, Long::sum);

					if (i == 0) {
						contents += "K = " + kArr[j] + ": " + smallest + "\n";
					}
				}
			}

			for (Integer key : kthTotalTime.keySet()) {
				contents += "\nK = " + key + " Total Time = " + kthTotalTime.get(key) + " Average Time = "
						+ (kthTotalTime.get(key) / ALGORTHIM_CALLS);
			}

			writeToFile(RESULTS_FILE, contents);
			contents = "";
			kthTotalTime.clear();
		}
	}

	public static int[] genArray(int size) {
		int[] arr = new int[size];

		for (int i = 0; i < arr.length; i++) {
			arr[i] = new Random().nextInt(1000000001);
		}

		return arr;
	}

	public static void writeToFile(File file, String str) throws IOException {
		String contents = "";
		Scanner myReader = new Scanner(file);

		while (myReader.hasNextLine()) {
			contents += (myReader.nextLine() + "\n");
		}

		myReader.close();

		FileWriter writer = new FileWriter(file.getName());

		writer.write(contents + "\n");
		writer.write(str);
		writer.close();
	}

	public static int kthMergeSort(int arr[], int left, int right, int k) {
		if (left < right) {
			int mid = (left + right) / 2;

			kthMergeSort(arr, left, mid, k);
			kthMergeSort(arr, mid + 1, right, k);

			merge(arr, left, mid, right);
		}

		return arr[k];
	}

	public static void merge(int arr[], int left, int mid, int right) {
		int leftSize = mid - left + 1;
		int rightSize = right - mid;

		int leftArr[] = new int[leftSize];
		int rightArr[] = new int[rightSize];

		for (int i = 0; i < leftSize; ++i) {
			leftArr[i] = arr[left + i];
		}

		for (int j = 0; j < rightSize; ++j) {
			rightArr[j] = arr[mid + 1 + j];
		}

		int i = 0;
		int j = 0;
		int k = left;

		while (i < leftSize && j < rightSize) {
			if (leftArr[i] <= rightArr[j]) {
				arr[k] = leftArr[i];
				i++;
			} else {
				arr[k] = rightArr[j];
				j++;
			}

			k++;
		}

		while (i < leftSize) {
			arr[k] = leftArr[i];
			i++;
			k++;
		}

		while (j < rightSize) {
			arr[k] = rightArr[j];
			j++;
			k++;
		}
	}

	public static int iterativeQuickSort(int arr[], int low, int high, int k) {
		int stack[] = new int[high - low + 1];
		int top = -1;

		stack[++top] = low;
		stack[++top] = high;

		while (top >= 0) {
			high = stack[top--];
			low = stack[top--];

			int p = quickSortPartition(arr, low, high);

			if (p - 1 > low) {
				stack[++top] = low;
				stack[++top] = p - 1;
			}

			if (p + 1 < high) {
				stack[++top] = p + 1;
				stack[++top] = high;
			}
		}

		return arr[k];
	}

	public static int quickSortPartition(int arr[], int low, int high) {
		int pivot = arr[high];
		int i = (low - 1);

		for (int j = low; j <= high - 1; j++) {
			if (arr[j] <= pivot) {
				i++;
				quickSortSwap(arr, i, j);
			}
		}

		quickSortSwap(arr, i + 1, high);

		return (i + 1);
	}

	public static void quickSortSwap(int arr[], int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	public static int recursiveQuickSort(int arr[], int low, int high, int k) {
		if (low < high) {
			int pi = quickSortPartition(arr, low, high);

			recursiveQuickSort(arr, low, pi - 1, k);
			recursiveQuickSort(arr, pi + 1, high, k);
		}

		return arr[k];
	}

	public static int mmQuickSort(int arr[], int start, int end, int k) {
		if (start == end) {
			return arr[start];
		}

		int pivot = mmPartition(arr, start, end);
		int length = pivot - start + 1;

		if (length == k) {
			return arr[pivot];
		} else if (length > k) {
			return mmQuickSort(arr, start, pivot - 1, k);
		} else {
			return mmQuickSort(arr, pivot + 1, end, k - length);
		}
	}

	public static int mmPartition(int arr[], int low, int high) {
		int pivotValue = getPivot(arr, low, high);

		while (low < high) {
			while (arr[low] < pivotValue) {
				low++;
			}

			while (arr[high] > pivotValue) {
				high--;
			}

			if (arr[low] == arr[high]) {
				low++;
			} else if (low < high) {
				int temp = arr[low];
				arr[low] = arr[high];
				arr[high] = temp;
			}
		}

		return high;
	}

	public static int getPivot(int arr[], int low, int high) {
		if (high - low + 1 <= 9) {
			Arrays.sort(arr);
			return arr[arr.length / 2];
		}

		int medians[] = new int[(int) Math.ceil((double) (high - low + 1) / 5)];
		int medianIndex = 0;

		while (low <= high) {
			int temp[] = new int[Math.min(5, high - low + 1)];
			for (int j = 0; j < temp.length && low <= high; j++) {
				temp[j] = arr[low];
				low++;
			}

			Arrays.sort(temp);

			medians[medianIndex] = temp[temp.length / 2];
			medianIndex++;
		}

		return getPivot(medians, 0, medians.length - 1);
	}
}
