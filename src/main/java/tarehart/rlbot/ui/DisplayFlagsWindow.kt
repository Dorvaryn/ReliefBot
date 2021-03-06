package tarehart.rlbot.ui

import java.awt.*
import javax.swing.*

class DisplayFlagsWindow: JPanel()  {

    private val flagAll = JButton()
    private val flagSimplePlan = JButton()
    private val flagDetailedPlan = JButton()
    private val flagBallPath = JButton()
    private val flagCarPath = JButton()
    private val flagDribbleIntercept = JButton()
    private val flagHoopsKickoff = JButton()
    private val flagHoopsGoalPrediction = JButton()
    private val flagGoalCrossing = JButton()
    private val enabledColor = Color(152,251,152)
    private val disabledColor = JButton().background

    init {
        this.layout = FlowLayout()
        this.isEnabled = false

        // Add all of the buttons to the line for this bot
        this.add(flagAll)
        this.add(flagSimplePlan)
        this.add(flagDetailedPlan)
        this.add(flagBallPath)
        this.add(flagCarPath)
        this.add(flagDribbleIntercept)
        this.add(flagHoopsKickoff)
        this.add(flagHoopsGoalPrediction)
        this.add(flagGoalCrossing)

        // Setup the names of all of the buttons
        flagAll.text = "Toggle All"
        flagSimplePlan.text = "Simple Plan"
        flagDetailedPlan.text = "Detailed Plan"
        flagBallPath.text = "Ball Path"
        flagCarPath.text = "Car Path"
        flagDribbleIntercept.text = "Dribble Intercept"
        flagHoopsKickoff.text = "Hoops Kickoff"
        flagHoopsGoalPrediction.text = "Hoops Goal Prediction"
        flagGoalCrossing.text = "Goal Crossing"

        // Make sure that the coloring logic for toggle buttons will show properly in windows look and feel
        flagSimplePlan.isContentAreaFilled = false
        flagSimplePlan.isOpaque = true
        flagDetailedPlan.isContentAreaFilled = false
        flagDetailedPlan.isOpaque = true
        flagBallPath.isContentAreaFilled = false
        flagBallPath.isOpaque = true
        flagCarPath.isContentAreaFilled = false
        flagCarPath.isOpaque = true
        flagDribbleIntercept.isContentAreaFilled = false
        flagDribbleIntercept.isOpaque = true
        flagHoopsKickoff.isContentAreaFilled = false
        flagHoopsKickoff.isOpaque = true
        flagHoopsGoalPrediction.isContentAreaFilled = false
        flagHoopsGoalPrediction.isOpaque = true
        flagGoalCrossing.isContentAreaFilled = false
        flagGoalCrossing.isOpaque = true

        // Set the button colors based on the pre-loaded states of the display flags
        flagSimplePlan.background = if(DisplayFlags[DisplayFlags.SIMPLE_PLAN] == 1) enabledColor else disabledColor
        flagDetailedPlan.background = if(DisplayFlags[DisplayFlags.DETAILED_PLAN] == 1) enabledColor else disabledColor
        flagBallPath.background = if(DisplayFlags[DisplayFlags.BALL_PATH] == 1) enabledColor else disabledColor
        flagCarPath.background = if(DisplayFlags[DisplayFlags.CAR_PATH] == 1) enabledColor else disabledColor
        flagDribbleIntercept.background = if(DisplayFlags[DisplayFlags.DRIBBLE_INTERCEPT] == 1) enabledColor else disabledColor
        flagHoopsKickoff.background = if(DisplayFlags[DisplayFlags.HOOPS_KICKOFF] == 1) enabledColor else disabledColor
        flagHoopsGoalPrediction.background = if(DisplayFlags[DisplayFlags.HOOPS_GOAL_PREDICTION] == 1) enabledColor else disabledColor
        flagGoalCrossing.background = if(DisplayFlags[DisplayFlags.GOAL_CROSSING] == 1) enabledColor else disabledColor

        // Add action listeners for all of the buttons
        flagAll.addActionListener { toggleAllFlags() }
        flagSimplePlan.addActionListener { toggleSimplePlan() }
        flagDetailedPlan.addActionListener { toggleDetailedPlan() }
        flagBallPath.addActionListener { toggleBallPath() }
        flagCarPath.addActionListener { toggleCarPath() }
        flagDribbleIntercept.addActionListener { toggleDribbleIntercept() }
        flagHoopsKickoff.addActionListener { toggleHoopsKickoff() }
        flagHoopsGoalPrediction.addActionListener { toggleHoopsGoalPrediction() }
        flagGoalCrossing.addActionListener { toggleGoalCrossing() }
    }

    private fun toggleAllFlags() {
        DisplayFlags.toggleAllFlags()
        updateColors()
    }

    fun updateColors() {
        var flagVal = DisplayFlags[DisplayFlags.ALL]
        val color = if(flagVal == 1) enabledColor else disabledColor
        flagSimplePlan.background = color
        flagDetailedPlan.background = color
        flagBallPath.background = color
        flagCarPath.background = color
        flagDribbleIntercept.background = color
        flagHoopsKickoff.background = color
        flagHoopsGoalPrediction.background = color
        flagGoalCrossing.background = color
    }

    private fun toggleSimplePlan() {
        val flagVal = DisplayFlags[DisplayFlags.SIMPLE_PLAN]
        DisplayFlags[DisplayFlags.SIMPLE_PLAN] = if(flagVal == 1) 0 else 1
        flagSimplePlan.background = if(flagVal == 1) enabledColor else disabledColor
    }

    private fun toggleDetailedPlan() {
        val flagVal = DisplayFlags[DisplayFlags.DETAILED_PLAN]
        DisplayFlags[DisplayFlags.DETAILED_PLAN] = if(flagVal == 1) 0 else 1
        flagDetailedPlan.background = if(flagVal == 1) enabledColor else disabledColor
    }

    private fun toggleBallPath() {
        val flagVal = DisplayFlags[DisplayFlags.BALL_PATH]
        DisplayFlags[DisplayFlags.BALL_PATH] = if(flagVal == 1) 0 else 1
        flagBallPath.background = if(flagVal == 1) enabledColor else disabledColor
    }

    private fun toggleCarPath() {
        val flagVal = DisplayFlags[DisplayFlags.CAR_PATH]
        DisplayFlags[DisplayFlags.CAR_PATH] = if(flagVal == 1) 0 else 1
        flagCarPath.background = if(flagVal == 1) enabledColor else disabledColor
    }

    private fun toggleDribbleIntercept() {
        val flagVal = DisplayFlags[DisplayFlags.DRIBBLE_INTERCEPT]
        DisplayFlags[DisplayFlags.DRIBBLE_INTERCEPT] = if(flagVal == 1) 0 else 1
        flagDribbleIntercept.background = if(flagVal == 1) enabledColor else disabledColor
    }

    private fun toggleHoopsKickoff() {
        val flagVal = DisplayFlags[DisplayFlags.HOOPS_KICKOFF]
        DisplayFlags[DisplayFlags.HOOPS_KICKOFF] = if(flagVal == 1) 0 else 1
        flagHoopsKickoff.background = if(flagVal == 1) enabledColor else disabledColor
    }

    private fun toggleHoopsGoalPrediction() {
        val flagVal = DisplayFlags[DisplayFlags.HOOPS_GOAL_PREDICTION]
        DisplayFlags[DisplayFlags.HOOPS_GOAL_PREDICTION] = if(flagVal == 1) 0 else 1
        flagHoopsGoalPrediction.background = if(flagVal == 1) enabledColor else disabledColor
    }

    private fun toggleGoalCrossing() {
        val flagVal = DisplayFlags[DisplayFlags.GOAL_CROSSING]
        DisplayFlags[DisplayFlags.GOAL_CROSSING] = if(flagVal == 1) 0 else 1
        flagGoalCrossing.background = if(flagVal == 1) enabledColor else disabledColor
    }
}
