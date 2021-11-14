package starships.spaceItems

import com.soywiz.klock.internal.Serializable
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import starships.colliders.LaserCollider

class Laser(position: Vector2, direction: Vector2, speed: Double,
            view: ImageView, val height: Double,val width: Double, val onCrash:(score: Int) -> Unit, val starshipId:Int)
    : MovableItem(position, direction, speed, view) {
    val laserCollider = LaserCollider(height, width, Math.toDegrees(direction.angle) - 90, this)
    var destroyed = false

    fun destroy(){
        destroyed = true
    }

    override fun move(vector: Vector2, direction: Vector2){
        laserCollider.shape.layoutX= vector.x
        laserCollider.shape.layoutY = vector.y
        laserCollider.shape.rotate = Math.toDegrees(direction.angle) - 90
        super.move(vector, direction)
    }
}