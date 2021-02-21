package com.udacity.project4

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.lang.Exception


//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource() : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source
    var reminders :LinkedHashMap<String,ReminderDTO> = LinkedHashMap()

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders?.let { return Result.Success(reminders.values.toList())  }
        return Result.Error("Reminders Not Found")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.put(reminder.id,reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders[id]?.let { return Result.Success(it) }
        return Result.Error("Reminder Not Found")



    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}