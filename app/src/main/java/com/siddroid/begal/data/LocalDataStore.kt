package com.siddroid.begal.data

import com.siddroid.begal.data.model.BegalDTO
import java.util.LinkedList

class LocalDataStore {
    companion object {
        private val FORWARD_ERROR = BegalDTO("Reached last element", "error")
        private val BACKWARD_ERROR = BegalDTO("Reached first element", "error")
    }

    private var localData: LinkedList<BegalDTO> = LinkedList()
    var currentIndex: Int = -1
    private set
    private var size: Int = -1

    fun getNext(): BegalDTO {
        return if (canGoForward()) {
            localData[++currentIndex]
        } else {
            FORWARD_ERROR
        }
    }

    fun addToLocalData(begalDTO: BegalDTO, maxEntries: Int) {
        if (size == maxEntries) {
            localData.removeFirst()
            --size
        }
        localData.add(begalDTO)
        currentIndex = ++size
    }

    fun getPrevious(): BegalDTO {
        return if (canGoBackward()) {
            localData[--currentIndex]
        } else {
            BACKWARD_ERROR
        }
    }

    private fun canGoForward(): Boolean {
        return currentIndex < size
    }

    private fun canGoBackward(): Boolean {
        return currentIndex > 0
    }
}