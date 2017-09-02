package tarehart.rlbot.planning;

import tarehart.rlbot.AgentOutput;
import tarehart.rlbot.steps.*;

import java.time.Duration;

public class SetPieces {

    public static Plan frontFlip() {

        return new Plan()
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withPitch(-1)
                                .withJump(true)
                                .withAcceleration(1)))
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withPitch(-1)
                                .withAcceleration(1)
                ))
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withJump(true)
                                .withAcceleration(1)
                                .withPitch(-1)))
                .withStep(new BlindStep(
                        new AgentOutput()
                                .withAcceleration(1)
                                .withPitch(-1),
                        Duration.ofMillis(50)
                ))
                .withStep(new LandMindlesslyStep());
    }

    public static Plan chaseBall() {
        return new Plan()
                .withStep(new ChaseBallStep());
    }

    public static Plan getBoostAndAerial() {
        return new Plan()
                .withStep(new GetBoostStep())
                .withStep(new TurnTowardCenterStep())
                .appendPlan(performAerial());
    }

    public static Plan performAerial() {
        return new Plan()
                .withStep(new BlindStep(
                        new AgentOutput()
                            .withJump(true)
                            .withPitch(1),
                        Duration.ofMillis(360)
                ))
                .withStep(new BlindStep(
                        new AgentOutput()
                            .withJump(true)
                            .withPitch(-1)
                            .withBoost(true),
                        Duration.ofMillis(360)
                ))
                .withStep(new MidairStrikeStep())
                .withStep(new LandGracefullyStep());
    }

}