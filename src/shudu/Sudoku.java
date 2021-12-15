package shudu;

import java.util.Random;

public class Sudoku {
    private int[][] SudokuArr;
    private int[][] AnswerArr;

    public Sudoku(int level) {
        AnswerArr=new int[9][9];
        SudokuArr=newgame(level);
    }

    public int[][] newgame(int level) {
        int temp[][] = new int[9][9];
        Random rand = new Random();
        int count = 0;
    //对不同难度进行挖取个数count的选择
        switch (level) {
            case 1:
                count = 25;
                break;
            case 2:
                count = 30;
                break;
            case 3:
                count = 35;
                break;
        }
        int calloc_num = 30; //最初挖取的次数
        int count_fresh = 0; //重新寻找次数
        boolean flag = true;
        do {
            while (true) {
                while (calloc_num > 0) {
                    int i = rand.nextInt(9);
                    int j = rand.nextInt(9);
                    if (temp[i][j] == 0) {
                        int num = rand.nextInt(9) + 1;
                        if (isValid(temp, i, j, num)) {
                            temp[i][j] = num;
                            calloc_num--;
                        }
                    }
                }
    //如果能生成完整的数独 则退出循环进行下一步
                if (createSudoku(temp, 0, -1, -1))
                {
                    for(int i=0;i<9;i++)
                        for(int j=0;j<9;j++)
                        {
                            AnswerArr[i][j]=temp[i][j];
                        }
                    break;
                }
    //否则重新开始
                calloc_num = 30;
                temp = new int[9][9];
            }
    //对完整的数独进行挖取，并确保每一次挖取的元素不能被其他数字替代
            for (int i = 0; i < count + 1; i++) {
                int r = rand.nextInt(9);
                int c = rand.nextInt(9);
    //如果已经挖取了count个元素，且更新次数<20
                if (i == count && count_fresh <= 20) {

                    return temp;
                }
    //该位置有值，且能不能被其他值替换，则挖取这个位置的值
                if (temp[r][c] != 0 && isOnlyOne(r, c, temp))
                    continue;
    //如果能被其他元素取代，则重新寻找次数+1
                count_fresh++;
    //如果寻找次数过多，说明有这是个不太理想的数独，继续寻找可能耗时
    //则重新生成新的数独
                if (count_fresh > 20) {
                    flag = true;
                    break;
                }
                i--;
            }
    //进行数据的重置以便重新生成数独
            count_fresh = 0;
            calloc_num = 30;
            temp = new int[9][9];
        } while (flag);
        return null;
    }


    public static boolean isValid(int[][] board, int row, int col, int c) {
        for (int i = 0; i < 9; i++) {
    //当前列有没有和数字c重复的
            if (board[i][col] == c)
                return false;
    //当前行有没有和数字c重复的
            if (board[row][i] == c)
                return false;
    //当前的3*3单元格内是否有和数字c重复的
            if (board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c)
                return false;
        }
    //否则说明没有重复数字，符合规则
        return true;
    }

    public static boolean backTrace(int[][] board, int row, int col,int num,int
            spr,int spc) {
    //当row等于board.length的时候表示数独的最后一行全部读遍历完了，说明数独中的值是有效的，直接返回true
        if (row == board.length)
            return true;
    //如果当前行的最后一列也遍历完了，就从下一行的第一列开始。这里的遍历
    //顺序是从第1行的第1列一直到最后一列，然后第二行的第一列一直到最后
        if (col == board.length)
            return backTrace(board, row + 1, 0, num, spr, spc);
    //如果当前位置已经有数字了，就不能再填了，直接到这一行的下一列
        if (board[row][col] != 0)
            return backTrace(board, row, col + 1, num, spr, spc);
    //如果上面条件都不满足，就从1到9种选择一个合适的数字填入到数独中
        for (int i = 1; i <= 9; i++) {
    //判断当前位置[row，col]是否可以放数字i，如果不能放再判断下一个能不能放
    //r行c列已经不能再填入num了，用于isOnlyOne方法
            if (i == num && spr == row && spc == col)
                continue;
            if (!isValid(board, row, col, i))
                continue;
    //如果能放数字i，就把数字i放进去
            board[row][col] = i;
    //如果成功就直接返回，不需要再尝试了
            if (backTrace(board, row, col, num, spr, spc))
                return true;
    //否则就撤销重新选择
            board[row][col] = 0;
        }
    //如果当前位置[row，col]不能放任何数字，直接返回false
        return false;
    }

    public static boolean isOnlyOne(int i,int j,int[][] b) { //判断在i,j挖去数字后是否有唯一解
        int k = b[i][j]; //保存待挖洞的原始数字
        for (int num = 1; num < 10; num++) {
            b[i][j] = 0;
            if (num != k && createSudoku_noChange(b, k,i,j)) { //除待挖的数字之外，还有其他的解，则返回false
                b[i][j] = k;
                return false;
            }
        }
        return true; //只有唯一解则返回true
    }

    //num 用做isOnlyOne()的特殊判断标志
    //spr spc表示表示该位置上的元素不能填入num值
    public static boolean createSudoku(int[][] board,int num,int spr,int spc)
    {
        return backTrace(board, 0, 0,num,spr,spc);
    }
    public static boolean createSudoku_noChange(int[][] board,int num,int
            spr,int spc) {
        int temp[][] = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, temp[i], 0, 9);
        }
        return backTrace(temp, 0, 0, num, spr, spc);
    }

    public int[][]getSudokuArr(){
        return SudokuArr;
    }

    public int[][] getAnswerArr() {
        return AnswerArr;
    }
}