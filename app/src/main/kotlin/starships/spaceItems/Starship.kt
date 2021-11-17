package starships.spaceItems

import com.soywiz.klock.DateTime
import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import kotlinx.serialization.Serializable
import starships.Player
import starships.colliders.StarShipCollider
import starships.factorys.FastGun
import starships.factorys.Gun
import starships.factorys.LaserGun

class Starship(position: Vector2, direction: Vector2,
               speed: Double, val image: Image,
               var lives: Int, val height: Double, val width: Double,val gunSpeed: Double, val id:Int): MovableItem(position, direction, speed) {
    var hasFired = false
    private val defaultGun = LaserGun(10.0, 500)

    var specialGun:Gun = FastGun(10.0)
    var specialGunCoolDown = 0.seconds
    var timeSinceGrabbedSpecialGun = DateTime.now()
    var score = 0

    fun takeDamage(){
        lives -= 1
    }
    fun hasLost():Boolean{
        return lives <= 0
    }
    fun prepareToShoot(){
        hasFired = true
    }
    fun shootGun(): List<Laser>{
        hasFired = false
        val currentTime = DateTime.now()
        return if(currentTime - timeSinceGrabbedSpecialGun < specialGunCoolDown){
            specialGun.shoot(this)
        } else{
            defaultGun.shoot(this)
        }
    }
    fun addScore(score:Int){
        this.score += score
    }
    fun updateScore(player: Player){
        player.score = this.score
    }
}