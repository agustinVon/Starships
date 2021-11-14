package starships.colliders

import starships.spaceItems.Laser

class LaserCollider(height: Double, width: Double, angle:Double, val laser: Laser): SpaceCollider(height, width, angle) {
    override fun collisionWithLaser(laserCollider: LaserCollider) {
    }

    override fun collisionWithAsteroid(asteroidCollider: AsteroidCollider) {
        if(!asteroidCollider.asteroid.destroyed && !laser.destroyed){
            val points = asteroidCollider.asteroid.valueInPoints
            laser.onCrash(asteroidCollider.asteroid.valueInPoints)
            laser.destroy()
        }
    }

    override fun collisionWithStarship(starShipDamageCollider: StarShipCollider) {
        if(!laser.destroyed){
            laser.destroy()
        }
    }

    override fun collisionWithPickUp(pickUpCollider: PickUpCollider) {
    }

    override fun handleCollisionWith(collider: SpaceCollider) {
        collider.collisionWithLaser(this)
    }
}