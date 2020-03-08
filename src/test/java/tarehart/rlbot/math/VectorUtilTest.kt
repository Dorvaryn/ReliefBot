package tarehart.rlbot.math

import org.junit.Assert
import org.junit.Test
import tarehart.rlbot.math.vector.Vector3

class VectorUtilTest {
    @Test
    @Throws(Exception::class)
    fun getCorrectionAngle() {
        val delta = 0.001F

        // Car on ground, pointed positive X. Wants to turn toward positive Y. That's a left turn.
        Assert.assertEquals(Math.PI.toFloat() / 2, VectorUtil.getCorrectionAngle(
                Vector3(1.0, 0.0, 0.0), Vector3(0.0, 1.0, 0.0), Vector3(0.0, 0.0, 1.0)), delta)

        // Car on side wall, negative X side, pointed straight up. Wants to turn toward positive Y. That's a right turn.
        Assert.assertEquals(-Math.PI.toFloat() / 2, VectorUtil.getCorrectionAngle(
                Vector3(0.0, 0.0, 1.0), Vector3(0.0, 1.0, 0.0), Vector3(1.0, 0.0, 0.0)), delta)

        // Car on side wall, positive X side, pointed straight up. Wants to turn toward positive Y. That's a left turn.
        Assert.assertEquals(Math.PI.toFloat() / 2, VectorUtil.getCorrectionAngle(
                Vector3(0.0, 0.0, 1.0), Vector3(0.0, 1.0, 0.0), Vector3(-1.0, 0.0, 0.0)), delta)

    }

}
