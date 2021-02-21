package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    //TODO: provide testing to the RemindersListViewModel and its live data objects
    private lateinit var dataSource: FakeDataSource
    private lateinit var viewModel: RemindersListViewModel
    @Before
    fun setUpRepository() {
        dataSource = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),dataSource)

    }

    @Test
    fun loadData_updateReminderList() =mainCoroutineRule.runBlockingTest{

        val reminder = ReminderDTO("Title","Description","Location",null,null)
        dataSource.saveReminder(reminder)

        mainCoroutineRule.pauseDispatcher()
        viewModel.loadReminders()
        assertThat(viewModel.showLoading.getOrAwaitValue(),`is`(true))

        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.showLoading.getOrAwaitValue(),`is`(false))

        val reminderList = viewModel.remindersList.getOrAwaitValue()[0]
        assertThat(reminderList.id,`is`(equalTo(reminder.id)))
        assertThat(reminderList.title,`is`(equalTo(reminder.title)))

    }

    @Test
    fun loadRemindersWhenNoReminders_showErrorToUser(){
        dataSource.setErrorState(true)

        viewModel.loadReminders()

        assertThat(viewModel.showSnackBar.getOrAwaitValue(),`is`("Test Exception"))
        assertThat(viewModel.showNoData.getOrAwaitValue(),`is`(true))
    }

    @After
    fun tearDown() {
        stopKoin()
    }





}