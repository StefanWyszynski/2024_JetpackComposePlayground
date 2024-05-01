package com.jetpackcompose.playground.my_algorithms

import org.junit.Assert.*
import org.junit.Test

class MyAlgorithmsImplTest{

    @Test
    fun test_myFizzBuzz() {
        val fizzBuzzResult = MyAlgorithmsImpl.myFizzBuzzImplForFun(100)

        // compare with typical fizz buzz implementation
        val arr = arrayListOf<String>()
        for (i in 1..100) {
            when {
                i % 3 == 0 && i % 5 == 0 -> arr.add("FizzBuzz")
                i % 3 == 0 -> arr.add("Fizz")
                i % 5 == 0 -> arr.add("Buzz")
                else -> arr.add(i.toString())
            }
        }

        assertEquals(arr, fizzBuzzResult)
    }

    @Test
    fun test_myAnagram() {
        // anagram
        assertEquals(true, MyAlgorithmsImpl.myIsAnagramImplForFun("listen", "silent"))
        assertEquals(true, MyAlgorithmsImpl.myIsAnagramImplForFun("debit card", "bad credit"))
        assertEquals(true, MyAlgorithmsImpl.myIsAnagramImplForFun("astronomer", "moon starer"))

        // not anagram
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun("hello", "world"))
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun("apple", "orange"))
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun("cat", "dog"))
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun("aa", "bbbbb"))
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun("bbbbb", "aa"))
    }

    @Test
    fun test_myAnagram2() {
        // anagram
        assertEquals(true, MyAlgorithmsImpl.myIsAnagramImplForFun2("listen", "silent"))
        assertEquals(true, MyAlgorithmsImpl.myIsAnagramImplForFun2("debit card", "bad credit"))
        assertEquals(true, MyAlgorithmsImpl.myIsAnagramImplForFun2("astronomer", "moon starer"))

        // not anagram
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun2("hello", "world"))
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun2("apple", "orange"))
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun2("cat", "dog"))
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun2("aa", "bbbbb"))
        assertEquals(false, MyAlgorithmsImpl.myIsAnagramImplForFun2("bbbbb", "aa"))
    }
}