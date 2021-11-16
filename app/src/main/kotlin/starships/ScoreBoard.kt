package starships

import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text

class ScoreBoard() {
    val vBox = VBox()
    var winnerPlayer = ""
    var winnerScore = 0
    init {
        vBox.padding = Insets(10.0)
        vBox.spacing= 8.0
    }
    fun updateScoreboard(playerScores:Map<String,Int>, playerLives:Map<String,Int>){
        vBox.children.removeAll(vBox.children)
        val title = Text("ScoreBoard")
        vBox.children.add(title)
        title.fill = Color.WHITE
        playerScores.map {
            if(it.value > winnerScore){
                winnerScore = it.value
                winnerPlayer = it.key
            }
            val text = Text(it.key + " score : " + it.value)
            text.fill = Color.WHITE
            vBox.children.add(text)
            val text2 = Text(it.key + " lives: " + playerLives[it.key])
            text2.fill = Color.WHITE
            vBox.children.add(text2)
        }
    }

    fun getWinnerPlayerAndScore(): String {
        return "The winner is $winnerPlayer with $winnerScore points"
    }
}