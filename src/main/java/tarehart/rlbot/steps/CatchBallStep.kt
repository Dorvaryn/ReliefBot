package tarehart.rlbot.steps

import tarehart.rlbot.AgentInput
import tarehart.rlbot.AgentOutput
import tarehart.rlbot.TacticalBundle
import tarehart.rlbot.math.SpaceTime
import tarehart.rlbot.math.vector.Vector3
import tarehart.rlbot.physics.ArenaModel
import tarehart.rlbot.intercept.AirTouchPlanner
import tarehart.rlbot.planning.GoalUtil
import tarehart.rlbot.planning.SteerUtil
import tarehart.rlbot.time.Duration

class CatchBallStep : StandardStep() {
    private lateinit var latestCatchLocation: SpaceTime

    override val situation: String
        get() = "Catching ball"

    override fun getOutput(bundle: TacticalBundle): AgentOutput? {

        val car = bundle.myCarData

        val ballPath = ArenaModel.predictBallPath(bundle)
        val catchOpportunity = SteerUtil.getCatchOpportunity(car, ballPath, AirTouchPlanner.getBoostBudget(car)) ?: return null

        // Weed out any intercepts after a catch opportunity. Should just catch it.
        latestCatchLocation = catchOpportunity
        return if (Duration.between(bundle.time, latestCatchLocation.time).seconds > 2) {
            null // Don't wait around for so long
        } else playCatch(bundle, latestCatchLocation)
    }

    private fun playCatch(input: AgentInput, catchLocation: SpaceTime): AgentOutput {
        val car = input.myCarData
        val enemyGoal = GoalUtil.getEnemyGoal(car.team).center
        val enemyGoalToLoc = catchLocation.space.minus(enemyGoal)
        val offset = Vector3(enemyGoalToLoc.x, enemyGoalToLoc.y, 0.0).scaledToMagnitude(1.2)
        val target = catchLocation.space.plus(offset)

        return SteerUtil.getThereOnTime(car, SpaceTime(target, catchLocation.time), true)
    }
}
