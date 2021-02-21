package com.udacity.project4
import java.util.concurrent.TimeUnit
object GeofencingConstants {

    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.HOURS.toMillis(1)
    val GEOFENCE_RADIUS_IN_METERS: Float = 100f
    val ACTION_GEOFENCE_EVENT = "action_geofence_event"
}
