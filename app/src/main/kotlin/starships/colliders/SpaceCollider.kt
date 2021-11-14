package starships.colliders

import edu.austral.dissis.starships.collision.Collider
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

abstract class SpaceCollider(height: Double, width: Double, angle: Double):Collider<SpaceCollider> {
    val shape = Rectangle(width, height)
    init{
        shape.rotate = angle
    }
    override fun getShape(): Shape {
        return shape
    }

    abstract fun collisionWithLaser(laserCollider: LaserCollider)
    abstract fun collisionWithAsteroid(asteroidCollider: AsteroidCollider)
    abstract fun collisionWithStarship(starShipDamageCollider: StarShipCollider)
    abstract fun collisionWithPickUp(pickUpCollider: PickUpCollider)

}