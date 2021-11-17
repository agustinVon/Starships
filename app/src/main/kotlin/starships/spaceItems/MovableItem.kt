package starships.spaceItems

import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import kotlinx.serialization.Serializable

abstract class MovableItem(internal var position: Vector2, internal var direction: Vector2, val speed: Double) {
    open fun move(vector: Vector2, direction: Vector2){
        position = vector
        this.direction = direction.asUnitary()
    }
}