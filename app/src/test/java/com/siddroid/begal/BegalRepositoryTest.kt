package com.siddroid.begal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.siddroid.begal.core.network.NetworkStatusHelper
import com.siddroid.begal.core.network.Resource
import com.siddroid.begal.data.BegalRepositoryImpl
import com.siddroid.begal.data.LocalDataStore
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO
import com.siddroid.begal.data.network.BegalService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class BegalRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val service = mock<BegalService>()
    private val networkHelper = mock<NetworkStatusHelper>()
    private val localData = mock<LocalDataStore>()

    private val repo = BegalRepositoryImpl(service, networkHelper, localData)

    @Test
    fun `should return network error when internet not available while fetching multiple image from network`() = runTest {
        givenInternetIsNotAvailable()

        val response = repo.getImagesFromNetwork(3)

        Assert.assertEquals(Resource.Status.NO_INTERNET, response.status)
    }

    @Test
    fun `should return unknown error incase of message or status is null or blank in response while fetching multiple images from network`() = runTest {
        givenInternetIsAvailable()
        givenGetRandomImagesFailedWithNullMessage(3)

        val response = repo.getImagesFromNetwork(3)

        Assert.assertEquals(Resource.Status.UNKNOWN, response.status)
    }

    @Test
    fun `should return error incase of status is not success in response while fetching multiple images from network`() = runTest {
        givenInternetIsAvailable()
        givenGetRandomImagesFailedWithUnsuccessfulStatus(3)

        val response = repo.getImagesFromNetwork(3)

        Assert.assertEquals(Resource.Status.ERROR, response.status)
    }

    @Test
    fun `should return success incase of status is success in response while fetching multiple images from network`() = runTest {
        givenInternetIsAvailable()
        givenGetRandomImagesSuccessfulStatus(3)

        val response = repo.getImagesFromNetwork(3)

        Assert.assertEquals(Resource.Status.SUCCESS, response.status)
    }

    @Test
    fun `should return network error when internet not available while fetching single image from network`() = runTest {
        givenInternetIsNotAvailable()

        val response = repo.getImageFromNetwork()

        Assert.assertEquals(Resource.Status.NO_INTERNET, response.status)
    }

    @Test
    fun `should return unknown error incase of message or status is null or blank in response`() = runTest {
        givenInternetIsAvailable()
        givenGetRandomImageFailedWithNullMessage()

        val response = repo.getImageFromNetwork()

        Assert.assertEquals(Resource.Status.UNKNOWN, response.status)
    }

    @Test
    fun `should return error incase of status is not success in response`() = runTest {
        givenInternetIsAvailable()
        givenGetRandomImageFailedWithUnsuccessfulStatus()

        val response = repo.getImageFromNetwork()

        Assert.assertEquals(Resource.Status.ERROR, response.status)
    }

    @Test
    fun `should return success incase of status is success in response`() = runTest {
        givenInternetIsAvailable()
        givenGetRandomImageSuccessfulStatus()

        val response = repo.getImageFromNetwork()

        Assert.assertEquals(Resource.Status.SUCCESS, response.status)
    }

    @Test
    fun `should return resorce successfully whenever getNext is called`() = runTest {
        givenGetNextAlwaysReturnSuccess()

        val response = repo.getNextImageFromLocal()

        Assert.assertEquals(Resource.Status.SUCCESS, response.status)
    }

    @Test
    fun `should return resorce successfully whenever getPrevious is called`() = runTest {
        givenGetPreviousAlwaysReturnSuccess()

        val response = repo.getPreviousImageFromLocal()

        Assert.assertEquals(Resource.Status.SUCCESS, response.status)
    }

    private fun givenGetPreviousAlwaysReturnSuccess() {
        whenever(localData.getPrevious()).thenReturn(BegalDTO("dummy", "dummy"))
    }

    private fun givenGetNextAlwaysReturnSuccess() {
        whenever(localData.getNext()).thenReturn(BegalDTO("dummy", "dummy"))
    }

    private fun givenInternetIsNotAvailable() {
        whenever(networkHelper.isConnected()).thenReturn(false)
    }

    private fun givenInternetIsAvailable() {
        whenever(networkHelper.isConnected()).thenReturn(true)
    }

    private suspend fun givenGetRandomImageFailedWithNullMessage() {
        whenever(service.getRandomImage()).thenReturn(BegalDTO(null, null))
    }

    private suspend fun givenGetRandomImagesFailedWithNullMessage(count: Int) {
        whenever(service.getRandomImage(count)).thenReturn(BegalListDTO(null, null))
    }

    private suspend fun givenGetRandomImageFailedWithUnsuccessfulStatus() {
        whenever(service.getRandomImage()).thenReturn(BegalDTO("dummy", "dummy"))
    }

    private suspend fun givenGetRandomImagesFailedWithUnsuccessfulStatus(count: Int) {
        whenever(service.getRandomImage(count)).thenReturn(BegalListDTO(listOf("dummy"), "dummy"))
    }

    private suspend fun givenGetRandomImageSuccessfulStatus() {
        whenever(service.getRandomImage()).thenReturn(BegalDTO("dummy", "success"))
    }

    private suspend fun givenGetRandomImagesSuccessfulStatus(count: Int) {
        whenever(service.getRandomImage(count)).thenReturn(
            BegalListDTO(listOf("dummy"), "success")
        )
    }
}