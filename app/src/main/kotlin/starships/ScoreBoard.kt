package starships

import javafx.geometry.Insets
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class ScoreBoard(private val playerScores:Map<String,Int>, private val playerLives:Map<String,Int>) {
    fun printScoreboard():VBox{
        val vbox = VBox()
        vbox.padding = Insets(10.0)
        vbox.spacing= 8.0
        val title = Text("ScoreBoard")
        vbox.children.add(title)
        playerScores.map {
            val text = Text(it.key + "score : " + it.value)
            val text2 = Text(it.key + "lives: " + playerLives[it.key])
        }
        return vbox
    }
}