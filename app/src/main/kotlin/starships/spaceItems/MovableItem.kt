package starships.spaceItems

import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import kotlinx.serialization.Serializable

abstract class MovableItem(internal var position: Vector2, internal var direction: Vector2, val speed: Double, val view:ImageView) {
    init {
        view.layoutX = position.x
        view.layoutY = position.y
        view.rotate = Math.toDegrees(direction.angle) - 90
    }
    open fun move(vector: Vector2, direction: Vector2){
        view.layoutX = vector.x
        view.layoutY = vector.y
        view.rotate = Math.toDegrees(direction.angle) - 90
        position = vector
        this.direction = direction.asUnitary()
    }
}