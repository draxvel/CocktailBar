package com.tkachuk.cocktailbar.util.extension

import java.util.*

fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start