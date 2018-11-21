package tarehart.rlbot.bots

import tarehart.rlbot.AgentInput
import tarehart.rlbot.AgentOutput
import tarehart.rlbot.physics.ArenaModel
import tarehart.rlbot.planning.*
import tarehart.rlbot.steps.GetBoostStep
import tarehart.rlbot.steps.GetOnOffenseStep
import tarehart.rlbot.steps.GoForKickoffStep
import tarehart.rlbot.steps.defense.WhatASaveStep
import tarehart.rlbot.steps.demolition.DemolishEnemyStep
import tarehart.rlbot.steps.strikes.FlexibleKickStep
import tarehart.rlbot.steps.strikes.KickAtEnemyGoal
import tarehart.rlbot.tactics.SoccerTacticsAdvisor
import tarehart.rlbot.tactics.TacticalSituation
import tarehart.rlbot.tactics.TacticsAdvisor
import tarehart.rlbot.tuning.BotLog

class AdversityBot(team: Team, playerIndex: Int) : BaseBot(team, playerIndex) {

    private val tacticsAdvisor: TacticsAdvisor = SoccerTacticsAdvisor()

    override fun getOutput(bundle: TacticalBundle): AgentOutput {

        val car = input.myCarData
        val ballPath = ArenaModel.predictBallPath(input)
        val situation = tacticsAdvisor.assessSituation(input, ballPath, currentPlan)

        findMoreUrgentPlan(input, situation, currentPlan)?.let {
            currentPlan = it
        }

        if (Plan.activePlanKt(currentPlan) == null) {
            currentPlan = makeFreshPlan(input, situation)
        }

        currentPlan?.let {
            if (it.isComplete()) {
                currentPlan = null
            } else {
                it.getOutput(input)?.let { return it }
            }
        }

        return SteerUtil.steerTowardGroundPositionGreedily(car, input.ballPosition.flatten()).withBoost(false)
    }

    private fun makeFreshPlan(bundle: TacticalBundle, situation: TacticalSituation): Plan {
        val car = input.myCarData

        val plan = FirstViableStepPlan(Plan.Posture.NEUTRAL).withStep(DemolishEnemyStep())

        if (situation.shotOnGoalAvailable && situation.teamPlayerWithInitiative.car == car) {
            plan.withStep(FlexibleKickStep(KickAtEnemyGoal()))
        }

        plan.withStep(GetBoostStep())
        plan.withStep(GetOnOffenseStep())
        return plan
    }

    private fun findMoreUrgentPlan(bundle: TacticalBundle, situation: TacticalSituation, currentPlan: Plan?): Plan? {

        val car = input.myCarData

        // NOTE: Kickoffs can happen unpredictably because the bot doesn't know about goals at the moment.
        if (Plan.Posture.KICKOFF.canInterrupt(currentPlan) && situation.goForKickoff && situation.teamPlayerWithInitiative.car == car) {
            return Plan(Plan.Posture.KICKOFF).withStep(GoForKickoffStep())
        }

        if (situation.scoredOnThreat != null && situation.teamPlayerWithInitiative.car == car && Plan.Posture.SAVE.canInterrupt(currentPlan)) {
            BotLog.println("Canceling current plan. Need to go for save!", input.playerIndex)
            return Plan(Plan.Posture.SAVE).withStep(WhatASaveStep())
        }

        return null
    }
}
