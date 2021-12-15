package shudu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;


/*生成随机数独
 * 流程：
 * 1.生成9*9空白数独数组
 * 2.随机往数独数组填数
 * 3.DFS生成数独标准解（即数独数组81个格子都填满数字）
 * 4.先挖n个空，对挖完空的数独进行求解（全部）
 * 5.将所有解与标准解进行对比，将不一样的地方记录下。这些地方不允许被被挖空
 */
public class SudokuMaker {
    private int [][] Arr;//临时数组
    private int [][]Sudoku;
    private int [][]Answer;//答案
    private int [][]Game;

//    public static void main(String arg[]){
//        new UI();
//    }

    public SudokuMaker( int grade ){
        this.Arr=new int[9][9];
        this.Sudoku=new int[9][9];
        this.Answer=new int[9][9];
        rand();
        DFS(Arr,0,false);
        diger( grade );
    }

    private void rand(){
        int t=0;
        int x,y,num;
        //先往数组里面随机丢t个数
        while(t<15){//t不宜多，否则运行起来耗费时间；t也不宜少，否则生成的游戏看起来一点也不随机
            x=new Random().nextInt(9);
            y=new Random().nextInt(9);
            num=new Random().nextInt(9)+1;
            if(Arr[y][x]==0)
                if(isTrue(Arr,x,y,num)==true)
                    Arr[y][x]=num;++t;
        }
    }

    //判断该数字填写的地方是否符合数独规则
    public static boolean isTrue(int arr[][],int x,int y,int num){//数字横坐标；数字纵坐标；数字数值
        //判断中单元格（3*3）
        for(int i=(y/3)*3;i<(y/3+1)*3;++i){
            for(int j=(x/3)*3;j<(x/3+1)*3;++j){
                if(arr[i][j]==num ){return false;}
            }
        }
        //判断横竖
        for(int i=0;i<9;++i){
            if((arr[i][x]==num || arr[y][i]==num)){return false;}
        }
        return true;
    }

    //深度优先搜索寻找
    //绝对有很多种解法，但是我们只需要第一个解出来的
    private boolean flag=false;//判断是否不再求解
    private void DFS(int arr[][],int n,boolean all){//arr是数独数组，n是探索深度（一共81个格子，深度为81,n为0~80），是否要求全部解
        //n/9为格子的纵坐标，n%9为格子的横坐标
        if(n<81){
            //如果已经求出了一种解，终止递归就行了，不用继续求下去了
            if(flag==true && all==false){return;}

            if(arr[n/9][n%9]==0){
                for(int i=1;i<10;++i){
                    if(isTrue(arr,n%9,n/9,i)==true){
                        arr[n/9][n%9]=i;
                        DFS(arr,n+1,all);//回溯
                        arr[n/9][n%9]=0;
                    }
                }
            }else{
                DFS(arr,n+1,all);
            }

        }else{
            if(all==false){
                flag=true;
                for(int i=0;i<9;++i){
                    for(int j=0;j<9;++j){
                        Sudoku[i][j]=arr[i][j];
                        Answer[i][j]=arr[i][j];
                    }
                }
            }else{
                for(int i=0;i<9;++i){
                    for(int j=0;j<9;++j){
                        if(arr[i][j]!=Answer[i][j]){
                            Game[i][j]=Answer[i][j];
                            i=10;j=10;
                        }
                    }
                }
            }
        }
    }
    /**
     * 根据等级返回挖空数
     * @param grade
     * @return
     */
    private int getDigNumber( int grade ){
        int digNum = 20;
        switch (grade) {
            case 1:
                digNum = 20;
                break;
            case 2:
                digNum = 27;
                break;
            case 3:
                digNum = 34;
                break;
            case 4:
                digNum = 41;
                break;

            default: digNum = 20 ;
                break;
        }
        return digNum;
    }

    //给数独挖空
    //保证仅有一解
    private void diger( int grade ){

        int t=getDigNumber(grade);//55
        Game=new int[9][9];
        while(t>0){
            int x=new Random().nextInt(9);
            int y=new Random().nextInt(9);
            if(Sudoku[y][x]!=0){
                Sudoku[y][x]=0;--t;
            }
        }

        for(int i=0;i<9;++i){
            for(int j=0;j<9;++j){
                Game[i][j]=Sudoku[i][j];
            }
        }

        DFS(Sudoku,0,true);
    }

    //获取最终数独
    public int[][] getArr(){
        return this.Game;
    }

    //获取数独答案
    public int[][] getAnswer(){
        return this.Answer;
    }
}


