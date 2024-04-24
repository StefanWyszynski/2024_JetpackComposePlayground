package com.jetpackcompose.playground.my_algorithms

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 *
 * this is my implementation of simple algorithms.
 * I am trying to make something new. I am curious if there is a better solution and probably is.
 * Anyway this is for fun :)
 *
 * I will probably add more if I have a time to analyze
 */
object MyAlgorithmsImpl {

    /**
     * I know typical implementation is O(n) too but this could be faster because there is less
     * comparisons of type (i % SOME_NUMBER == 0). Anyway this is different and could be faster
     */
    fun myFizzBuzzImplForFun(n: Int): List<String> {
        val fizzBuzzOccuranceText =
            arrayListOf("Fizz", "Buzz", "Fizz", "Fizz", "Buzz", "Fizz", "FizzBuzz")
        val fizzBuzzValues = arrayListOf(3, 5, 3, 3, 5, 3, 3)
        var index = 0
        var currentNum = 3
        val result = ArrayList<String>(n)
        for (i in 1..n) {
            if (i % currentNum == 0) {
                result.add(fizzBuzzOccuranceText[index])
                index = (index + 1) % 7
                currentNum = fizzBuzzValues[index]
            } else {
                result.add(i.toString())
            }
        }
        return result
    }

    /**
     * typical implementation is O(n log(n)) because it uses sorting,
     * so I created implementation with O(n) because it uses counting to zero
     */
    fun myIsAnagramImplForFun(first: String, second: String): Boolean {
        val sumArr = Array(256) { 0 }
        val aChars = first.toCharArray().filter { it.isLetter() }
        val bChars = second.toCharArray().filter { it.isLetter() }
        if (aChars.size != bChars.size) {
            return false
        }
        for (i in 0 until aChars.size) {
            sumArr[aChars[i].code]++
            sumArr[bChars[i].code]--
        }
        return sumArr.all { it == 0 }
        // faster solution is commented because it probably may not work is special cases
//        var sum = 0L
//        for (i in 0 until aChars.size) {
//            sum += aChars[i].code - bChars[i].code
//        }
//        return sum == 0L
    }
}