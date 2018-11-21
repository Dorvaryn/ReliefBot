package tarehart.rlbot.steps.defense

import tarehart.rlbot.AgentInput
import tarehart.rlbot.AgentOutput
import tarehart.rlbot.math.vector.Vector2
import tarehart.rlbot.planning.Plan
import tarehart.rlbot.planning.ZoneTelemetry
import tarehart.rlbot.steps.NestedPlanStep
import tarehart.rlbot.steps.travel.ParkTheCarStep
import tarehart.rlbot.tactics.SoccerTacticsAdvisor
import tarehart.rlbot.tactics.TacticsTelemetry

class RotateAndWaitToClearStep : NestedPlanStep() {


    override fun doComputationInLieuOfPlan(bundle: TacticalBundle): AgentOutput? {

        val positionFacing = GetOnDefenseStep.calculatePositionFacing(input)
        val car = input.myCarData

        val positionError = car.position.flatten().distance(positionFacing.position)
        val facingError = Vector2.angle(car.orientation.noseVector.flatten(), positionFacing.facing)

        if (positionError < 5 && facingError < Math.PI / 8) {
            return AgentOutput()
        }

        val plan = Plan(Plan.Posture.DEFENSIVE)
                .withStep(ParkTheCarStep { inp -> GetOnDefenseStep.calculatePositionFacing(inp) })

        return startPlan(plan, input)
    }

    override fun getLocalSituation(): String {
        return "Rotating and waiting to clear"
    }

    override fun shouldCancelPlanAndAbort(bundle: TacticalBundle): Boolean {
        val tacticalSituation = TacticsTelemetry[input.myCarData.playerIndex]
        val zonePlan = ZoneTelemetry[input.playerIndex]
        return !SoccerTacticsAdvisor.getWaitToClear(zonePlan, input, tacticalSituation?.enemyPlayerWithInitiative?.car)
                || tacticalSituation?.ballAdvantage?.seconds?.let { it > 0.8 } ?: false
    }

}


