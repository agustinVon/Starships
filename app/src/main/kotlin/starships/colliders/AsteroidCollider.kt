package starships.colliders

import kotlinx.serialization.Serializable
import starships.spaceItems.Asteroid

class AsteroidCollider(height: Double, width: Double, angle:Double, val asteroid:Asteroid): SpaceCollider(height, width, angle) {
    override fun collisionWithLaser(laserCollider: LaserCollider) {
        if(!asteroid.destroyed){
            asteroid.destroy()
        }
    }

    override fun collisionWithAsteroid(asteroidCollider: AsteroidCollider) {
    }

    override fun collisionWithStarship(starShipDamageCollider: StarShipCollider) {
        if(!asteroid.destroyed){
            asteroid.destroy()
        }
    }

    override fun collisionWithPickUp(pickUpCollider: PickUpCollider) {
    }

    override fun handleCollisionWith(collider: SpaceCollider) {
        collider.collisionWithAsteroid(this)
    }
}