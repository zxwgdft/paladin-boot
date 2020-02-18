package com.paladin.framework.utils.others;

/**
 * 组合排列工具类
 * 
 * @author TontoZhou
 * 
 */
public class ProbabilityUtil {

	/**
	 * 从m个元素中选取n个元素的所有可能排列
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static int[][] A(int m, int n) {
		if (m >= n && n > 0) {
			int probableCount = countA(m, n);

			int[][] result = new int[probableCount][n];

			int[] arr = new int[m];
			for (int i = 0; i < m; i++)
				arr[i] = i;

			A(result, 0, 0, arr, n);

			return result;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 排列
	 * 
	 * @param matrix
	 *            排列矩阵
	 * @param r
	 *            排列矩阵中从第几行开始写入
	 * @param c
	 *            排列矩阵中从第几列开始写入
	 * @param arr
	 *            可以选择的元素
	 * @param n
	 *            选择数量
	 * @return
	 */
	private static int A(int[][] matrix, int r, int c, int[] arr, int n) {
		if (n == 1) {
			for (int i : arr)
				matrix[r++][c] = i;
			return arr.length;
		} else {
			int len = arr.length;
			int subLen = len - 1;
			int total = countA(len, n);
			int oc = c;
			c++;
			n--;
			for (int i = 0; i < len; i++) {
				int[] subArr = new int[subLen];

				for (int j = 0, k = 0; j < len; j++) {
					if (j != i)
						subArr[k++] = arr[j];
				}

				int or = r;
				int v = arr[i];

				r += A(matrix, r, c, subArr, n);

				for (; or < r; or++)
					matrix[or][oc] = v;

			}

			return total;
		}
	}

	/**
	 * 从m个元素中选取n个元素的所有可能组合
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static int[][] C(int m, int n) {
		if (m >= n && n > 0) {
			int probableCount = countC(m, n);

			int[][] result = new int[probableCount][n];

			for (int i = 0; i < n; i++)
				result[0][i] = i;

			C(result, 0, n, m);

			return result;
		} else {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * 从m个元素中选取n个元素的所有可能组合
	 * <p>
	 * 
	 * C(5,3) = (选3*C(2,2))+(选4*C(3,2))+(选5*C(4,2))
	 * 
	 * 选3，4，5意指最后一个元素选择，*号表示所有结果相交
	 * 
	 * </p>
	 * 
	 * @param matrix
	 *            组合矩阵
	 * @param r
	 *            矩阵写入行号
	 * @param n
	 * @param m
	 * @return
	 */
	private static int C(int[][] matrix, int r, int n, int m) {

		if (n == 1) {

			for (int i = 0; i < m; i++) {
				matrix[r++][0] = i;
			}

			return m;
		}

		if (n == m) {

			for (int i = 0; i < m; i++) {
				matrix[r][i] = i;
			}

			return 1;
		}

		int or = r;

		n = n - 1;

		for (int a = n; a < m; a++) {

			int c = C(matrix, r, n, a);

			int o = r;
			r += c;

			for (; o < r; o++)
				matrix[o][n] = a;

		}

		return r - or;
	}

	/**
	 * m个元素所有可能排列
	 * 
	 * @param m
	 * @return
	 */
	public static int[][] P(int m) {
		return A(m, m);
	}

	/**
	 * m个元素中取n次任意元素（可重复）的所有组合
	 * 
	 * @param n
	 * @return
	 */
	public static int[][] AA(int m, int n) {
		if (m >= n && n > 0) {

			int probableCount = (int) Math.pow(m, n);

			int[][] result = new int[probableCount][n];

			for (int i = 0; i < n; i++)
				result[0][i] = i;

			AA(result, 0, 0, n, m);

			return result;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 
	 * @param matrix
	 * @param r
	 * @param c
	 * @param n
	 * @param m
	 * @return
	 */
	private static int AA(int[][] matrix, int r, int c, int n, int m) {
		if (n == 1) {
			for (int i = 0; i < m; i++)
				matrix[r++][c] = i;
			return m;
		} else {

			int oc = c;
			int or = r;
			c++;
			n--;

			for (int i = 0; i < m; i++) {

				int o = r;

				r += AA(matrix, r, c, n, m);

				for (; o < r; o++)
					matrix[o][oc] = i;

			}

			return r - or;
		}

	}

	/**
	 * A排列的所有可能数
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static int countA(int m, int n) {
		int s = 1;
		int to = m - n;
		for (; m > to; m--)
			s *= m;

		return s;
	}

	/**
	 * C选择的所有可能数
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static int countC(int m, int n) {
		return countA(m, n) / countA(n, n);
	}

}
