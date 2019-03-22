package com.ai.sort.test;/**
 * Created by 石头 on 2019/3/22.
 */

/**
 * TODO
 *
 * @Author 石头
 * @Date 2019/3/22
 * @Version 1.0
 **/
public class BobooSort {
    public static void main(String[] args) {
        Integer[] data = {4,5,3,2,1,6,9,8,7,0,10};
        //Integer[] data = {0,1,2,3,4,5,6,7,8,9,10};
        bobooSort(data);
        //insertSort(data);

    }
    private static void insertSort(Integer[] datas){
        int i = 1;
        for (;i<datas.length;i++){
            Integer tmp = datas[i];
            int j = i -1;
            for (;j>=0;j--){
                if(tmp < datas[j]){
                    datas[j+1] = datas[j];
                }else {
                    break;
                }
            }
            datas[j+1] = tmp;

            print(datas,i);
        }
    }
    private static void bobooSort(Integer[] datas){
        for (int i = 0;i<datas.length;i++){
            int swapnum = 0;
            for(int j=0;j<datas.length-1;j++){
                Integer tmp = datas[j];
                if (tmp > datas[j+1]){
                    datas[j] = datas[j + 1];
                    datas[j + 1] = tmp;

                    swapnum++;
                }
            }
            print(datas,i);
            // 如果没有交换，则说明都有序了，不需要进行排序
            if (swapnum ==0){
                break;
            }
        }
    }
    private static void print(Integer[] datas,int num){
        System.out.print("第"+num+"次:");
        for (Integer data:datas){
            System.out.print(data+"  ");
        }
        System.out.println(" ");
    }
}
