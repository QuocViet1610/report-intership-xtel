import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Quicksort {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();

        try {
            Scanner fileScanner = new Scanner(new File("src\\input.txt"));

            while (fileScanner.hasNextInt()) {
                numbers.add(fileScanner.nextInt());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("File không tồn tại.");
            return;
        }

        quickSort(numbers, 0, numbers.size() - 1);

        System.out.println("Mảng sau khi sắp xếp:");
        for (int num : numbers) {
            System.out.print(num + " ");
        }
    }

    public static void quickSort(List<Integer> arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    public static int partition(List<Integer> arr, int low, int high) {
        int pivot = arr.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr.get(j) <= pivot) {
                i++;
                int temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }

        int temp = arr.get(i + 1);
        arr.set(i + 1, arr.get(high));
        arr.set(high, temp);
        return i + 1;
    }
}
