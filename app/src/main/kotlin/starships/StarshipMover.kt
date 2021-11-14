package starships

import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.input.KeyCode
import kotlinx.serialization.Serializable
import starships.spaceItems.Starship
import kotlin.math.cos
import kotlin.math.sin

@Serializable
class StarshipMover(private val up:KeyCode, private val down:KeyCode, private val rotL:KeyCode, private val rotR:KeyCode, private val shoot:KeyCode) {

    fun notifyKeySet(keySet: Set<KeyCode>, secondsSinceLastFrame: Double, starship: Starship) {
        var position = starship.position
        var direction = starship.direction
        val movement = starship.speed * secondsSinceLastFrame
        keySet.forEach{keyCode ->
            when(keyCode){
                up -> position = position.subtract(direction.multiply(movement))
                down -> position = position.add(direction.multiply(movement))
                rotR -> direction = direction.rotate(Math.toRadians(3.0))
                rotL -> direction = direction.rotate(Math.toRadians(-3.0))
                shoot -> starship.prepareToShoot()
                else -> {}
            }
            starship.move(position, direction)
        }
    }
}