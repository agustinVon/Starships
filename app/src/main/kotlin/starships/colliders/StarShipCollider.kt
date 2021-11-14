package starships.colliders

import com.soywiz.klock.DateTime
import starships.spaceItems.Starship

class StarShipCollider(height: Double, width: Double, angle:Double, val starship: Starship): SpaceCollider(height, width, angle) {
    override fun collisionWithLaser(laserCollider: LaserCollider) {
        if(!laserCollider.laser.destroyed && starship.lives > 0){
            starship.takeDamage()
        }
    }

    override fun collisionWithAsteroid(asteroidCollider: AsteroidCollider) {
        if(!asteroidCollider.asteroid.destroyed && starship.lives > 0){
            starship.takeDamage()
        }
    }

    override fun collisionWithStarship(starShipDamageCollider: StarShipCollider) {
        if(!starShipDamageCollider.starship.hasLost() && starship.lives > 0){
            starship.takeDamage()
        }
    }

    override fun collisionWithPickUp(pickUpCollider: PickUpCollider) {
        if(!pickUpCollider.pickUp.destroyed){
            starship.specialGun = pickUpCollider.pickUp.gun
            starship.specialGunCoolDown = pickUpCollider.pickUp.cooldown
            starship.timeSinceGrabbedSpecialGun = DateTime.now()
            pickUpCollider.pickUp.destroyed = true
        }
    }

    override fun handleCollisionWith(collider: SpaceCollider) {
        collider.collisionWithStarship(this)
    }
}