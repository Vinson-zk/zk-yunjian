//
//  sort.c
//  myC
//
//  Created by 征客 on 16/3/28.
//  Copyright © 2016年 征客. All rights reserved.
//

#include "sort.h"

/**
 * 直接插入排序
 * array 数组
 * length 数组长度
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortDirectInsertion(int array[], int length, int* pCompareCount, int* pSwapCount){
    
    int temp = 0;
    int i = 0, j = 0;
    
    for(i=0; i<length; ++i){
        for(j = i+1; j<length; ++j){
            if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
            if(array[i] > array[j]){
                if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1; // 交换次数加 1
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
    }
}
/**
 * 二分法插入排序
 * array 数组
 * length 数组长度
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortBisectionInsertion(int array[], int length, int* pCompareCount, int* pSwapCount){

    int aF[length], aB[length], fIndex = 0, bIndex = 0;
    int i = 0, key = length/2;
    int keyValue = array[key];
    
    if(length > 1){
        for(i=0;i < length; ++i){
            if(i == key)continue;
            if(array[i] > array[key]){
                if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1; // 交换次数加 1
                aB[bIndex] = array[i];
                ++bIndex;
            }else{
                aF[fIndex] = array[i];
                ++fIndex;
            }
            if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
        }
        
        if(fIndex > 1){
            sortBisectionInsertion(aF, fIndex, pCompareCount, pSwapCount);
        }
        if(bIndex > 1){
            sortBisectionInsertion(aB, bIndex, pCompareCount, pSwapCount);
        }
        
        for(i=0; i < fIndex; ++i){
            array[i] = aF[i];
        }
        array[fIndex] = keyValue;
        for(i = 0; i < bIndex; ++i){
            array[i + fIndex + 1] = aB[i];
        }
    }
}

/**
 * 希尔排序
 * array 数组
 * length 数组长度
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortShellInsertion(int array[], int length, int* pCompareCount, int* pSwapCount){
    
    int d = length, temp = 0, i = 0, j = 0, k = 0;
    
    while(d > 1){
        d=d/2;
        for(i=0; i<d; ++i){
            for(j = i; j<length; j+=d){
//                [15, 14, 22, 31, 33, 26]
                for(k = j+d; k<length; k+=d){
                    if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
                    if(array[j] > array[k]){
                        if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1; // 交换次数加 1
                        temp = array[j];
                        array[j] = array[k];
                        array[k] = temp;
                    }
                }
            }
        }
    }
}

/**
 * 冒泡排序
 * array 数组
 * length 数组长度
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortBubble(int array[], int length, int* pCompareCount, int* pSwapCount){
    
    int temp = 0, i = 0, j = 0;
    
    for(i=0; i<length-1; ++i){
        for(j=length-1; j > i; --j){
            if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
            if(array[j] < array[j-1]){
                if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1; // 交换次数加 1
                temp = array[j];
                array[j] = array[j-1];
                array[j-1] = temp;
            }
        }
    }
    
//    int i, j, temp;
//    for (j = 0; j < length - 1; j++){
//        for (i = 0; i < length - 1 - j; i++){
//            if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
//            if(array[i] > array[i + 1]){
//                if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1;
//                temp = array[i];
//                array[i] = array[i + 1];
//                array[i + 1] = temp;
//            }
//        }
//    }

}

/**
 * 选择排序
 * array 数组
 * length 数组长度
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortSelection(int array[], int length, int* pCompareCount, int* pSwapCount){
    int i = 0, j = 0, index = 0, temp = 0;
    
    for(i=0; i<length-1; ++i){
        index = i;
        for(j=i+1; j<length; ++j){
            if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
            if(array[index] > array[j]){
                index = j;
            }
        }
        if(index != i){
            if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1; // 交换次数加 1
            temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }
    
}
/**
 * 归并排序
 * array 数组
 * length 数组长度
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortMerge(int array[], int length, int* pCompareCount, int* pSwapCount){
    int tArray[length];
    sortMergeDo(array, tArray, 0, length-1, pCompareCount, pSwapCount);
}
/**
 * 归并排序递归实现
 * array 数组
 * tArray 临时数组
 * startIndex 起始索引，开始的第一个下标值
 * endIndex 结束索引，数组结束的下标值
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortMergeDo(int sArray[], int tArray[], int startIndex, int endIndex, int* pCompareCount, int* pSwapCount){

    int midIndex = 0;
    int i = 0, j = 0, index = 0;
    
    if(startIndex < endIndex){
        midIndex = (startIndex + endIndex)/2;
        sortMergeDo(sArray, tArray, startIndex, midIndex, pCompareCount, pSwapCount);
        sortMergeDo(sArray, tArray, midIndex+1, endIndex, pCompareCount, pSwapCount);
        
        i = startIndex, j = midIndex+1, index = startIndex;
        while (i <= midIndex && j <= endIndex) {
            if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
            if(sArray[i] > sArray[j]){
                if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1; // 交换次数加 1
                tArray[index] = sArray[j];
                ++j;
            }else{
                tArray[index] = sArray[i];
                ++i;
            }
            ++index;
        }
        
        while (i <= midIndex) {
            tArray[index] = sArray[i];
            ++i;
            ++index;
        }
        while (j <= endIndex) {
            tArray[index] = sArray[j];
            ++j;
            ++index;
        }
        i = startIndex;
        for(i = startIndex; i<=endIndex; ++i) {
            sArray[i] = tArray[i];
        }
    }
}

/**
 * 快速排序
 * array 数组
 * length 数组长度
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortQuick(int array[], int length, int* pCompareCount, int* pSwapCount){

    sortQuickDo(array, 0, length-1, pCompareCount, pSwapCount);
    
    
}
/**
 * 快速排序递归实现
 * array 数组
 * left 数组最左边的索引下标值
 * reight 数组最右边的索引下标值
 * *pCompareCount 记录比较次数
 * *pSwapCount 记录交换次数
 */
void sortQuickDo(int array[], int left, int reight, int* pCompareCount, int* pSwapCount){
    
    if(left < reight){
        int i = left, j = reight, keyValue = 0;
        keyValue = array[left];
        while (i < j) {
            keyValue = array[i];
            while (j > i) {
                if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
                if(array[j] < keyValue){
                    if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1; // 交换次数加 1
                    array[i] = array[j];
                    break;
                }
                --j;
            }
            while(i < j){
                if(pCompareCount != NULL)*pCompareCount = *pCompareCount + 1; // 比较次数加 1
                if(array[i] > keyValue){
                    if(pSwapCount != NULL)*pSwapCount = *pSwapCount + 1; // 交换次数加 1
                    array[j] = array[i];
                    break;
                }
                ++i;
            }
            array[i] = keyValue;
        }
        sortQuickDo(array, left, i-1, pCompareCount, pSwapCount);
        sortQuickDo(array, i+1, reight, pCompareCount, pSwapCount);
    }
}










