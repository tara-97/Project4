package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    private lateinit var remindersLocalRepository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),RemindersDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        remindersLocalRepository = RemindersLocalRepository(database.reminderDao(),Dispatchers.Main)
    }
    @After
    fun cleanUp() {
        database.close()
    }


    @Test
    fun saveReminder_getReminder() = runBlocking {
        val newReminder = ReminderDTO("New Reminder","Description For New Reminder",null,null,null)
        remindersLocalRepository.saveReminder(newReminder)

        val reminderRetrieved =  remindersLocalRepository.getReminder(newReminder.id)

        reminderRetrieved as Result.Success

        assertThat(reminderRetrieved.data.title,`is`("New Reminder"))
        assertThat(reminderRetrieved.data.description,`is`("Description For New Reminder"))
    }

    @Test
    fun saveReminders_getAllReminders_deleteAllReminder() = runBlocking {
        val reminder1 = ReminderDTO("New Reminder 1",null,null,null,null)
        val reminder2 = ReminderDTO("New Reminder 2",null,null,null,null)

        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.saveReminder(reminder2)

        var allReminders = remindersLocalRepository.getReminders()

        allReminders as Result.Success

        assertThat(allReminders.data.size,`is`(2))
        assertThat(allReminders.data[0].title,`is`("New Reminder 1"))
        assertThat(allReminders.data[1].title,`is`("New Reminder 2"))

        remindersLocalRepository.deleteAllReminders()

        allReminders = remindersLocalRepository.getReminders()

        allReminders as Result.Success
        assertThat(allReminders.data.size,`is`(0))

    }





}