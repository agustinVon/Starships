package starships

import javafx.scene.input.KeyCode
import kotlinx.serialization.Serializable
import starships.spaceItems.Starship

class Player(val shipMover: StarshipMover, val starship: Starship) {
    var score = 0
    var hasLost = false
    fun handleKeyPress(keySet: Set<KeyCode>, secondsSinceLastFrame: Double){
        shipMover.notifyKeySet(keySet,secondsSinceLastFrame, starship)
    }
    fun lose(){
        hasLost = true
    }

    fun addScore(score: Int) {
        this.score += score
    }
}