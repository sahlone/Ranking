package models

import org.joda.time.DateTime

case class Trip(tripId: String,
                departure: Long, //time in milliseconds,
                tripTime: Float,
                noOfConnections: Int,
                tripDistance: Float,
                tripPrice: Float)
