import kotlin.math.log
import kotlin.math.round

fun <T : Comparable<T>> singleMergeSort(array: List<T>) : List<T>{
    if (array.size <= 1){
        return array
    }
    val middle = array.size / 2
    val left = array.subList(0,middle);
    val right = array.subList(middle,array.size);
    return merge(singleMergeSort(left), singleMergeSort(right))
}

fun <T : Comparable<T>> merge(left: List<T>, right: List<T>): List<T> {
    var indexLeft = 0
    var indexRight = 0
    val newList : MutableList<T> = mutableListOf()
    while (indexLeft < left.count() && indexRight < right.count()) {
        if (left[indexLeft] <= right[indexRight]) {
            newList.add(left[indexLeft])
            indexLeft++
        } else {
            newList.add(right[indexRight])
            indexRight++
        }
    }
    while (indexLeft < left.size) {
        newList.add(left[indexLeft])
        indexLeft++
    }
    while (indexRight < right.size) {
        newList.add(right[indexRight])
        indexRight++
    }
    return newList
}

fun <T : Comparable<T>> threadMergeSort(array: List<T>, threads: Int):
        List<T> {
    val log = log(threads.toDouble(), 2.0)
    if (log == 0.0) {
        return singleMergeSort(array)
    }
    require(threads > 0 && round(log) == log) { " Number of threads must be power of two and positive " }
    val middle = array.size / 2
    val left = array.subList(0, middle);
    val right = array.subList(middle, array.size);
    var leftResult: List<T> = listOf()
    val thread1 = Thread {
        leftResult = threadMergeSort(left, threads / 2)
    }
    var rightResult: List<T> = listOf()
    val thread2 = Thread {
        rightResult = threadMergeSort(right, threads / 2)
    }
    thread1.start()
    thread2.start()
    thread2.join()
    thread1.join()
    return merge(leftResult, rightResult)

}
