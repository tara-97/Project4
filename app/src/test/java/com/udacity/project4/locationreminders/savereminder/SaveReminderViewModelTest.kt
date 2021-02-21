package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private lateinit var reminderDataSource: FakeDataSource
    private lateinit var viewModel:SaveReminderViewModel

    //TODO: provide testing to the SaveReminderView and its live data objects
    @Before
    fun setupSaveReminderViewModelTest(){
        reminderDataSource = FakeDataSource()
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),reminderDataSource)
    }
    @Test
    fun saveReminder_saveToDataSource() {
        val item = ReminderDataItem("Title","Description","Location",null,null)

        mainCoroutineRule.pauseDispatcher()
        viewModel.saveReminder(item)
        assertThat(viewModel.showLoading.getOrAwaitValue(),`is`(true))

        mainCoroutineRule.resumeDispatcher()

        val toastVal = viewModel.showToast.getOrAwaitValue()
        val navCommandValue = viewModel.navigationCommand.getOrAwaitValue()
        val loadingValue = viewModel.showLoading.getOrAwaitValue()

        assertThat(reminderDataSource.reminders[item.id]?.title,`is`("Title"))
        assertThat(toastVal,`is`("Reminder Saved !"))
        assertThat(navCommandValue, instanceOf(NavigationCommand::class.java))
        assertThat(loadingValue,`is`(false))

    }

    @Test
    fun validateAndSaveReminder_emptyNullTitle_ShowSnackbarEmptySource(){
        val emptyTitleItem = ReminderDataItem("","Description","Location",null,null)
        val nullTitleItem = ReminderDataItem(null,"Description","Location",null,null)
        viewModel.validateAndSaveReminder(emptyTitleItem)
        viewModel.validateAndSaveReminder(nullTitleItem)
        val snackBarTestForEmptyTitle = viewModel.showSnackBarInt.getOrAwaitValue()
        val snackBarTestForNullTitle = viewModel.showSnackBarInt.getOrAwaitValue()

        assertThat(snackBarTestForEmptyTitle,`is`(R.string.err_enter_title))
        assertThat(snackBarTestForNullTitle,`is`(R.string.err_enter_title))
    }

    @After
    fun tearDown() {
        stopKoin()
    }



}