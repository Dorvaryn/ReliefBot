package tarehart.rlbot.routing

import tarehart.rlbot.math.DistanceTimeSpeed
import tarehart.rlbot.math.vector.Vector2
import tarehart.rlbot.physics.DistancePlot
import tarehart.rlbot.time.Duration
import tarehart.rlbot.tuning.ManeuverMath


object RoutePlanner{

    fun arriveWithSpeed(start: Vector2, end: Vector2, speed: Float, distancePlot: DistancePlot): List<RoutePart>? {

        val distanceDuration = getDecelerationDistanceWhenTargetingSpeed(start, end, speed, distancePlot)

        val distance = start.distance(end)
        val maxAccelMotion = distancePlot.getMotionAfterDistance(distance) ?: return null

        if (distanceDuration.distance == 0F) {
            return listOf(AccelerationRoutePart(start, end, maxAccelMotion.time))
        }

        if (distanceDuration.distance > distance) {
            // This may overestimate the time if we can't decelerate fast enough
            return listOf(DecelerationRoutePart(start, end, distanceDuration.duration))
        }

        val inflectionPoint = end + (start - end).scaledToMagnitude(distanceDuration.distance)
        val inflectionTime = distancePlot.getTravelTime(distance - distanceDuration.distance) ?: return null

        return listOf(
                AccelerationRoutePart(start, inflectionPoint, inflectionTime),
                DecelerationRoutePart(inflectionPoint, end, distanceDuration.duration))
    }

    fun getDecelerationDistanceWhenTargetingSpeed(start: Vector2, end: Vector2, desiredSpeed: Float, distancePlot: DistancePlot): DistanceDuration {

        val distance = start.distance(end)
        // Assume we'll only accelerate half way.
        val maxAccelMotion = distancePlot.getMotionAfterDistance(distance / 2) ?: return DistanceDuration(0F, Duration.ofMillis(0))

        if (maxAccelMotion.speed <= desiredSpeed) {
            return DistanceDuration(0F, Duration.ofMillis(0))
        }

        val speedDiff = maxAccelMotion.speed - desiredSpeed
        val secondsDecelerating = speedDiff / ManeuverMath.BRAKING_DECELERATION
        val avgSpeed = (maxAccelMotion.speed + desiredSpeed) / 2
        return DistanceDuration(avgSpeed * secondsDecelerating, Duration.ofSeconds(secondsDecelerating))
    }

    fun getMotionAfterSpeedChange(currentSpeed: Float, idealSpeed: Float, forwardAccelPlot: DistancePlot): DistanceTimeSpeed? {

        if (idealSpeed < currentSpeed) {
            val secondsRequired = (currentSpeed - idealSpeed) / ManeuverMath.BRAKING_DECELERATION
            val avgSpeed = (currentSpeed + idealSpeed) / 2
            return DistanceTimeSpeed(
                    avgSpeed * secondsRequired,
                    Duration.ofSeconds(secondsRequired),
                    idealSpeed)
        }

        return forwardAccelPlot.getMotionUponSpeed(idealSpeed)
    }
}
