package starships.spaceItems

import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import kotlinx.serialization.Serializable
import starships.colliders.AsteroidCollider

class Asteroid(position: Vector2, direction: Vector2, speed: Double,view: ImageView, val height: Double, val width: Double): MovableItem(position, direction, speed, view) {
    var asteroidCollider = AsteroidCollider(height - 10.0, width - 10.0, Math.toDegrees(direction.angle) - 90, this)
    val valueInPoints = 100
    init {
        asteroidCollider.shape.layoutX = position.x
        asteroidCollider.shape.layoutY = position.y
    }
    var destroyed = false
    fun destroy(){
        destroyed = true
    }

    override fun move(vector: Vector2, direction: Vector2){
        asteroidCollider.shape.layoutX= vector.x
        asteroidCollider.shape.layoutY = vector.y
        super.move(vector, direction)
    }
}
