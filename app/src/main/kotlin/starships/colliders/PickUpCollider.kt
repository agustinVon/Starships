package starships.colliders

import starships.spaceItems.PickUp

class PickUpCollider(height: Double, width: Double, angle: Double, val pickUp: PickUp): SpaceCollider(height, width, angle) {
    override fun collisionWithLaser(laserCollider: LaserCollider) {
    }

    override fun collisionWithAsteroid(asteroidCollider: AsteroidCollider) {
    }

    override fun collisionWithStarship(starShipDamageCollider: StarShipCollider) {
    }

    override fun collisionWithPickUp(pickUpCollider: PickUpCollider) {
    }

    override fun handleCollisionWith(collider: SpaceCollider) {
        collider.collisionWithPickUp(this)
    }
}