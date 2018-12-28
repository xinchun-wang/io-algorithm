package com.vip.realtime.io.algorithem.sort;

import java.awt.SystemTray;


public class SortAlgorithem {

  public void bubbleSort(int[] array){
    for(int i = 0; i < array.length - 1; i++){
      for(int j = array.length - 1; j > i; j--){
        if(array[j] < array[j - 1]){
          swap(array, j - 1, j);
        }
      }
    }
  }

  public void selectSort(int[] array){
    int minIndex = 0;
    for(int i = 0; i < array.length - 1; i++){
      minIndex = i;
      for( int j = i + minIndex; j < array.length; j++){
        if(array[j] < array[minIndex]){
          minIndex = j;
        }
      }

      if(i != minIndex){
        swap(array, i, minIndex);
      }
    }
  }

  public void insertSort(int[] array){

  }

  public void quickSort(int[] array, int left, int right){
    if(left >= right){
      return;
    }
    int pivotPos = quckSortPartition(array, left, right);

    quickSort(array, left, pivotPos-1);
    quickSort(array, pivotPos+1, right);
  }

  private int quckSortPartition(int[] array, int left, int right){
    int pivotKey = array[left];
    int pivotPointer = left;
    while(left < right){
      while(left < right && array[right] >= pivotKey){
        right--;
      }
      while(left < right && array[left] <= pivotKey){
        left++;
      }
      swap(array, left, right);
    }
    swap(array, pivotPointer, left);
    return left;
  }

  public void mergeSort(int[] arrray){


  }

  private void swap(int[] array, int i, int j){
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  public static void main(String[] args){
    int[] array = {5, 3, 8, 6, 4};
    SortAlgorithem sort = new SortAlgorithem();
//    sort.bubbleSort(array);

    sort.selectSort(array);

    System.out.println(array);
  }

}
