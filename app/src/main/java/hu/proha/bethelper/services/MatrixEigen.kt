package hu.proha.bethelper.services

import java.lang.Math.abs

class MatrixEigen {
    companion object {
        fun eigenValues(matrix: Array<DoubleArray>): DoubleArray {
            val n = matrix.size
            val identity = Array(n) { DoubleArray(n) }
            for (i in 0 until n) {
                for (j in 0 until n) {
                    identity[i][j] = if (i == j) 1.0 else 0.0
                }
            }
            val eigenValues = DoubleArray(n)
            for (i in 0 until n) {
                val a = matrix.map { it.copyOf() }.toTypedArray()
                for (j in 0 until n) a[j][j] -= eigenValues[i]
                val x = gaussJordanElimination(a, identity)
                eigenValues[i] = x[n - 1][n - 1]
            }
            return eigenValues
        }
        
        fun eigenVectors(matrix: Array<DoubleArray>, eigenValues: DoubleArray): Array<DoubleArray> {
            val n = matrix.size
            val eigenVectors = Array(n) { DoubleArray(n) }
            for (i in 0 until n) {
                val a = matrix.map { it.copyOf() }.toTypedArray()
                for (j in 0 until n) a[j][j] -= eigenValues[i]
                val x = gaussJordanElimination(a, eigenVectors)
                for (j in 0 until n) eigenVectors[j][i] = x[j][n - 1] / x[n - 1][n - 1]
            }
            return eigenVectors
        }

        fun gaussJordanElimination(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
            val n = a.size
            val m = b[0].size
            for (i in 0 until n) {
                var pivot = i
                for (j in i + 1 until n) {
                    if (abs(a[j][i]) > abs(a[pivot][i])) {
                        pivot = j
                    }
                }
                if (pivot != i) {
                    val tA = a[pivot]
                    a[pivot] = a[i]
                    a[i] = tA
                    val tB = b[pivot]
                    b[pivot] = b[i]
                    b[i] = tB
                }
                val diag = a[i][i]
                for (k in 0 until n) {
                    a[i][k] /= diag
                }
                for (k in 0 until m) {
                    b[i][k] /= diag
                }
                for (j in 0 until n) {
                    if (j == i) continue
                    val factor = a[j][i]
                    for (k in 0 until n) {
                        a[j][k] -= factor * a[i][k]
                    }
                    for (k in 0 until m) {
                        b[j][k] -= factor * b[i][k]
                    }
                }
            }
            return b
        }

    }
}
