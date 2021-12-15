

fun <T : Comparable<T>> mergeSort(array: List<T>) : List<T>{
    if (array.size <= 1){
        return array
    }
    val middle = array.size / 2

    return merge(mergeSort(array.subList(0,middle)), mergeSort(array.subList(middle,array.size)))
}

fun <T : Comparable<T>> merge(left: List<T>, right: List<T>): List<T> {
    var indexLeft = 0
    var indexRight = 0
    val result : MutableList<T> = mutableListOf()
    while (indexLeft < left.count() && indexRight < right.count()) {
        if (left[indexLeft] <= right[indexRight]) {
            result.add(left[indexLeft])
            indexLeft++
        } else {
            result.add(right[indexRight])
            indexRight++
        }
    }
    while (indexLeft < left.size) {
        result.add(left[indexLeft])
        indexLeft++
    }
    while (indexRight < right.size) {
        result.add(right[indexRight])
        indexRight++
    }
    return result
}

fun <T : Comparable<T>> threadMergeSort(array: List<T>, threads: Int): List<T> {
    require(threads > 0 && (threads and (threads - 1)) == 0) { " Number of threads must be power of two and positive " }
    if (threads == 1) {
        return mergeSort(array)
    }
    val middle = array.size / 2
    val left = array.subList(0, middle)
    val right = array.subList(middle, array.size)
    var leftResult: List<T> = listOf()
    var rightResult: List<T> = listOf()

    val thread1 = Thread {
        leftResult = threadMergeSort(left, threads / 2)
    }
    val thread2 = Thread {
        rightResult = threadMergeSort(right, threads / 2)
    }

    thread1.start()
    thread2.start()
    thread2.join()
    thread1.join()
    return merge(leftResult, rightResult)
}
