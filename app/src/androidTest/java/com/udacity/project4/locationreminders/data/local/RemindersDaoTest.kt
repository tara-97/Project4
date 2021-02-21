package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }
    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetTaskById() = runBlockingTest{
        val reminder = ReminderDTO("Title","Description","Location",null,null)
        database.reminderDao().saveReminder(reminder)

        val loaded = database.reminderDao().getReminderById(reminder.id)
        assertThat(loaded as ReminderDTO,notNullValue())
        assertThat(loaded.title,`is`("Title"))
        assertThat(loaded.description,`is`("Description"))
        assertThat(loaded.location,`is`("Location"))
    }

    @Test
    fun insertTasksAndDeleteTasks() = runBlockingTest {
        val reminder1 = ReminderDTO("New Reminder 1",null,null,null,null)
        val reminder2 = ReminderDTO("New Reminder 2",null,null,null,null)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)

        var allReminders = database.reminderDao().getReminders()
        assertThat(allReminders.size,`is`(2))

        database.reminderDao().deleteAllReminders()
        allReminders = database.reminderDao().getReminders()
        assertThat(allReminders.size,`is`(0))


    }



}