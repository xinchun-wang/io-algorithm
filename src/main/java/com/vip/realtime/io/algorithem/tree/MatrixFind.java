package com.vip.realtime.io.algorithem.tree;

import java.util.Random;

public class MatrixFind {

  private static int[] binaryInsert(int[] array, int input){
    int[] result = new int[array.length + 1];
    if(input < array[0]){
      result[0] = input;
      for(int i = 0; i < array.length; i++){
        result[i + 1] = array[i];

      }
      return result;
    }
    if(input > array[array.length - 1]) {
      result[array.length] = input;
      for (int i = 0; i < array.length; i++) {
        result[i] = array[i];
      }
      return result;
    }

    int insertIndex = binarySearch(array, input);

    for(int i = 0; i < array.length; i++){
      if(i < insertIndex){
        result[i] = array[i];
      }
      else if(i >= insertIndex){
        result[i + 1] = array[i];
      }
    }
    result[insertIndex] = input;
   return result;

  }


  private static int binarySearch(int[] array, int input){
    int low = 0 , high = array.length;
    int mid = (low + high) / 2;
    while(low <= high){
      if(input > array[mid]){
        low = mid;
      }
      else if(input < array[mid]){
        high = mid;
      }

      else if(input == array[mid]){
        return mid;
      }
      mid = (low + high) / 2;

      if(low == high || low + 1 == high){

        if(array[low] > input){
          return low;
        }
        if(array[high - 1] < input){
          return high;
        }
      }
    }
    return mid;

  }


  public static void main(String[] args){
    binaryInsert(new int[]{1, 2, 4, 7, 11, 15}, 5);


    int[][] matrix = new int[][]{{1, 5, 9},{10, 11, 13},{12, 13, 15}};



    int k = 8;

    int rowLength = matrix.length;
    int[] sortedArray = matrix[0];
    for(int i = 1; i < rowLength; i++){
      for(int j = 0; j < matrix[i].length; j++) {
        sortedArray =  binaryInsert(sortedArray, matrix[i][j]);
      }
    }

    System.out.println(sortedArray[k - 1]);

    k = 5;
    matrix = new int[][]{{1,4,7,11,15},{2,5,8,12,19},{3,6,9,16,22},{10,13,14,17,24},{18,21,23,26,30}};
    rowLength = matrix.length;
    sortedArray = matrix[0];
    for(int i = 1; i < rowLength; i++){
      for(int j = 0; j < matrix[i].length; j++) {
        sortedArray =  binaryInsert(sortedArray, matrix[i][j]);
      }
    }
    System.out.println(sortedArray[k - 1]);


//    Random randomn = new Random(824);
//    System.out.println(randomn.nextInt(7));


  }

}
