
import space.kscience.dataforge.meta.configure
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.trace
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main(){
    var counter = 10
    val secondsSingle = mutableListOf<Long>()
    val secondsThread2 = mutableListOf<Long>()
    val secondsThread4 = mutableListOf<Long>()
    val secondsThread8 = mutableListOf<Long>()
    val secondsThread16 = mutableListOf<Long>()

    val size = mutableListOf<Int>()
    for (i in 0 until 6) {
        val array = createArray(counter)
        secondsSingle.add(measureTimeMillis { mergeSort(array) })
        secondsThread2.add(measureTimeMillis { threadMergeSort(array, 2) })
        secondsThread4.add(measureTimeMillis { threadMergeSort(array, 4) })
        secondsThread8.add(measureTimeMillis { threadMergeSort(array, 8) })
        secondsThread16.add(measureTimeMillis { threadMergeSort(array, 16) })
        size.add(array.size)
        counter *= 10
    }
    val commonList = mutableListOf(secondsSingle, secondsThread2, secondsThread4, secondsThread8, secondsThread16)

    val plot = Plotly.plot {
        for (i in commonList.indices) {
            trace {
                configure {
                    "type" put "scatter2d"
                }
                x.set(commonList[i])
                y.set(size)
                name = if (i == 0) "Single" else "Parallel, ${(2.0.pow(i + 0.0)).toInt()} threads"
            }
        }
        layout {
            title = "Comparison single sort and multithreaded parallel sort"
            xaxis {
                title = "Time, ms"
            }
            yaxis {
                title = "Array size, int"
            }
        }
    }
    plot.makeFile()


    val threads = mutableListOf<Int>()
    val time = mutableListOf<Long>()
    val arrayOneMillion = createArray(1000000)

    for (i in 0 until 10) {
        val power = 2.0.pow(i + 0.0).toInt()
        threads.add(power)
        time.add(measureTimeMillis { threadMergeSort(arrayOneMillion, power) })
    }

    val plotAmdahl = Plotly.plot {

        trace {
            configure {
                "type" put "scatter2d"
            }
            x.set(threads)
            y.set(time)
        }

        layout {
            title = "Amdahl`s law"
            xaxis {
                title = "Threads"
            }
            yaxis {
                title = "Time, ms"
            }
        }
    }
    plotAmdahl.makeFile()

}

fun createArray(size: Int): MutableList<Int> {
    val array = mutableListOf<Int>()
    for (i in 0 until size) {
        array.add((-100000..100000).random())
    }
    return array
}