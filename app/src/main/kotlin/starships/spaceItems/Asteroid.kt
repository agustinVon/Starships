package starships.spaceItems

import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import kotlinx.serialization.Serializable
import starships.colliders.AsteroidCollider

class Asteroid(position: Vector2, direction: Vector2, val image: Image, speed: Double, val height: Double, val width: Double): MovableItem(position, direction, speed) {
    val valueInPoints = 100
    var destroyed = false
    fun destroy(){
        destroyed = true
    }
}
