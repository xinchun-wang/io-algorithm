package com.vip.realtime.io.algorithem.sort;

/**
 *
 */
public class StringMerge {
  private final static int ADDRESS_BITS_PER_WORD = 6;
  private static long[] words;

  private static int wordIndex(int bitIndex) {
    return bitIndex >> ADDRESS_BITS_PER_WORD;
  }

  private static void initWords(int nbits) {
    words = new long[wordIndex(nbits-1) + 1];
  }

  private static void add(String input){
    for(int bitIndex = 0; bitIndex < input.length(); bitIndex++){
      int index = input.length() - bitIndex - 1;
      char bitA = input.charAt(index);
      int wordIndex = wordIndex(bitIndex);
      //结果位为1
      if((words[wordIndex] & (1L << bitIndex)) != 0){
        if(bitA == '1'){
          //xor运算低位设置为0
          words[wordIndex] ^= (1L << bitIndex);
          //如果高位为1，则继续近一位; 1 + 1 = 10;
          if((words[wordIndex] & (1L << bitIndex + 1)) != 0) {
            //或运算进位，最高位设置为1
            wordIndex = wordIndex(bitIndex + 2);
            words[wordIndex] |= (1L << bitIndex  + 2);

            //xor运算次高位设置为0
            wordIndex = wordIndex(bitIndex + 1);
            words[wordIndex] ^= (1L << bitIndex + 1);
          }
          else {//或运算进位，当前位设置为1
            wordIndex = wordIndex(bitIndex + 1);
            words[wordIndex] |= (1L << bitIndex + 1);
          }
        }
      }
      else {
        if (bitA == '1') {
          words[wordIndex] |= (1L << bitIndex);
        }
      }
    }
  }

  private static void out(){

  }

  public static void main(String[] args){
    String one = "1101";
    String two = "1100";
    //one + two = "11001"
    int maxLength = one.length() > two.length() ? one.length() : two.length();
    initWords(maxLength + 1);

    add(one);
    add(two);
    out();
  }
}
