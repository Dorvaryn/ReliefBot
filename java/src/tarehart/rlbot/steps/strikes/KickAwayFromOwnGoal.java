package tarehart.rlbot.steps.strikes;

import tarehart.rlbot.AgentInput;
import tarehart.rlbot.input.CarData;
import tarehart.rlbot.math.VectorUtil;
import tarehart.rlbot.math.vector.Vector2;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.planning.GoalUtil;

public class KickAwayFromOwnGoal implements KickStrategy {


    @Override
    public Vector3 getKickDirection(AgentInput input) {
        return getKickDirection(input, input.getBallPosition());
    }

    @Override
    public Vector3 getKickDirection(AgentInput input, Vector3 ballPosition) {
        CarData car = input.getMyCarData();
        Vector3 toBall = ballPosition.minus(car.getPosition());
        return getDirection(input.getMyCarData(), ballPosition, toBall);
    }

    @Override
    public Vector3 getKickDirection(AgentInput input, Vector3 ballPosition, Vector3 easyKick) {
        return getDirection(input.getMyCarData(), ballPosition, easyKick);
    }

    @Override
    public boolean looksViable(CarData car, Vector3 ballPosition) {
        return true;
    }

    private Vector3 getDirection(CarData car, Vector3 ballPosition, Vector3 easyKick) {
        Vector2 easyKickFlat = easyKick.flatten();
        Vector2 toLeftPost = GoalUtil.INSTANCE.getOwnGoal(car.getTeam()).getLeftPost().minus(ballPosition).flatten();
        Vector2 toRightPost = GoalUtil.INSTANCE.getOwnGoal(car.getTeam()).getRightPost().minus(ballPosition).flatten();

        Vector2 safeDirectionRight = VectorUtil.INSTANCE.rotateVector(toRightPost, -Math.PI/4);
        Vector2 safeDirectionLeft = VectorUtil.INSTANCE.rotateVector(toLeftPost, Math.PI/4);

        double rightToEasyCorrection = safeDirectionRight.correctionAngle(easyKickFlat, false);
        double rightToLeftCorrection = safeDirectionRight.correctionAngle(safeDirectionLeft, false);
        double safeRightCorrection = easyKickFlat.correctionAngle(safeDirectionRight);
        double safeLeftCorrection = easyKickFlat.correctionAngle(safeDirectionLeft);
        if (rightToLeftCorrection < rightToEasyCorrection) {
            // The easy kick is already wide. Go with the easy kick.
            return new Vector3(easyKickFlat.getX(), easyKickFlat.getY(), 0);
        } else if (Math.abs(safeRightCorrection) < Math.abs(safeLeftCorrection)) {
            return new Vector3(safeDirectionRight.getX(), safeDirectionRight.getY(), 0);
        } else {
            return new Vector3(safeDirectionLeft.getX(), safeDirectionLeft.getY(), 0);
        }
    }
}
